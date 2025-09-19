# SmartCaravans Client - Android App

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)
![Min SDK](https://img.shields.io/badge/Min%20SDK-30-lightgrey.svg)

An Android application built with Jetpack Compose for managing insurance claims, constats (accident reports), and document sharing through NFC and QR code technologies.

## 📱 Features

- **Digital Accident Reports (Constats)**: Create and manage digital accident reports with photo uploads
- **NFC Sharing**: Share and receive data between devices using Near Field Communication
- **QR Code Scanning**: Quick data exchange via QR code scanning
- **Document Management**: Upload and manage insurance documents and photos
- **Multi-language Support**: French localization with proper translations
- **Material Design 3**: Modern UI following Material Design 3 guidelines
- **Camera Integration**: Take photos for accident documentation
- **Location Services**: Automatic location detection for accident reports
- **User Authentication**: Secure login and registration system

## 🏗️ Architecture

The app follows Clean Architecture principles with MVVM pattern:

```
app/
├── auth/                    # Authentication module
├── core/                    # Shared core functionality
│   ├── data/               # Data layer (repositories, data sources)
│   ├── domain/             # Domain layer (use cases, models)
│   └── presentation/       # Shared UI components
├── main/                   # Main application flow
│   ├── data/
│   ├── domain/
│   └── presentation/
└── di/                     # Dependency injection
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 21
- Android SDK 30+
- Device with NFC capability (optional, for NFC features)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/smartcaravans-client.git
   cd smartcaravans-client
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Open the project folder
   - Wait for Gradle sync to complete

3. **Build and Run**
   ```bash
   # Windows
   .\gradlew.bat assembleDebug
   .\gradlew.bat installDebug
   
   # Linux/Mac
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

## 📋 Tech Stack

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - UI design system
- **Coroutines** - Asynchronous programming
- **Flow** - Reactive streams

### Architecture & DI
- **MVVM Pattern** - Presentation layer architecture
- **Clean Architecture** - Multi-layer architecture
- **Koin** - Dependency injection framework

### Networking & Data
- **Ktor Client** - HTTP networking
- **Apollo GraphQL** - GraphQL client
- **Room Database** - Local data persistence
- **DataStore** - Preferences storage
- **Gson** - JSON serialization

### UI & Media
- **CameraX** - Camera functionality
- **Coil** - Image loading
- **ZXing** - QR code scanning
- **MapBox** - Maps integration

### Hardware Integration
- **NFC API** - Near Field Communication
- **Location Services** - GPS functionality
- **ML Kit** - Barcode scanning

## 🔧 Key Components

### NFC Implementation

The app includes native NFC read/write functionality without external libraries:

```kotlin
// Reading NFC data
fun readTextWithNfc() {
    // Arms the reader for the next NFC tag
    // Automatically processes NDEF text records
}

// Writing NFC data
fun shareTextWithNfc(text: String) {
    // Prepares text for writing to next NFC tag
    // Supports NDEF formatting if needed
}
```

**Files:**
- `MainActivity.kt` - NFC implementation
- `AndroidManifest.xml` - NFC permissions and intent filters
- `nfc_tech_filter.xml` - Supported NFC technologies

### DateField Component

Custom date picker component derived from MyTextField:

```kotlin
@Composable
fun DateField(
    value: LocalDateTime?,
    onValueChange: (LocalDateTime?) -> Unit,
    // ... other parameters
)
```

Features:
- Material 3 DatePickerDialog integration
- Proper keyboard handling
- Error state support
- French date formatting (dd/MM/yyyy)

### Authentication System

Secure authentication with:
- JWT token management
- Automatic token refresh
- Biometric authentication support
- Session persistence

## 🌍 Localization

The app is fully localized in French with:
- ✅ Complete UI translations
- ✅ Error messages in French
- ✅ Form validation messages
- ✅ Proper XML escaping for apostrophes

**Translation Coverage:**
- Authentication flows
- Main navigation
- Error handling
- Form validations
- NFC interactions
- Camera permissions

## 📱 Permissions

The app requires the following permissions:

```xml
<!-- Core functionality -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />

<!-- Location services -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- NFC functionality -->
<uses-permission android:name="android.permission.NFC" />

<!-- Storage (SDK 28-32 compatibility) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />

<!-- Notifications -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## 🔨 Build Configuration

```kotlin
android {
    compileSdk = 36
    
    defaultConfig {
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("21")
        languageVersion = KotlinVersion.fromVersion("2.2")
    }
}
```

## 🐛 Troubleshooting

### Common Issues

**NFC Not Working:**
- Ensure NFC is enabled in device settings
- Keep the app in foreground
- Position NFC tag near device's NFC antenna
- Check that the tag supports NDEF format

**Build Errors:**
- Clean project: `./gradlew clean`
- Invalidate caches in Android Studio
- Check Gradle JDK version (should be JDK 21)

**Resource Compilation Errors:**
- Verify no smart quotes in strings.xml (use straight quotes only)
- Check proper XML apostrophe escaping with `\'`

### Performance Tips

- Use `LazyColumn` for large lists
- Implement proper image caching with Coil
- Use `Flow.stateIn()` for UI state management
- Leverage Compose's `remember` for expensive operations

## 📚 Documentation

### Key Files
- `MainActivity.kt` - Main activity with NFC implementation
- `MyTextField.kt` - Base text field component
- `MyDateField.kt` - Date picker component
- `strings.xml` - French translations
- `AndroidManifest.xml` - App permissions and components

### Architecture Decisions
- **Reader Mode NFC**: Modern approach vs deprecated foreground dispatch
- **Compose Navigation**: Single-activity architecture
- **Koin DI**: Lightweight dependency injection
- **Clean Architecture**: Separation of concerns

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Write unit tests for business logic
- Document public APIs

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- [Material Design 3](https://m3.material.io/) - Design system
- [Koin](https://insert-koin.io/) - Dependency injection
- [Apollo GraphQL](https://www.apollographql.com/) - GraphQL client
- [CameraX](https://developer.android.com/camerax) - Camera functionality

---

**Note:** This app targets Android 11+ (API 30) devices. NFC functionality requires NFC-capable hardware but is not mandatory for basic app operation.
