# PokemonApp

A Kotlin Multiplatform Pokémon browser that fetches data from [PokeAPI](https://pokeapi.co/). Browse paginated Pokémon, view details, build a team, and customize light/dark theme settings.

## Features

- Paginated Pokémon list with pull-to-refresh
- Pokémon detail view (stats, types, moves)
- Team builder with local persistence
- Light, dark, and system theme modes

## Architecture

The project follows clean architecture with shared business logic in Kotlin:

| Module | Role |
|--------|------|
| `sharedLogic` | Domain models, repositories, ViewModels, Room database, Ktor networking |
| `sharedUI` | Compose Multiplatform UI (Android) |
| `androidApp` | Android entry point |
| `iosApp` | SwiftUI entry point using the `SharedLogic` framework |

**Stack:** Kotlin Multiplatform, Ktor, Koin, Room, Coil, Compose Multiplatform (Android), SwiftUI (iOS)

## Getting started

**Requirements:** JDK 11+, Android Studio (or IntelliJ), Xcode (for iOS)

### Android

```bash
./gradlew :androidApp:assembleDebug
```

Or run the `androidApp` configuration from your IDE.

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and run on a simulator or device.

## Tests

```bash
./gradlew :sharedUI:testAndroidHostTest :sharedLogic:testAndroidHostTest
./gradlew :sharedLogic:iosSimulatorArm64Test
```
