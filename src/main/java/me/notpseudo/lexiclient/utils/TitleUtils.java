package me.notpseudo.lexiclient.utils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import static me.notpseudo.lexiclient.LexiClient.mc;

/**
 * This class is from https://github.com/inglettronald/DulkirMod/blob/master/src/main/kotlin/dulkirmod/DulkirMod.kt
 */
public class TitleUtils {

    private static String curString = "";
    private static long endTime = 0;
    private static float customX, customY, customSize;
    private static boolean useCustomPos, useCustomSize = false;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (System.currentTimeMillis() > endTime) {
            useCustomPos = false;
            useCustomSize = false;
            return;
        }
        int width = mc.fontRendererObj.getStringWidth(curString);
        ScaledResolution res = new ScaledResolution(mc);
        double screenWidth = res.getScaledWidth_double();
        double screenHeight = res.getScaledHeight_double();
        double baseScale = useCustomSize ? customSize : .7f;
        double scale = ((screenWidth - 100) * baseScale) / width;
        double xCoord = useCustomPos ? customX : screenWidth / 2;
        double yCoord = useCustomPos ? customY : screenHeight / 2;
        scale = Math.min(scale, 10);
        GlStateManager.pushMatrix();
        GlStateManager.translate((xCoord - width * scale / 2), yCoord - (4.5 * scale), 0.0);
        GlStateManager.scale(scale, scale, scale);
        mc.fontRendererObj.drawString(curString, 0f, 0f, 0, false);
        GlStateManager.popMatrix();
    }

    public static void showTitleForTime(String string, int timems) {
        curString = string;
        endTime = System.currentTimeMillis() + timems;
        useCustomPos = false;
    }
    public static void showTitleForTime(String string, int timems, float scale) {
        curString = string;
        endTime = System.currentTimeMillis() + timems;
        useCustomPos = false;
        customSize = scale;
        useCustomSize = true;
    }

    public static void showTitleForTime(String string, int timems, float x, float y, float scale) {
        curString = string;
        endTime = System.currentTimeMillis() + timems;
        customX = x;
        customY = y;
        useCustomPos = true;
        customSize = scale;
        useCustomSize = true;
    }

}
