package com.adamjaroui23.elytraflightdetector.hud;

import com.adamjaroui23.elytraflightdetector.ElytraFlightDetector;
import com.adamjaroui23.elytraflightdetector.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;

/**
 * HUD Renderer for Elytra Flight Detector.
 *
 * Renders the Elytra flight indicator on screen when the player is actively
 * flying with an Elytra. The HUD features:
 * - "ELYTRA FLYING" text label
 * - Optional Elytra wing icon (uses Minecraft's built-in elytra texture)
 * - Semi-transparent background for readability
 * - Smooth fade in/out animation
 * - Configurable position, scale, color, and opacity
 *
 * Rendering approach:
 * - Uses vanilla Minecraft rendering APIs (DrawContext) for full compatibility
 * - Scales coordinates using MatrixStack for resolution independence
 * - Alpha blending for transparency and fade effects
 * - Positioned relative to the screen edges to avoid overlap with other HUDs
 *
 * Performance considerations:
 * - Only renders when hudFade > 0 (virtually zero cost when hidden)
 * - Uses simple geometric primitives (no complex shader effects)
 * - Allocates no objects during rendering (no garbage collection pressure)
 * - All calculations are lightweight arithmetic
 *
 * MC 1.21.4 Compatibility Notes:
 * - DrawableHelper was removed; use DrawContext.fill() directly
 * - drawBorder() is no longer on DrawContext; we draw border lines manually
 * - drawTexture() uses the (Identifier, x, y, u, v, w, h) overload
 * - MatrixStack.translate() requires 3 arguments (x, y, z)
 *
 * @author AdamJaroui23
 */
public class ElytraHudRenderer {

    // The main HUD text displayed during Elytra flight
    private static final Text HUD_TEXT = Text.literal("ELYTRA FLYING");

    // Identifier for Minecraft's built-in elytra item texture (used for the icon)
    private static final Identifier ELYTRA_ICON = Identifier.ofVanilla("textures/item/elytra.png");

    /**
     * Main render method called every frame by the HudRenderCallback.
     *
     * @param drawContext the rendering context providing access to the vertex consumer and matrices
     * @param fade the current fade value (0.0 to 1.0), used for smooth transitions
     */
    public static void render(DrawContext drawContext, float fade) {
        ModConfig config = ModConfig.getConfig();
        if (!config.enabled && fade <= 0.01f) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Calculate base alpha from fade and config opacity
        int baseAlpha = (int) (fade * (config.opacity / 100.0f) * 255.0f);
        baseAlpha = MathHelper.clamp(baseAlpha, 0, 255);

        // Calculate scale factor from config (stored as percentage)
        float scale = config.hudScale / 100.0f;

        // Get window dimensions for positioning
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Measure text width at the current scale
        int textWidth = client.textRenderer.getWidth(HUD_TEXT);
        int textHeight = client.textRenderer.fontHeight;

        // Calculate icon dimensions (if enabled)
        int iconSize = (int) (16 * scale);
        int iconWidth = config.iconEnabled ? iconSize + 4 : 0; // 4px gap between icon and text

        // Total HUD element dimensions
        int totalWidth = (int) ((textWidth + iconWidth) * scale);
        int totalHeight = (int) ((textHeight + 4) * scale); // 4px vertical padding

        // Calculate position based on config setting
        int x = calculateX(config.hudPosition, screenWidth, totalWidth);
        int y = calculateY(config.hudPosition, screenHeight, totalHeight);

        // Render background if enabled
        if (config.backgroundEnabled && baseAlpha > 10) {
            int bgAlpha = (int) (baseAlpha * 0.4f); // Background is 40% of text opacity
            int bgColor = (bgAlpha << 24) | 0x00000000; // Black background
            int bgPadding = (int) (4 * scale);

            int left = x - bgPadding;
            int top = y - bgPadding;
            int right = x + totalWidth + bgPadding;
            int bottom = y + totalHeight + bgPadding;

            // Fill background using DrawContext.fill (MC 1.21.4 API)
            drawContext.fill(left, top, right, bottom, bgColor);

            // Draw border lines manually (drawBorder removed in MC 1.21.4)
            int borderAlpha = (int) (baseAlpha * 0.6f);
            int borderColor = (borderAlpha << 24) | (config.textColor & 0x00FFFFFF);
            int thickness = 1;

            // Top edge
            drawContext.fill(left, top - thickness, right, top, borderColor);
            // Bottom edge
            drawContext.fill(left, bottom, right, bottom + thickness, borderColor);
            // Left edge
            drawContext.fill(left - thickness, top - thickness, left, bottom + thickness, borderColor);
            // Right edge
            drawContext.fill(right, top - thickness, right + thickness, bottom + thickness, borderColor);
        }

        // Render main HUD text
        int textColor = (baseAlpha << 24) | (config.textColor & 0x00FFFFFF);
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x + iconWidth, y + 2, 0.0);
        drawContext.getMatrices().scale(scale, scale, 1.0f);
        drawContext.drawTextWithShadow(client.textRenderer, HUD_TEXT, 0, 0, textColor);
        drawContext.getMatrices().pop();

