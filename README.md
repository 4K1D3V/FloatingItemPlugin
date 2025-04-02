# FloatingItemPlugin

![GitHub](https://img.shields.io/github/license/4K1D3V/FloatingItemPlugin)

**FloatingItemPlugin** is a lightweight, industrial-grade Minecraft plugin for Paper 1.21.4 that allows players to create and manage floating items with customizable animations. Built with Java 21, it leverages modern features like virtual threads and sealed interfaces for performance and maintainability.

## Features

- **Floating Items**: Spawn items that hover with configurable animations (Sine, Bounce, Linear).
- **Customizable**: Adjust animation speed, height, rotation, particles, and sounds via `config.yml`.
- **Commands**: Create, remove, list, move, and manage floating items with an intuitive command system.
- **Interact Actions**: Define what happens when players click items (pickup, remove, teleport, or run a command).
- **Data Persistence**: Saves floating items to `data.yml` with automatic backups.
- **Performance**: Optimized with parallel streams for large-scale use and virtual threads for async operations.

## Installation

1. **Download**: Grab the latest `.jar` from the [Releases](https://github.com/4K1D3V/FloatingItemPlugin/releases) page.
2. **Place in Server**: Drop the `.jar` file into your server's `plugins` folder.
3. **Start Server**: Run your Paper 1.21.4 server to generate the default configuration files.
4. **Configure**: Edit `plugins/FloatingItemPlugin/config.yml` to customize settings (see [Configuration](#configuration)).

## Requirements

- **Server**: Paper 1.21.4 (or compatible forks)
- **Java**: Java 21 (required for compilation and runtime)

## Commands

All commands require the `floatitem.admin` permission (default: OP only).

| Command                        | Description                                  |
|--------------------------------|----------------------------------------------|
| `/floatitem create`            | Create a floating item with the held item    |
| `/floatitem remove <id>`       | Remove a floating item by its UUID           |
| `/floatitem list [page]`       | List all active floating items (paginated)   |
| `/floatitem reload`            | Reload the configuration file                |
| `/floatitem setpattern <id> <pattern>` | Set animation pattern (SINE, BOUNCE, LINEAR) |
| `/floatitem move <id> <x> <y> <z>` | Move a floating item to new coordinates      |
| `/floatitem removeall`         | Remove all floating items                    |
| `/floatitem help`              | Display command help                         |

### Tab Completion
- Full tab completion support for subcommands, UUIDs, patterns, and coordinates.

## Configuration

The `config.yml` file is generated in `plugins/FloatingItemPlugin/` on first run. Below are the key options:

```yaml
animation:
  hover-amplitude: 0.1       # Height of hover (0.0 to 1.0)
  rotation-speed: 0.05       # Rotation speed (radians/sec, 0.0 to 1.0)
  update-frequency: 1        # Animation update ticks (1 to 20)
  default-pattern: "SINE"    # Default pattern: SINE, BOUNCE, LINEAR
item:
  spawn-height: 1.5          # Height above spawn point (0.5 to 3.0)
  allowed-types: []          # Allowed item types (empty = all allowed)
effects:
  particles: false           # Enable particle effects
  particle-type: "FLAME"     # Particle type (Bukkit Particle names)
  particle-count: 2          # Particles per update (1 to 50)
  interact-particle: "HEART" # Particle on interact
  interact-particle-count: 5 # Interact particle count (1 to 50)
sounds:
  success: "ENTITY_EXPERIENCE_ORB_PICKUP" # Success sound
  error: "BLOCK_ANVIL_LAND"               # Error sound
  info: "BLOCK_NOTE_BLOCK_PLING"          # Info sound
  warning: "ENTITY_VILLAGER_NO"           # Warning sound
  interact: "ENTITY_ITEM_PICKUP"          # Interact sound
  volume: 1.0              # Sound volume (0.0 to 2.0)
  pitch: 1.0               # Sound pitch (0.5 to 2.0)
interact:
  action: "PICKUP"         # Action on click: NONE, PICKUP, REMOVE, TELEPORT, COMMAND
  command: "say %player% interacted with a floating item" # Command if action is COMMAND
worlds:
  allowed-worlds: []       # Allowed worlds (empty = all allowed)
backup:
  enabled: true            # Enable data backups
  max-backups: 5           # Max number of backups (1 to 10)
```

### Data Persistence
- Floating items are saved to `plugins/FloatingItemPlugin/data.yml`.
- Backups are stored in `plugins/FloatingItemPlugin/backups/` if enabled.

## Permissions

- `floatitem.admin`: Grants access to all `/floatitem` commands (default: OP).

## Building from Source

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/4K1D3V/FloatingItemPlugin.git
   cd FloatingItemPlugin
   ```
2. **Build with Maven**:
   ```bash
   mvn clean package
   ```
3. **Output**: The `.jar` will be in the `target/` folder.

### Prerequisites
- Java 21 JDK
- Maven 3.6+

## Contributing

Contributions are welcome! Follow these steps:

1. **Fork** the repository.
2. **Create** a feature branch (`git checkout -b feature/YourFeature`).
3. **Commit** your changes (`git commit -m "Add YourFeature"`).
4. **Push** to your branch (`git push origin feature/YourFeature`).
5. **Open** a Pull Request.

Please ensure your code follows the existing style and includes tests where applicable.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

- **Author**: KiteGG
- **Built with**: Java 21, Paper API