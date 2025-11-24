# HID Tools for Android

HID Tools is a powerful utility for Android that allows you to simulate Human Interface Devices (HID) like keyboards and mice over USB. It is designed for developers, security researchers, and enthusiasts for automation, testing, and educational purposes.

**Disclaimer:** This application is intended for legal and ethical use only. The developer is not responsible for any misuse of this application or any damage it may cause.

## Features

- **Live Keyboard & Mouse:** Control a connected computer with an on-screen keyboard and touchpad.
- **Scripting Engine:** Record, save, and run complex sequences of keyboard and mouse actions.
- **Combined Mode:** A compact UI that combines keyboard and mouse controls for quick access.
- **Simulation Mode:** Test and debug scripts without root access. All actions are logged to Logcat instead of being executed.
- **Settings:** Customize mouse sensitivity, action throttling, and other preferences.

## Build Instructions

1.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url>
    ```
2.  **Open in Android Studio:**
    - Open Android Studio.
    - Click on `File > Open` and select the cloned project directory.
3.  **Sync Gradle:**
    - Let Android Studio index the files and sync the project with the Gradle configuration.
4.  **Build the APK:**
    - Click on `Build > Build Bundle(s) / APK(s) > Build APK(s)`.

## Requirements & Running the App

### Core Dependencies
- **libsu:** This app uses `com.github.topjohnwu.libsu` to perform actions with root privileges.
- **Jetpack Compose:** The UI is built entirely with modern Android's declarative UI toolkit.

### How to Run

This application **cannot** be fully used on a standard Android emulator. The core functionality relies on low-level kernel modules (`hidg`) that are not present in emulator environments.

You must run this app on a **physical, rooted Android device** that has the necessary HID gadget kernel modules enabled. The most common way to achieve this is by using a device with the [NetHunter](https://www.kali.org/docs/nethunter/) platform.

### Enabling the NetHunter USB Arsenal HID Gadget

Before using the app, you must enable the HID gadget interface. The app checks for the existence of `/dev/hidg0` (keyboard) and `/dev/hidg1` (mouse) on startup.

1.  Open the **NetHunter App** on your device.
2.  Navigate to the **USB Arsenal** section.
3.  Enable the **HID Gadget** option.

This will create the necessary device nodes that HID Tools uses to send keyboard and mouse reports.

## Security: Root Requirement & Safety Checks

This application requires **root access** to write to the `/dev/hidg*` device files. This is a significant security consideration, and the app is designed with safety in mind.

- **Explicit Root Checks:** The app checks for root access before attempting any write operations. As seen in `hid/HidWriter.kt`, the `sendReport` function will not proceed if root is not available:

    ```kotlin
    if (forceSimulateMode || !Su.isroot()) {
        // ... log to Logcat and return
        return@withContext
    }
    ```

- **Device File Verification:** The app also checks if the HID gadget files exist before writing. This prevents errors on devices that don't have the HID gadget enabled.

- **User-Controlled:** No HID actions are performed without explicit user input (e.g., tapping a key, running a script).

- **Script Safety:** The app includes a pre-run analysis for scripts, warning you if a script contains long delays or other potentially problematic commands.

## CHANGELOG Template

A `CHANGELOG.md` file is essential for tracking changes across different versions of your app. Here is a template you can use:

```markdown
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Added
- 

### Changed
- 

### Deprecated
- 

### Removed
- 

### Fixed
- 

### Security
- 

---

## [1.0.0] - YYYY-MM-DD

### Added
- Initial release of HID Tools.
- Keyboard, Mouse, and Combo screens.
- Script recording and execution engine.
- Settings screen with preferences and security warnings.
- Simulation mode for non-root development.
```

### Sample Release Notes for v1.0.0

**Welcome to HID Tools v1.0!**

This is the initial release of HID Tools, a powerful utility for simulating keyboard and mouse input over USB. This version includes:

- **Live HID Control:** Use your phone as a portable keyboard and touchpad.
- **Powerful Scripting:** Record and play back complex sequences of actions for automation and testing.
- **Safe & Secure:** Built with safety in mind, with explicit root checks and a simulation mode for non-root users.

We hope you find this tool useful. Please report any bugs or feature requests on our GitHub page.
