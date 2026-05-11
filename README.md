# Elytra Flight Detector

![Minecraft 1.21.4+](https://img.shields.io/badge/Minecraft-1.21.4+-green)
![Fabric](https://img.shields.io/badge/Loader-Fabric-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

A lightweight client-side Minecraft mod that detects when you're actively flying with an Elytra and displays a customizable HUD indicator.

## Features

* Real Elytra flight detection using `player.isFallFlying()`
* HUD indicator while gliding
* Instant flight state updates
* Optional Elytra icon
* Optional sound notification
* Configurable HUD settings
* Lightweight and optimized
* Client-side only
* Multiplayer compatible
* Sodium compatible
* Mod Menu integration
* Cloth Config support

## Installation

### Requirements

* Minecraft 1.21.4+
* Fabric Loader
* Fabric API

### Steps

1. Install Fabric Loader
2. Install Fabric API
3. Place the mod JAR into your `.minecraft/mods` folder
4. Launch Minecraft using the Fabric profile

### Optional Dependencies

* Mod Menu
* Cloth Config

## Configuration

The mod includes a configurable HUD system.

Available options:

* HUD position
* HUD scale
* Text color
* Background visibility
* Opacity
* Sound notifications
* Icon visibility

## How It Works

This mod uses Minecraft's real Elytra flight detection system:

```java
player.isFallFlying()
```

The HUD only appears when the player is actively gliding with an Elytra.

### Triggers Detection

* Active Elytra gliding

### Does NOT Trigger

* Elytra simply equipped
* Normal falling
* Jumping
* Creative flight
* Standing on the ground

## Compatibility

* Sodium — Compatible
* Iris — Compatible
* Multiplayer Servers — Compatible
* Other HUD Mods — Compatible

## Building from Source

```bash
git clone https://github.com/AdamJaroui23/ElytraFlightDetector.git
cd ElytraFlightDetector
./gradlew build
```

The compiled JAR will be located in:

```text
build/libs/
```

## Author

AdamJaroui23

## License

MIT License

## Links
- [Modrinth](https://modrinth.com/mod/elytra-flight-detector)
- [GitHub](https://github.com/AdamJaroui23/ElytraFlightDetector)
- [Issues](https://github.com/AdamJaroui23/ElytraFlightDetector/issues)
