package com.adamjaroui23.elytraflightdetector.keybind;

import com.adamjaroui23.elytraflightdetector.ElytraFlightDetector;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Keybind registration for Elytra Flight Detector.
 *
 * Registers a toggle keybind (default: Right Shift) that allows the player
 * to instantly enable/disable the HUD overlay during gameplay.
 *
 * The keybind is registered through Fabric's KeyBindingHelper, which ensures
 * proper integration with Minecraft's controls menu and keybind conflict system.
 *
 * @author AdamJaroui23
 */
public class ModKeybinds {

    /**
     * The keybinding used to toggle the HUD on/off.
     * Default: Right Shift (GLFW_KEY_RIGHT_SHIFT).
     *
     * Registered in the "misc" category so it appears in the
     * Minecraft Controls > Misc menu where players can rebind it.
     */
    public static KeyBinding toggleKey;

    /**
     * Registers all mod keybinds.
     * Called once during mod initialization (onInitializeClient).
     *
     * The keybind category "key.categories.misc" maps to the
     * "Miscellaneous" section in Minecraft's Controls menu.
     */
    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + ElytraFlightDetector.MOD_ID + ".toggle_hud",       // Translation key
                InputUtil.Type.KEYSYM,                                       // Input type (keyboard)
                GLFW.GLFW_KEY_RIGHT_SHIFT,                                   // Default key: Right Shift
                "key.categories.misc"                                        // Category in controls menu
        ));

        ElytraFlightDetector.LOGGER.info("Keybinds registered. Toggle key: Right Shift");
    }

    /**
     * Gets the display name for the toggle keybind.
     * Useful for rendering the keybind name in tooltips or messages.
     *
     * @return the localized text for the toggle keybind
     */
    public static Text getToggleKeyName() {
        return Text.translatable("key." + ElytraFlightDetector.MOD_ID + ".toggle_hud");
    }
}
