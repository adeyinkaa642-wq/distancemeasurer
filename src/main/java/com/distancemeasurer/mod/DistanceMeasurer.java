package com.distancemeasurer.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class DistanceMeasurer {

    private boolean enabled = false;
    private BlockPos pos1 = null;
    private BlockPos pos2 = null;
    private int awaitingClick = 1;
    private static final int TOGGLE_KEY = GLFW.GLFW_KEY_G;
    private boolean keyWasDown = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        boolean keyDown = GLFW.glfwGetKey(mc.getWindow().getWindow(), TOGGLE_KEY) == GLFW.GLFW_PRESS;

        if (keyDown && !keyWasDown) {
            enabled = !enabled;
            pos1 = null;
            pos2 = null;
            awaitingClick = 1;

            if (enabled) {
                mc.player.sendMessage(
                    new StringTextComponent(TextFormatting.GREEN + "[Measure] ON — Right-click block 1"),
                    mc.player.getUUID()
                );
            } else {
                mc.player.sendMessage(
                    new StringTextComponent(TextFormatting.RED + "[Measure] OFF"),
                    mc.player.getUUID()
                );
            }
        }
        keyWasDown = keyDown;
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!enabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && event.getAction() == GLFW.GLFW_PRESS) {
            RayTraceResult result = mc.hitResult;
            if (result == null || result.getType() != RayTraceResult.Type.BLOCK) return;

            BlockPos hit = ((BlockRayTraceResult) result).getBlockPos();

            if (awaitingClick == 1) {
                pos1 = hit;
                awaitingClick = 2;
                mc.player.sendMessage(
                    new StringTextComponent(TextFormatting.YELLOW + "[Measure] Pos 1: " + formatPos(pos1) + " — Now right-click Pos 2"),
                    mc.player.getUUID()
                );
            } else {
                pos2 = hit;
                awaitingClick = 1;
                showDistances(mc);
                pos1 = null;
                pos2 = null;
            }
        }
    }

    private void showDistances(Minecraft mc) {
        if (pos1 == null || pos2 == null || mc.player == null) return;

        int dx = Math.abs(pos2.getX() - pos1.getX());
        int dy = Math.abs(pos2.getY() - pos1.getY());
        int dz = Math.abs(pos2.getZ() - pos1.getZ());
        double distance3d = Math.sqrt(dx * dx + dy * dy + dz * dz);

        mc.player.sendMessage(new StringTextComponent(TextFormatting.AQUA + "--- Distance Measurement ---"), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.WHITE + "From: " + formatPos(pos1)), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.WHITE + "To:   " + formatPos(pos2)), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "X diff: " + dx + " blocks  |  Z diff: " + dz + " blocks"), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "Vertical (Y): " + dy + " blocks"), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.GOLD + "3D Distance: " + String.format("%.2f", distance3d) + " blocks"), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.AQUA + "----------------------------"), mc.player.getUUID());
        mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "[Measure] Right-click Pos 1 for next measurement"), mc.player.getUUID());
    }

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Text event) {
        if (!enabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        String status = awaitingClick == 1
            ? TextFormatting.GREEN + "[Measure ON] Right-click Pos 1"
            : TextFormatting.YELLOW + "[Measure ON] Pos 1 set — Right-click Pos 2";

        event.getLeft().add("");
        event.getLeft().add(status);
    }

    private String formatPos(BlockPos pos) {
        return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
    }
}
