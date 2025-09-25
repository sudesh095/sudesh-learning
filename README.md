# NewsPlayerApp

A multi-module Android project using **Clean Architecture, MVVM, Hilt, Compose, Flow**, with **News API + Room caching** and an **ExoPlayer screen**.

## Features
- Clean Architecture (domain/data/app/core modules)
- Hilt for DI
- NewsAPI integration (replace API key)
- Room DB offline fallback
- Flow with debounce + retryWhen
- Resource/UiState for error handling
- Coil for image loading
- ExoPlayer with ABR + retry + low internet warning
- Unit & UI tests
- CI-ready (GitHub Actions workflow included)

## Setup
1. Clone project
2. Replace API key in `core/Constants.kt`
3. Run with Android Studio
4. Navigate between News list and Player screen

## Run tests
```bash
./gradlew test
./gradlew connectedAndroidTest
