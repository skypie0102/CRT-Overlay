# CRT Overlay (Android)

This project implements a simple screen overlay that simulates CRT-like scanlines, chromatic offset, and vignette.
It includes a Canvas-based overlay and a GLSL shader mode (if implemented). The app requests the Draw-over-apps permission.

- Build: Use GitHub Actions included in `.github/workflows/android.yml` to automatically build a debug APK (app-debug.apk).
- To install the debug APK on a device: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
