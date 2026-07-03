# SecureVault

<p align="center">
  <img width="200" height="200" alt="Blue" src="https://github.com/user-attachments/assets/93619160-2d6c-4b7c-94cd-e6e2efa34f30" />
</p>
<p align="center"><strong>
A private, encrypted space to store sensitive documents locally on Android, protected by your biometrics.
</strong></p>

<p align="center">
    <a href="https://github.com/YounesBouhouche/SecureVault/releases/latest">
        <img src="https://img.shields.io/github/v/release/YounesBouhouche/SecureVault?include_prereleases&logo=github&style=for-the-badge&label=Latest%20Release" alt="Latest Release">
    </a>
    <img src="https://img.shields.io/badge/Android-11%2B-green?style=for-the-badge&logo=android" alt="Android 8+">
    <img src="https://img.shields.io/badge/Kotlin-100%25-purple?style=for-the-badge&logo=kotlin" alt="Kotlin">
</p>

## Screenshots

| Onboarding      | PIN Screen | Home Screen | File Viewer |
|-----------------|------------|-----------|-------------|
| <img width="200" alt="Screenshot_20260703_151036_SecureVault" src="https://github.com/user-attachments/assets/6089fbb6-b1a2-43f1-a8b7-6b74e997f7a6" /> | <img width="200" alt="Screenshot_20260703_151637_SecureVault" src="https://github.com/user-attachments/assets/1b4fec4c-dc0e-4f15-b4c3-50313242548a" /> | <img width="200" alt="Screenshot_20260703_151151_SecureVault" src="https://github.com/user-attachments/assets/698523ec-be40-4a5e-8a4c-be2e41c99cdc" /> | <img width="200" alt="Screenshot_20260703_151157_SecureVault" src="https://github.com/user-attachments/assets/69b89092-7c29-41e2-b444-b534098ee1da" /> |

---

## Features

- **Biometric + PIN authentication** with a fallback mechanism
- **AES-256 file encryption** on import — originals are deleted immediately
- ️**SQLCipher-encrypted database** for file metadata
- **Android Keystore** for hardware-isolated key storage
- **In-app file viewer** — decrypted on demand, never written back to plain storage
-  **File organization** — folders, tags, and favorites
- **Export as ZIP**
- **Material 3 Expressive** with dynamic theming
- **Auto-lockdown** after 5 failed PIN attempts (60-second cooldown)

---

## Security Design Decisions

### 1. Keys never touch the app
All encryption keys are generated and stored inside **Android Keystore**, a hardware-isolated environment (TEE). The app never holds raw key material, it only holds aliases.

### 2. Defense in depth on authentication
The PIN is **hashed before being stored**, even though the database is already encrypted with SQLCipher. If a vulnerability ever exposed the database in RAM, raw PINs still wouldn't be recoverable. One lock is never enough.

### 3. On-demand decryption only
The app **never decrypts a file** until the user explicitly requests it. Once the file is no longer in use, the decrypted data is cleared from memory immediately.

### 4. Encrypted preferences too
App preferences are stored in a **DataStore with a custom serializer** that handles encryption and decryption.

### 5. Performance without compromise
Encryption, decryption, hashing, and disk I/O are CPU-heavy operations. All of these run on background threads using **Kotlin Coroutines** to prevent blocking UI thread.

---

## Architecture

SecureVault is built with **Clean Architecture + MVVM + Repository Pattern**, organized by feature rather than by layer.

```
app/
├── core/               # Shared utilities, crypto engine, base classes
│    ├── data/
│    ├── domain/
│    ├── presentation/
│    └── util/
├── di/                 # Koin dependency injection modules
└── features/
    ├── navigation/     # Navigation graph and routes
    ├── auth/           # Biometric + PIN setup and verification
    │   ├── data/
    │   ├── domain/
    │   └── presentation/
    ├── main/          # File list and file-viewer
    │   ├── data/
    │   ├── domain/
    │   └── presentation/
    └── export/         # In-app file viewer
        ├── domain/
        └── presentation/
```

The `core` module contains the `Crypto` utility class — the single source of truth for all encryption and decryption operations across the app, using AES-256 with IV prepended to the cipher output.

---

## Tech Stack

| Layer         | Technology                                     |
|---------------|------------------------------------------------|
| Language      | Kotlin                                         |
| UI            | Jetpack Compose                                |
| Architecture  | Clean Architecture · MVVM · Repository Pattern |
| DI            | Koin                                           |
| Database      | Room + SQLCipher                               |
| Preferences   | DataStore (custom encryption serializer)       |
| Security      | Android Keystore · AES-256 Cipher · BiometricPrompt   |
| File Access   | Storage Access Framework (SAF)                 |
| Image Loading | Coil                                           |
| Async         | Kotlin Coroutines + Flow                       |
| Navigation    | Navigation 2                                   |

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Min SDK 26 (Android 8.0)

### Setup

1. Clone the repository

```bash
git clone https://github.com/YounesBouhouche/SecureVault.git
```

2. Open the project in Android Studio

3. Create a `local.properties` file in the root directory (if it doesn't exist) and add the following keys:
```properties
KEY_ALIAS=your_key_alias_here
DB_KEY_ALIAS=your_db_key_alias_here
```

> These values are imported as `BuildConfig` fields and used to reference keys stored in Android Keystore. Never commit `local.properties` to version control.

4. Build and run the project, or download the latest APK from the [Releases](https://github.com/YounesBouhouche/SecureVault/releases) page.

---

## Download

[![Download APK](https://img.shields.io/badge/Download-APK-blue?style=for-the-badge&logo=android)](https://github.com/YounesBouhouche/SecureVault/releases)

---

## Roadmap

- [ ] Wider file type support in the in-app viewer
- [ ] Trash / recycle bin feature
- [ ] Configurable lockdown attempts and cooldown duration (currently hardcoded constants)
- [ ] Unit and instrumentation tests
- [ ] Codebase refactoring and code quality improvements

---

## Contributing

This project is in beta and the codebase is still evolving. Feedback, feature suggestions, and contributions are all welcome, open an issue or submit a pull request.

---
