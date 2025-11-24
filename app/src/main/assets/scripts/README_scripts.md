# HID Tools Scripting Language

This document outlines the syntax for creating HID scripts for the HID Tools app.

## Commands

### Keyboard

*   `KEY <CHAR>`: Sends a single key press for the given character. This includes a key down and a key up event.  
    *Example:* `KEY A`

*   `KEYDOWN <MODIFIER>`: Holds down a modifier key (SHIFT, CTRL, ALT, META).
    *Example:* `KEYDOWN SHIFT`

*   `KEYUP <MODIFIER>`: Releases a modifier key.
    *Example:* `KEYUP SHIFT`

*   `TYPE <TEXT>`: Types out a string of text.  
    *Example:* `TYPE Hello, world!`

### Mouse

*   `MOUSE MOVE <dx> <dy>`: Moves the mouse cursor by the given deltas.
    *Example:* `MOUSE MOVE 10 -5`

*   `MOUSE CLICK <BUTTON>`: Sends a mouse click for the given button (LEFT, RIGHT, MIDDLE).
    *Example:* `MOUSE CLICK LEFT`

### Other

*   `DELAY <milliseconds>`: Pauses the script for the given number of milliseconds.
    *Example:* `DELAY 500`

*   `// <COMMENT>`: A comment line, which is ignored by the script runner.
    *Example:* `// This is a comment`
