# Misery's Magica
A Cave Story level editor built off of Sam & K’s [Dog Studio](https://github.com/SamanthaKcles/dog-studio).

# Purpose  
I wanted Dog Studio to truly be a, "everything editor"... So I made this.

# Features  
Here is a list of the new features in this editor:

### General

- **Discord RPC**  
  - Showcase your love for this Editor!!
- **Cave Story+ 2026 Support**  
  - Now you can finally use Misery's Magica instead of the [Booster's Lab Fork](https://github.com/Clownacy/boosters-lab-csplus-2026).
- **Other Engines too!!**
  - doukutsu-rs, Cave Story+ 2011/2012, nxengine-evo / nxengine, & Cave Story ~ Encore (*Partial Support*).  These require loading a `stage.tbl`!
- **Auto-save**  
  - With choices between off, 2 minutes, 5 minutes, & 15 minutes...
- **Animating GIF Support for Themes**
  - Nice!
- **Crash Handler**
  - Now it just pops up the error. Crash logs are stored in "`editor/crash/(timestamp).log`"
- **`entitylist.txt` now stored in your mod(s)**
  - Go to "`yourmod/data/.dogstudio/entitylist.txt`".
- **Misc Changes**  
  - `musiclist.txt` includes CS+26 and Legacy (2011/2012) tracks.
  - `endlist.txt` now has <POP, & <PSH.
  - Return of the `tsc_list_tsc+.txt`!
  - <CML is now labeled that is it for the Freeware layers hack.
  - All CS+26/Legacy TSCs are labeled as such: CS+26=2026 | CS+=both
  - Dragon Sisters are now just Sisters...
  - Nikumaru Counter is 290 Counter
  - White has been renamed to "White Stonewall". Final Cave > "Last Cave", & On to Grasstown > "Onto Bushlands".

### Script

- **Read unencrypted TSCs**  
  - Super, super cool, right?

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