        // Render Elytra icon if enabled
        if (config.iconEnabled) {
            int iconAlpha = baseAlpha;
            // MC 1.21.4: setShaderColor is on RenderSystem, not DrawContext
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, iconAlpha / 255.0f);
            // MC 1.21.4: drawTexture requires Function<Identifier, RenderLayer> as first param
            // RenderLayer.getGuiTextured() returns the correct GUI texture RenderLayer
            drawContext.drawTexture(
                    RenderLayer::getGuiTextured,
                    ELYTRA_ICON,
                    x,
                    y + (int) ((totalHeight - iconSize) / 2.0),
                    0.0f, 0.0f,
                    iconSize, iconSize,
                    16, 16
            );
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
        }
    }

    /**
     * Calculates the X coordinate based on the selected HUD position.
     *
     * @param position the configured HUD position
     * @param screenWidth the current screen width in scaled pixels
     * @param totalWidth the total width of the HUD element
     * @return the X coordinate for the top-left corner of the HUD
     */
    private static int calculateX(ModConfig.HudPosition position, int screenWidth, int totalWidth) {
        int margin = 8;
        return switch (position) {
            case TOP_LEFT, BOTTOM_LEFT -> margin;
            case TOP_CENTER, BOTTOM_CENTER -> (screenWidth - totalWidth) / 2;
            case TOP_RIGHT, BOTTOM_RIGHT -> screenWidth - totalWidth - margin;
        };
    }

    /**
     * Calculates the Y coordinate based on the selected HUD position.
     *
     * @param position the configured HUD position
     * @param screenHeight the current screen height in scaled pixels
     * @param totalHeight the total height of the HUD element
     * @return the Y coordinate for the top-left corner of the HUD
     */
    private static int calculateY(ModConfig.HudPosition position, int screenHeight, int totalHeight) {
        int margin = 8;
        return switch (position) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> margin;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> screenHeight - totalHeight - margin;
        };
    }

    /**
     * Plays a subtle notification sound when Elytra flight begins.
     *
     * Uses Minecraft's experience orb level-up sound at reduced volume
     * for a pleasant, non-intrusive notification that doesn't interfere
     * with gameplay audio cues.
     *
     * The sound is played at the player's position so it has proper
     * spatial audio in multiplayer.
     *
     * @param client the Minecraft client instance
     */
    public static void playFlightStartSound(MinecraftClient client) {
        if (client.player == null) return;

        // Play a subtle chime at 40% volume (0.4f) at the player's position
        // Using the experience orb level-up sound for a pleasant notification feel
        client.player.playSoundToPlayer(
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                SoundCategory.PLAYERS,
                0.4f,  // Volume: 40% (subtle, not intrusive)
                1.5f   // Pitch: slightly higher than normal for a "notification" feel
        );
    }
}
