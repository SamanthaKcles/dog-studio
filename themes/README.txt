Themes are relatively simple and easy to create.


Image files are self-explanatory.


There are three types of sounds. These will play is sounds are enabled.
.Wav files MUST be   { Signed 16-bit PCM 44100 Hz (stereo) }

startup.wav - plays when opening the editor.
startup_loop.wav - plays when opening the editor, indefinite.
startup.mid - plays when opening the editor.


You must add the folder name of your theme to the 'themeid.txt'.
The order of the names = the order they are displayed.


To change hardcoded colors, edit 'themesinfo.txt'. It also includes
Name, Author, Description, and Theme. Here is a template.



name=[name]
author=[author]
description=[desc]
theme=[dark/light]

// Colors (Hex Codes)
panel-color=
splash-color=
map-outline=
border-color=
tsc-tag-color=
tsc-tag-background=

// Unused in light theme
background=
background-secondary=
background-input=
text-color=
text-color-bright=
selection-color=

// Optional (Some unaffected in light theme)
button-color=
text-color-inactive=
scroll-bar=
scroll-bg=
script-bg=
panel-outline=
divider-col=



If you cannot open the editor because the theme you had selected was
deleted, copy an existing theme folder and rename it to the missing one.



		Happy theming!


