# HID Tools - Integration Test Checklist

This checklist provides a step-by-step guide for testing the core functionality of the HID Tools application on a physical device.

## Test Environment Setup

- **Device:** A physical, rooted Android device.
- **Platform:** NetHunter or a similar platform with HID kernel modules.
- **Host:** A computer (Windows, macOS, Linux) to act as the USB host.

---

## Test Plan

### 1. Pre-flight: Enable HID Gadget
- [ ] On the test device, open the NetHunter app.
- [ ] Navigate to **USB Arsenal** and enable the **HID Gadget**.
- [ ] Connect the device to the host computer via USB.

### 2. Module: Splash Screen & Device Detection
- [ ] Launch the HID Tools app.
- [ ] **Expected Result:** The splash screen appears, shows a loading spinner, and then navigates to the Main Menu. This confirms that `/dev/hidg0` and `/dev/hidg1` were successfully detected.

### 3. Module: Keyboard (Live Mode)
- [ ] From the Main Menu, tap **Keyboard Only**.
- [ ] On the host computer, open a text editor.
- [ ] On the app, type a sequence of characters (e.g., "Hello World").
- [ ] Toggle the `Shift` key and type to test uppercase letters and symbols.
- [ ] **Expected Result:** All keystrokes appear correctly in the text editor on the host computer.

### 4. Module: Mouse (Live Mode)
- [ ] From the Main Menu, tap **Mouse Only**.
- [ ] On the app, drag your finger across the touchpad area.
- [ ] Tap the **Left Click** and **Right Click** buttons.
- [ ] **Expected Result:** The mouse cursor on the host computer moves smoothly and clicks correctly.

### 5. Module: Scripting (Record & Run)
- [ ] From the Main Menu, tap **Record Script**.
- [ ] Tap **Start Recording**.
- [ ] Perform a short sequence of actions (e.g., open the Keyboard screen, type a few keys, open the Mouse screen, move the mouse).
- [ ] Tap **Stop Recording**.
- [ ] Enter a filename (e.g., "test_script") and tap **Save Script**.
- [ ] Navigate to the **Run Script** screen (to be created).
- [ ] Select and run "test_script".
- [ ] **Expected Result:** The recorded actions are reproduced exactly on the host computer.

### 6. Verification: Simulation Mode (Non-Root)
- [ ] Install the app on a **non-rooted** Android device (or use the rooted device and enable "Force Simulate Mode" in Settings).
- [ ] Open the app. The splash screen should show an error about root access.
- [ ] Navigate to the Keyboard or Mouse screen.
- [ ] Open **Logcat** in Android Studio, filtering for the `HidWriter` tag.
- [ ] Perform actions (typing, moving the mouse).
- [ ] **Expected Result:** No actions occur on the host. Logcat prints human-readable logs for each action (e.g., `SIMULATE: writeKeyboard [...]`).

### 7. Automated Tests
- [ ] In Android Studio, right-click on the `app/src/test` directory and select **Run 'Tests in app'**.
- [ ] **Expected Result:** All unit tests pass.
- [ ] Right-click on the `app/src/androidTest` directory and select **Run 'Tests in app'**.
- [ ] **Expected Result:** All instrumentation tests pass.
