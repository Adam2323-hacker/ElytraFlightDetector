package com.adamjaroui23.elytraflightdetector.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

/**
 * Mod Menu integration for Elytra Flight Detector.
 *
 * This class registers our config screen with the Mod Menu mod,
 * allowing users to access our configuration from the Mod Menu
 * interface in the Minecraft menu screen.
 *
 * Mod Menu will automatically detect this class via the "modmenu"
 * entrypoint in fabric.mod.json.
 *
 * @author AdamJaroui23
 */
public class ModMenuIntegration implements ModMenuApi {

    /**
     * Provides the config screen factory for Mod Menu.
     * This returns a Cloth Config screen built from our AutoConfig data.
     *
     * @return a factory that creates our mod's configuration screen
     */
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }
}
