# Dog Studio CS+2026 Fork
A Cave Story level editor built off of Noxid’s Booster’s Lab. Don't get it twisted, though, it's its own thing.

# Purpose  
I wanted to create a customizable "everything editor" that allows for personalization and efficiency. Now you won't have to sit and go "Wow, I really wish the editor could do that" because now it does.

# Features  
I’m going to assume you’re familiar with Booster’s Lab. If not, that’s okay, too. Here is a list of all the features:

### General

- **Customizable Themes**  
  - You can easily create and switch between themes. The lab is now yours!
- **Discord RPC**  
  - Showcase your love for this Editor!!
- **Cave Story+ 2026 Support**  
  - Now you can finally use Dog Booster instead of the Booster's Lab Fork.
- **Auto-save**  
  - With choices between off, 2 minutes, 5 minutes, & 15 minutes...
- **Recent Files**  
  - Up to ten of your recently loaded projects appear here.  
- **New .PXA Editor**  
  - It's SDE. It's literally just SDE. (hi jakzie)
- **Map Tab Reordering**  
  - Map tabs can be dragged around and reordered.  
- **Map Dragging**
  - Holding the middle click and moving your mouse will drag the map.  
- **Tab Keybinds**  
  - The tile, entity, script, and mapdata tabs have been bound to F1, F2, F3, and F4.  
- **Editor Configuration**  
  - A new window has been added under the Editor tab. Here you can add/remove/edit TSC commands, END commands, songs, sound effects, equips, map bosses, and background types.
  - You can also choose to disable SFX, script errors, and command errors. You can also change the sensitivity of the map scroll bar.
- **Autumnal Lab Mode**  
  - A new option has also been added which will give your editor all of the Autumnal Lab features.
- **Unload Mod**  
  - Replaced the “New” option. Unloads the current mod.
- **Rect Generator**  
  - A niche but helpful feature for those who need it!
- **NPC Flag Location**  
  - A spicy change, but once you get used to it it's better than scrolling down the list each time. If enough people want the old one back I'll try to make it an option.
- **Misc Changes**  
  - Niche actions have been given their own submenu.
  - There is now a search filter for both the map and entity lists.

### Script

- **Window Docking**  
  - You can now dock the script window.  
- **Selectable Things**  
  - You can pull up a graphic or list to select which thing you want when it comes to faces, items, maps, songs, sfx, and direction.
- **Command Bar Position**  
  - A “flip” button has been added allowing you to move the command bar from right to left and vice-versa.  
- **Undo/Redo**  
  - Ctrl-Z and Ctrl-Y for the script editor.
- **Read CS+2026's unencrypted TSC scripts and new PXA files**  
  - Super, super cool, right?
- **Command Input Boxes**  
  - There are now input boxes for changing values.  
- **List Selection**  
  - Maps, Songs, SFXs, and Equips can be selected from a list now.  
- **Graphic Selection**  
  - Face Portraits, Items, and Weapon graphics can be selected from their image.  
- **Six Operand Support**  
  - Up to six operands are supported by the editor. (wwww:xxxx:yyyy:zzzz:qqqq:rrrr)  
- **String Support**  
  - String$ support has been added.  
- **Flag Color**  
  - Flag numbers have a slightly different color.  
- **Goto Event**  
  - Clicking the arrow next to event numbers will highlight them.  
- **Zoom in/out**  
  - Hold control and scroll to zoom in or out.  
- **Standalone File Editing**  
  - Drag and drop a .tsc or .txt file into the script tabs to load it.  
- **Syntax Check**  
  - The editor will check for syntax errors when saving a file. It also checks for events that haven’t ended. End commands are editable.  
- **QoL**  
  - Many tiny new QoL features that you may or may not ever experience.

### Npc.tbl

- **Visual Preview**  
  - An accurate visual display based on the given sprite location numbers has been added. Hopefully now they are much less confusing.  
- **Sprite Location**  
  - The location of your sprite on the spritesheet.  
- **Search Bar**  
  - A search bar has been added.  
- **Copy and Paste**  
  - You can copy and paste NPC attributes from on to another.  
- **Warning Prompt**  
  - When saving or closing a warning will appear.

# Contributing
If you are planning to contribute a change, please open an issue in github's issue tracker so I can let you know
whether it fits into the project roadmap. All merge requests are subject to code review. Please at least approximately
follow standard Java naming and bracket conventions etc. and use tabs for indentation.

# Organization
The project's source files are separated into several packages, each grouping approximately similar functional
units of the editor.

- **ca.noxid.uiComponents**
  - A collection of Swing components extended to have slightly different behaviour or, more often than not, textured backgrounds.
- **ca.noxid.lab**
  - Contains the main class, `EditorApp`, and several other classes that haven't been given a more specific category.
  - **ca.noxid.lab.entity**
    - Classes relating to the Entity view, including the npc.tbl editor
  - **ca.noxid.lab.gameinfo**
    - Classes that manipulate the Cave Story executable, and classes that contain information about the state
      of the game/project
  - **ca.noxid.lab.mapdata**
    - relating specifically to the metadata of each specific map (e.g. which tileset it uses, what its name is, etc)
  - **ca.noxid.lab.rsrc**
    - holds all static resources used by the program such as images and default files, as well as the resource manager class.
  - **ca.noxid.lab.script**
    - Anything to do with TSC (text scripts)
  - **ca.noxid.lab.tile**
    - Classes used by the Tile view, including the tileset pane and linemode behaviour.

# Compiling

Requires: Java 17, discord-rpc, Gson, Jackson Databind, Jackson Core, Jackson Annotations, SLF4J API Module, & SLF4J Simple Provider.

  ---

discord-rpc
- https://github.com/firstdarkdev/discord-rpc
- https://mvnrepository.com/artifact/dev.firstdark.discordrpc/discord-rpc

Gson
- https://github.com/google/gson
- https://central.sonatype.com/artifact/com.google.code.gson/gson

Jackson Databind, Jackson Core, & Jackson Annotations
- https://github.com/FasterXML/jackson
- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations

SLF4J API Module, & SLF4J Simple Provider.
- https://mvnrepository.com/artifact/org.slf4j/slf4j-api
- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple


# License
This project is licensed under the Apache License, Version 2.0.
http://www.apache.org/licenses/LICENSE-2.0
