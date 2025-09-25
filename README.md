# Android Multi-Module MVVM Project

This is a sample Android app demonstrating MVVM with Clean Architecture using multi-module setup. It includes Retrofit for networking, Hilt for DI, Coroutines & Flow for async tasks, and Jetpack Compose for UI. Perfect for developers learning scalable Android app development.**.

### Ideal for **developers learning Android best practices** or preparing for interviews with a production-like sample app.

## Features
- **Multi-Module Setup** – separation of concerns for scalability
- **MVVM + Clean Architecture** – clear boundaries between layers
- **Retrofit** – networking layer with API calls
- **Hilt** – dependency injection for easy testing and scalability
- **Room DB** - offline fallback
- **Coroutines & Flow** – asynchronous programming made simple
- **Jetpack Compose** – modern UI toolkit for declarative UIs
- **Testable Architecture** – repositories & use cases decoupled from UI
- **CI/CD Ready** – GitHub Actions workflow included


## Tech Stack

- [Kotlin](https://kotlinlang.org/) – primary language
- [Jetpack Compose](https://developer.android.com/jetpack/compose) – UI
- [Hilt](https://dagger.dev/hilt/) – dependency injection
- [Retrofit](https://square.github.io/retrofit/) – API communication
- [Coroutines + Flow](https://kotlinlang.org/docs/coroutines-overview.html) – async programming
- [Clean Architecture](https://developer.android.com/jetpack/guide) – modular architecture approach


## Architecture Diagram
![Architecture Diagram](https://github.com/user-attachments/assets/70a99b36-79d5-4541-b1e6-a42dda15628f)

## Architecture OverView
┌──────────────┐
│   Presentation│ (Compose UI, ViewModels)
└───────▲──────┘
│
┌───────┴──────┐
│    Domain     │ (UseCases, Business Logic, Interfaces)
└───────▲──────┘
│
┌───────┴──────┐
│     Data      │ (RepositoriesImpl, API, DB, DTOs)
└──────────────┘


### Prerequisites
- Android Studio **Ladybug | 2024.2.1** or later
- JDK 17
- Gradle 8+


## Setup Instructions
1. Clone project git clone https://github.com/sudesh095/sudesh-learning
2. Replace API key in `core/Constants.kt`
3. Run with Android Studio
4. Navigate between News list and News Detail screen

## Run tests
```bash
./gradlew test
./gradlew connectedAndroidTest
