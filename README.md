# Inspect Animations Antique

Inspect Animations adds new animations for items, inspired by the inspect feature from counterstrike.

![demo.gif](https://cdn.modrinth.com/data/fRpkaLG9/images/a218ae1e61c21884ecd4633cf40ed00fca1259a7.gif)

### Animations

| Animation |                                                         |
|-----------|---------------------------------------------------------|
| Turn      | Classic counterstrike inspect animation.                |
| Toss      | Toss the item up  in the air and catch it.              |
| Flourish  | Twirl the item back and forth like flourishing a sword. |
| Flip      | Flip the item around in your hand.                      |

### Keybinds

Press [R] to play the selected animation.

Hold [SHIFT] and press [R] to cycle through the available animations.

### Multiplayer

Use the gamerule `inspect_radius` to change the distance from which other players will receive animation packets.

Use the gamerule `send_inspections` to enable/disable sending animations to players. When setting from false to true, players already in the server will need to re-join to apply changes.

### Configuration

Use Mod Menu to configure the mod:

- Animations can be removed from the cycling option
- Animation duration can be changed
- Some animations can have their number of loops changed

There are a few experimental options:

- Select specific animations for items and item tags
- Select a specific animation for all blocks
- Maximum Delta: Try changing this value if the animation does not play smoothly or skips during lag spikes

### Port Notice

Inspect Animations Antique is a Minecraft Forge 1.12.2 port of SoundsoftheSun's original Inspect Animations mod.

- [Original mod on CurseForge](https://www.curseforge.com/minecraft/mc-mods/inspect-animations)
- [Original source repository on Codeberg](https://codeberg.org/SoundsoftheSun/inspect-animations)
