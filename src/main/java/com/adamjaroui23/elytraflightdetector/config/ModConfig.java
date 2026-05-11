package com.adamjaroui23.elytraflightdetector.config;

import com.adamjaroui23.elytraflightdetector.ElytraFlightDetector;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

/**
 * Configuration system for Elytra Flight Detector.
 *
 * Uses Cloth Config (via AutoConfig) to provide a visual config screen
 * accessible through Mod Menu. All settings are saved to a JSON file
 * in the Minecraft config directory.
 *
 * The config file is automatically created on first load with default values.
 *
 * @author AdamJaroui23
 */
@Config(name = ElytraFlightDetector.MOD_ID)
public class ModConfig implements ConfigData {

    /**
     * Enable/disable the Elytra flight HUD overlay.
     * When disabled, no HUD elements are rendered even during flight.
     */
    public boolean enabled = true;

    /**
     * HUD horizontal position on screen.
     * Values: 0 = left, 1 = center, 2 = right
     */
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public HudPosition hudPosition = HudPosition.TOP_RIGHT;

    /**
     * Scale factor for the HUD display.
     * 1.0 = normal, 0.5 = half size, 2.0 = double size.
     */
    @ConfigEntry.BoundedDiscrete(min = 50, max = 200)
    @ConfigEntry.Gui.Tooltip(count = 1)
    public int hudScale = 100;

    /**
     * HUD text color in ARGB format.
     * Default: bright cyan (0x00FFFFFF).
     */
    @ConfigEntry.ColorPicker
    public int textColor = 0x00FFFFFF;

    /**
     * Enable/disable the semi-transparent background behind the HUD text.
     * The background improves readability over varying game scenes.
     */
    public boolean backgroundEnabled = true;

    /**
     * HUD opacity/transparency (0-100).
     * Controls the overall transparency of the HUD elements.
     */
    @ConfigEntry.BoundedDiscrete(min = 10, max = 100)
    @ConfigEntry.Gui.Tooltip(count = 1)
    public int opacity = 80;

    /**
     * Enable/disable the Elytra icon next to the HUD text.
     * When enabled, a small Elytra wing icon is rendered beside the text.
     */
    public boolean iconEnabled = true;

    /**
     * Enable/disable the sound notification when Elytra flight begins.
     * Plays a subtle chime when you start gliding.
     */
    public boolean soundEnabled = true;

    /**
     * HUD position enum for the config screen.
     * Determines where on the screen the HUD is rendered.
     */
    public enum HudPosition {
        TOP_LEFT("Top Left"),
        TOP_CENTER("Top Center"),
        TOP_RIGHT("Top Right"),
        BOTTOM_LEFT("Bottom Left"),
        BOTTOM_CENTER("Bottom Center"),
        BOTTOM_RIGHT("Bottom Right");

        private final String displayName;

        HudPosition(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Initializes the AutoConfig system and registers the config.
     * Called once during mod initialization.
     */
    public static void init() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ElytraFlightDetector.LOGGER.info("Config system initialized.");
    }

    /**
     * @return the current mod configuration instance
     */
    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    /**
     * Saves the current configuration to disk.
     * Should be called after any runtime config changes.
     */
    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
