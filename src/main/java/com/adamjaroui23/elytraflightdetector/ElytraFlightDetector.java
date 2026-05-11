package com.adamjaroui23.elytraflightdetector;

import com.adamjaroui23.elytraflightdetector.config.ModConfig;
import com.adamjaroui23.elytraflightdetector.hud.ElytraHudRenderer;
import com.adamjaroui23.elytraflightdetector.keybind.ModKeybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elytra Flight Detector - Main mod entry point.
 *
 * This lightweight client-side mod detects when the player is actively flying
 * with an Elytra using the proper Minecraft flight state method
 * (player.isFallFlying()). It renders a configurable HUD indicator while
 * Elytra flight is active and provides optional sound notifications.
 *
 * Key Detection Logic:
 * - We use player.isFallFlying() which is the authoritative Minecraft method
 *   for determining if a player is actively gliding with an Elytra.
 * - This method returns true ONLY when:
 *   1. The player has initiated Elytra flight (double-tap jump while falling)
 *   2. The Elytra is currently deployed and the player is gliding
 * - It returns false when:
 *   1. The Elytra is merely equipped but not in use
 *   2. The player is falling normally (not gliding)
 *   3. The player is jumping or on the ground
 *   4. The player is using creative flight (spectator/creative mode fly)
 *
 * IMPORTANT: We do NOT check armor slots or item stacks. The detection is
 * entirely based on the actual flight state, making it accurate and
 * compatible with any mods that modify Elytra behavior.
 *
 * @author AdamJaroui23
 * @version 1.0.0
 * @license MIT
 */
public class ElytraFlightDetector implements ClientModInitializer {

    public static final String MOD_ID = "elytraflightdetector";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Tracks the current Elytra flight state.
     * Updated every client tick for real-time detection.
     */
    private static boolean isFlying = false;

    /**
     * Tracks the previous tick's flight state for edge detection.
     * Used to detect the exact moment flight starts/stops for sound triggers.
     */
    private static boolean wasFlying = false;

    /**
     * Fade animation progress (0.0 = fully invisible, 1.0 = fully visible).
     * Used for smooth HUD transitions.
     */
    private static float hudFade = 0.0f;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Elytra Flight Detector initializing...");

        // Load configuration
        ModConfig.init();

        // Register keybinds
        ModKeybinds.register();

        // Register the tick-based flight detection system
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) {
                isFlying = false;
                return;
            }

            /*
             * CORE ELYTRA FLIGHT DETECTION
             *
             * client.player.isGliding() is the canonical Minecraft method for checking
             * if a player is actively using an Elytra to glide. This is the same
             * method used internally by Minecraft for:
             * - Elytra physics calculations
             * - Elytra durability reduction
             * - Elytra rendering state
             * - Elytra-related advancement triggers
             *
             * In Yarn 1.21.4 mappings, this method is named isGliding() (was
             * isFallFlying() in older versions/Mojang mappings). It returns true
             * ONLY when the player has initiated Elytra flight and is actively gliding.
             *
             * Why not check the chest armor slot?
             * - Checking armor slots only tells us if an Elytra is EQUIPPED, not
             *   whether the player is actively FLYING with it.
             * - A player can wear an Elytra while walking on the ground, falling
             *   normally, or standing still. We only want to detect active flight.
             * - Some mods may grant Elytra flight without an Elytra item equipped.
             * - Using isGliding() is the most accurate and future-proof approach.
             *
             * Why not use creative flight check?
             * - isGliding() specifically returns false for creative/spectator flight.
             * - This ensures we only detect real Elytra gliding, not creative flying.
             */
            wasFlying = isFlying;
            isFlying = client.player.isGliding();

            // Handle toggle keybind press
            while (ModKeybinds.toggleKey.wasPressed()) {
                ModConfig.getConfig().enabled = !ModConfig.getConfig().enabled;
                ModConfig.save();
                LOGGER.info("HUD toggled: {}", ModConfig.getConfig().enabled ? "ON" : "OFF");
            }

            // Smooth fade animation for the HUD
            if (isFlying && ModConfig.getConfig().enabled) {
                // Fade in: smoothly increase opacity
                hudFade = Math.min(1.0f, hudFade + 0.15f);
            } else {
                // Fade out: smoothly decrease opacity
                hudFade = Math.max(0.0f, hudFade - 0.1f);
            }

            // Trigger sound notification on flight START (rising edge detection)
            if (isFlying && !wasFlying && ModConfig.getConfig().soundEnabled) {
                ElytraHudRenderer.playFlightStartSound(client);
            }
        });

        // Register the HUD renderer
        // This callback fires every frame for smooth rendering
        // MC 1.21.4: HudRenderCallback uses RenderTickCounter instead of float tickDelta
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (hudFade > 0.01f) {
                ElytraHudRenderer.render(drawContext, hudFade);
            }
        });

        LOGGER.info("Elytra Flight Detector initialized successfully!");
    }

    /**
     * @return true if the player is currently actively flying with an Elytra
     */
    public static boolean isElytraFlying() {
        return isFlying;
    }

    /**
     * @return the current HUD fade value (0.0 to 1.0)
     */
    public static float getHudFade() {
        return hudFade;
    }
}
