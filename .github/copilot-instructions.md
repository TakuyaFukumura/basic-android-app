# Basic Android App
Modern Android application built with Kotlin and Jetpack Compose. This is a basic Android development template repository providing a foundation for Android app development.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Prerequisites and Setup
- Java 17 is required (available in this environment)
- Android SDK is pre-installed at `/usr/local/lib/android/sdk`
- Android platform-tools and build-tools are available
- Gradle wrapper is included in the project

### Bootstrap and Build Commands
CRITICAL: Set appropriate timeouts for all Android build commands. Android builds can take significant time, especially on first run.

**Initial Setup:**
```bash
# Make gradlew executable (if needed)
chmod +x gradlew

# Add Android tools to PATH
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
```

**Build Commands:**
```bash
# Clean build (recommended first step)
./gradlew clean
# Build debug APK - NEVER CANCEL: Can take 15-45 minutes on first build. Set timeout to 60+ minutes.
./gradlew assembleDebug
# Build release APK - NEVER CANCEL: Can take 20-60 minutes. Set timeout to 90+ minutes.
./gradlew assembleRelease
```

**Test Commands:**
```bash
# Run unit tests - NEVER CANCEL: Takes 5-15 minutes. Set timeout to 30+ minutes.
./gradlew test
# Run lint checks - NEVER CANCEL: Takes 10-30 minutes. Set timeout to 45+ minutes.
./gradlew lint
# Check all code quality tools - NEVER CANCEL: Takes 15-45 minutes. Set timeout to 60+ minutes.
./gradlew check
```

**Installation and Running:**
```bash
# Install debug APK to connected device/emulator
./gradlew installDebug
# Uninstall app
adb uninstall com.example.myapplication
```

### Known Issues and Limitations
**IMPORTANT: Network Access Required**
- The Android Gradle Plugin version (8.12.1) in `gradle/libs.versions.toml` may not be available in environments with restricted network access
- If build fails with "Plugin was not found" errors, this indicates network connectivity issues with Google's Maven repository
- In such cases, consider updating to a more stable AGP version (e.g., 8.1.0 or 7.4.2) in `gradle/libs.versions.toml`

**Common Build Issues:**
- First builds may fail due to missing SDK components - rerun the build command
- Gradle daemon issues: run `./gradlew --stop` then retry
- Network timeouts: increase timeout values in `gradle.properties`
- Memory issues: ensure sufficient heap space (already configured to 2048m)

**Build Validation Status:**
- ⚠️  Build commands could not be fully validated due to network restrictions in this environment
- ✅  Project structure and configuration verified
- ✅  Android SDK environment confirmed available (Android API 36 target)
- ✅  Test files and source code verified
- ✅  Dependencies and versions analyzed

## Validation and Testing

### Manual Validation Requirements
After making any changes to the application:

1. **Always build the project** to ensure code compiles:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Run unit tests** to verify functionality:
   ```bash
   ./gradlew test
   ```

3. **Run instrumented tests** (requires emulator or device):
   ```bash
   ./gradlew connectedAndroidTest
   ```

4. **Test user scenarios manually:**
   - Install the app on device/emulator
   - Launch the app and verify it displays "Hello Android!" text
   - Test basic navigation and UI interactions
   - Verify app doesn't crash during basic usage

### Lint and Code Quality
Always run these before committing changes:
```bash
# Run Android lint
./gradlew lint
# Run all checks (lint + tests)
./gradlew check
```

## Project Structure and Navigation

### Key Directories and Files
```
basic-android-app/
├── app/                           # Main application module
│   ├── src/main/
│   │   ├── java/com/example/myapplication/
│   │   │   ├── MainActivity.kt    # Main activity with Compose UI
│   │   │   └── ui/theme/         # Theme configuration
│   │   ├── res/                  # Android resources
│   │   └── AndroidManifest.xml   # App manifest
│   ├── src/test/                 # Unit tests
│   ├── src/androidTest/          # Instrumented tests
│   ├── build.gradle.kts          # App-level build configuration
│   └── proguard-rules.pro        # ProGuard configuration
├── gradle/
│   └── libs.versions.toml        # Dependency version catalog
├── build.gradle.kts              # Project-level build file
├── settings.gradle.kts           # Gradle settings
└── gradlew / gradlew.bat         # Gradle wrapper scripts
```

