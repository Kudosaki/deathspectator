# DeathSpectator

A highly configurable Paper plugin that provides a cinematic spectator camera and auto-respawn countdown when players die.

## Features
- **Cinematic Spectator View**: Automatically attaches player cameras to an invisible anchor viewing their death point.
- **Configurable Camera Offsets**: Set camera distance and target height independently.
- **Auto-Respawn & Countdown**: Show remaining time via Action Bar, Title, or Chat.
- **Sneak to Skip**: Players can press Shift to instantly skip the death timer.
- **100% Paper Native**: Uses non-destructive paper APIs and PDC tracking (No NMS required).
- **PlaceholderAPI Support**: Native support for PAPI placeholders.

## Requirements
- Minecraft Server running **Paper 1.21.11** (or compatible 1.21.x build).
- Java **21** or higher.

## Commands & Permissions
| Command | Description | Permission |
| :--- | :--- | :--- |
| `/deathspectator reload` | Reload configuration files | `deathspectator.reload` |
| `/deathspectator force <player>` | Test spectator camera on a player | `deathspectator.force` |
| `/deathspectator stop <player>` | Force stop spectator session for a player | `deathspectator.stop` |
| `/deathspectator respawn <player>`| Instantly respawn spectating player | `deathspectator.respawn` |
| `/deathspectator version` | Display plugin version and dependencies | `deathspectator.admin` |

## Configuration Example
```yaml
death:
  respawn:
    delay: 3
    sneak-to-skip: true
    automatic-respawn: true
    countdown:
      enabled: true
      display: ACTION_BAR
      message: "<gray>Respawning in <yellow><seconds></yellow><gray>..."
