# Elytra Flight Detector

![Minecraft 1.21+](https://img.shields.io/badge/Minecraft-1.21+-green)
![Fabric](https://img.shields.io/badge/Fabric-API-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

A lightweight, client-side Minecraft mod that detects **real Elytra flight** and displays a clean, modern HUD indicator while you're actively gliding.

## Features

- **Accurate Elytra Detection** — Uses `player.isFallFlying()` for real flight state detection, not just armor slot checks
- **Modern HUD Overlay** — Clean Minecraft-style HUD with "ELYTRA FLYING" text, optional Elytra icon, and glow effects
- **Smooth Animations** — HUD fades in/out smoothly with no visual popping
- **Sound Notifications** — Optional subtle chime when Elytra flight begins
- **Full Config Screen** — Extensive configuration via Mod Menu + Cloth Config
- **Client-Side Only** — Works in multiplayer and singleplayer, no server-side installation needed
- **Performance Optimized** — Minimal performance impact, compatible with Sodium and optimization mods

## Installation

### Requirements
- **Minecraft:** 1.21+
- **Fabric Loader:** 0.16.0+
- **Fabric API:** 0.98.0+ (for 1.21)

### Steps
1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download the [Fabric API](https://modrinth.com/mod/fabric-api) mod and place it in your `.minecraft/mods/` folder
3. Download **Elytra Flight Detector** from [Modrinth](https://modrinth.com/mod/elytraflightdetector) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/elytraflightdetector)
4. Place the mod JAR in your `.minecraft/mods/` folder
5. Launch Minecraft with the Fabric profile

### Optional Dependencies
- [Mod Menu](https://modrinth.com/mod/modmenu) — Adds a config button in the mods menu
- [Cloth Config](https://modrinth.com/mod/cloth-config) — Required for the visual config screen (auto-installed with Mod Menu on most launchers)

## Configuration

Access the config screen via:
- **Mod Menu** → Click the config icon next to Elytra Flight Detector
- Or edit `config/elytraflightdetector.json` manually

### Config Options

| Option | Default | Description |
|--------|---------|-------------|
| HUD Position | Top Right | Screen position (6 options) |
| HUD Scale | `100` | Scale percentage (50-200%) |
| Text Color | Cyan | ARGB color picker |
| Enable Background | `true` | Semi-transparent background panel |
| Opacity | `80` | Overall transparency (10-100%) |
| Enable Icon | `true` | Show Elytra icon beside text |
| Enable Sound | `true` | Play sound on flight start |

## How It Works

This mod uses the **proper Minecraft flight detection method** (`LivingEntity.isFallFlying()`) to determine if the player is actively gliding with an Elytra. This is the same method Minecraft uses internally for Elytra physics, durability, and rendering.

### What triggers detection:
- ✅ Actively gliding with Elytra (after double-tap jump while falling with Elytra equipped)

### What does NOT trigger detection:
- ❌ Elytra merely equipped (walking on ground, standing still)
- ❌ Normal falling (without Elytra deployed)
- ❌ Jumping
- ❌ Creative/Spectator flight
- ❌ Standing on ground

## Compatibility

- **Sodium** — Fully compatible
- **Lithium** — Fully compatible
- **OptiFine** — Not tested (Fabric + OptiFine may have issues unrelated to this mod)
- **Iris Shaders** — Fully compatible
- **Other HUD Mods** — Designed not to interfere

## Building from Source

### Prerequisites
- JDK 21+
- Gradle 8.10 (wrapper included)

### Steps
```bash
git clone https://github.com/AdamJaroui23/ElytraFlightDetector.git
cd ElytraFlightDetector
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## Credits

- **Author:** AdamJaroui23
- **Icon:** AI-generated, custom design for Elytra Flight Detector
- **Built with:** [Fabric](https://fabricmc.net/), [Cloth Config](https://github.com/shedaniel/RoughlyEnoughItems), [Mod Menu](https://github.com/Prospector/ModMenu)

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

## Links

- [Modrinth](https://modrinth.com/mod/elytraflightdetector)
- [GitHub](https://github.com/AdamJaroui23/ElytraFlightDetector)
- [Issues](https://github.com/AdamJaroui23/ElytraFlightDetector/issues)
