# HID Tools - QA Bug Checklist

This checklist is designed for quick, focused testing to catch common bugs and regressions.

## Common Failure Modes

### App Launch & State
- [ ] **Stuck on Splash:** App does not proceed past the splash screen. (Likely cause: HID devices not found, but no error is shown).
- [ ] **Incorrect Status Icons:** The keyboard, mouse, or root icons in the main menu do not reflect the actual device status.
- [ ] **State not Persisting:** Modifier keys (Shift, Ctrl) or mode toggles (Live/Script) do not remain in their selected state when navigating between screens.

### HID Functionality
- [ ] **Keystrokes Not Registering:** Typing on the keyboard screen has no effect on the host.
- [ ] **Incorrect Character Mapping:** Pressing a key sends the wrong character to the host (e.g., `a` produces `q`).
- [ ] **Mouse Movement Inverted:** Moving the mouse up moves the cursor down, etc.
- [ ] **Sticky Modifier Keys:** A modifier key (Shift, Ctrl) remains active even after being toggled off.

### Scripting
- [ ] **Script Not Saving:** The app fails to save a recorded script, or the saved file is empty.
- [ ] **Script Not Running:** Tapping "Run" does nothing.
- [ ] **Incorrect Action Playback:** The script runs but performs the wrong actions (e.g., types the wrong text, moves the mouse in the wrong direction).
- [ ] **App Freeze on Run:** The app becomes unresponsive when running a script, especially one with long delays.

### UI & User Experience
- [ ] **Missing Haptic Feedback:** No vibration on key presses.
- [ ] **UI Elements Overlapping:** Text or buttons overlap, especially on smaller screens or with larger font sizes.
- [ ] **Missing Content Descriptions:** Using a screen reader (TalkBack) reveals that icons or buttons are unlabeled.
- [ ] **No Overwrite Warning:** The app overwrites an existing script without asking for confirmation.