### Important Files to Monitor
When making changes, always check these files for impact:
- `app/src/main/java/com/example/myapplication/MainActivity.kt` - Main app logic
- `app/build.gradle.kts` - Dependencies and build configuration
- `gradle/libs.versions.toml` - Version management
- `app/src/main/AndroidManifest.xml` - App permissions and configuration

### Common Development Tasks

**Adding Dependencies:**
1. Add to `gradle/libs.versions.toml` in `[libraries]` section
2. Reference in `app/build.gradle.kts` dependencies block
3. Sync and build project

**Creating New Activities/Screens:**
1. Create new Kotlin file in `app/src/main/java/com/example/myapplication/`
2. Add to `AndroidManifest.xml` if it's an Activity
3. Update navigation or main activity as needed

**Modifying UI:**
1. Edit Compose functions in MainActivity.kt or create new composables
2. Update theme files in `ui/theme/` if needed
3. Test on device/emulator immediately

## Technology Stack and Current Configuration
- **Language:** Kotlin 2.0.21
- **UI Framework:** Jetpack Compose with BOM 2024.09.00
- **Build System:** Gradle 8.13 with Kotlin DSL
- **Android Gradle Plugin:** 8.12.1 (may need adjustment for network-restricted environments)
- **Target SDK:** 36 (Android API level 36)
- **Min SDK:** 24 (Android 7.0)
- **Java Compatibility:** Java 17 (available in environment)
- **Package Name:** com.example.myapplication
- **Version:** 0.1.0 (versionCode 1)

### Current Dependencies (from libs.versions.toml)
- androidx-core-ktx: 1.10.1
- androidx-lifecycle-runtime-ktx: 2.6.1
- androidx-activity-compose: 1.8.0
- androidx-compose-bom: 2024.09.00
- JUnit: 4.13.2
- androidx-junit: 1.1.5
- androidx-espresso-core: 3.5.1

## Development Workflow
1. Always pull latest changes before starting work
2. Make incremental changes and test frequently
3. Run unit tests after each significant change
4. Build and install on device/emulator to test UI changes
5. Run full test suite before committing
6. Always run lint checks before pushing changes

**Remember: Android builds can be time-consuming. Never cancel long-running build operations - they may take 45+ minutes on first run or clean builds.**

## Quick Reference - Repository Contents

### Root Directory
```
.github/               # GitHub configuration and templates
├── ISSUE_TEMPLATE/   # Issue templates
└── copilot-instructions.md  # This file
app/                  # Main Android application module
gradle/              # Gradle version catalog and wrapper
├── libs.versions.toml  # Centralized dependency versions
└── wrapper/         # Gradle wrapper files
build.gradle.kts     # Project-level build configuration
settings.gradle.kts  # Gradle settings and module inclusion
gradle.properties    # Gradle JVM and project settings
gradlew             # Gradle wrapper script (Unix)
gradlew.bat         # Gradle wrapper script (Windows)
README.md           # Project documentation
.gitignore          # Git ignore patterns
```

### Application Module (app/)
```
src/main/
├── java/com/example/myapplication/
│   ├── MainActivity.kt           # Main activity with Compose UI
│   └── ui/theme/                # UI theme configuration
│       ├── Color.kt             # App color definitions
│       ├── Theme.kt             # Material3 theme setup
│       └── Type.kt              # Typography definitions
├── res/                         # Android resources
│   ├── values/                  # String and style resources
│   ├── mipmap-*/               # App icons (various densities)
│   └── xml/                    # Backup and data extraction rules
└── AndroidManifest.xml         # App manifest and permissions

src/test/                       # Unit tests (JVM)
└── java/com/example/myapplication/
    └── ExampleUnitTest.kt      # Sample unit test

src/androidTest/               # Instrumented tests (Android device)
└── java/com/example/myapplication/
    └── ExampleInstrumentedTest.kt  # Sample instrumented test
```