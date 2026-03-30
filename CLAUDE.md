# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Tests
./gradlew test                          # All unit tests
./gradlew :<module>:test               # Single module, e.g. :transaction:data:impl:test

# Lint & code quality
./gradlew detekt                        # Static analysis
./gradlew dependencyAnalysis            # Check for unused dependencies
./gradlew sortDependencies              # Sort dependency declarations

# Full build (includes tests and linting)
./gradlew build
```

Build configuration: compile SDK 36, min SDK 23, Java 17, Kotlin 2.3.20.

## Architecture

**Clean Architecture + Multi-Module**. Each feature is split into submodules:

- `:feature:ui` — Jetpack Compose screens
- `:feature:domain:contract` / `:impl` — Use cases and business logic
- `:feature:data:contract` / `:impl` — Repository interfaces and implementations
- `:feature:domain:model` — Domain models
- `:feature:di` — Hilt modules wiring everything together

**Core modules:**
- `:core:database` — Room database
- `:core:datastore` — DataStore preferences
- `:core:designsystem` — Material 3 theme and shared UI components
- `:core:common` — Kotlin-only utilities (no Android deps)
- `:core:common-android` — Android-specific utilities
- `:core:network` — Retrofit/OkHttp setup

**Feature modules:** `analytics`, `backup`, `billing`, `budget`, `category`, `currency`, `transaction`, `transaction:regular`, `user-info`, `profile`, `settings`, `statistics`, `incomeexpense`

**Data flow:** UI → ViewModel → UseCase (domain) → Repository (data) → Room/Firebase/Network. All async via Kotlin Coroutines + Flow.

## Key Patterns

**Dependency Injection:** Hilt throughout. Feature DI wiring lives in each `:feature:di` module; `AppModule` is in `:app`.

**Navigation:** Jetpack Navigation Compose with nested nav graphs. Routes are defined as constants per feature. `MainActivity` hosts the `NavHost`.

**Convention Plugins** in `buildSrc/` define reusable Gradle config:
- `AndroidLibraryConventionPlugin` — standard library setup
- `AndroidHiltConventionPlugin` — adds Hilt/KSP
- `AndroidLibraryComposeConventionPlugin` — adds Compose
- `KotlinLibraryConventionPlugin` — pure Kotlin modules (no Android)

All library versions are centralized in `gradle/libs.versions.toml`.

**Testing:** JUnit 4 + MockK + `kotlinx.coroutines.test`. Unit tests mock data sources with `mockk { }` / `coEvery` / `coVerify`. Test reports at `<module>/build/reports/tests/`.

**Financial values** use `BigDecimal` throughout — do not use `Double` or `Float`.

## Firebase

App uses Firebase Auth (Google + Phone sign-in via FirebaseUI), Analytics, Crashlytics, Remote Config, and Cloud Storage (backups). Auth state is observed in `MainActivity`/`MainViewModel`.

## Release Signing

Release builds require `keystore.properties` at the project root (not in repo). `app/google-services.json` holds Firebase config — the repo version is a placeholder for debug; production values are applied separately.
