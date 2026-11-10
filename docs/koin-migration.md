# Koin dependency injection

The Android app uses Koin 4.2.2 with Koin Compiler Plugin 1.2.1. The application
starts Koin before initializing WorkManager or scheduling background work.
`MoneyManagerModule.kt` explicitly includes the feature and infrastructure modules.
The compiler plugin is applied to modules that declare definitions or resolve them;
`compileSafety` and `strictSafety` are enabled in the app.

Constructor declarations use the compiler DSL (`factory<T>()`, `single<T>()`,
`viewModel<T>()`, `worker<T>()`). Functions that create platform objects use
`create(::provider)`. Existing application singletons and factories retain their
lifetimes. ViewModels are obtained through the current ViewModelStoreOwner using
`koinViewModel()` or the Android Activity `viewModel()` delegate.

Keep constructor declarations in the same Gradle module as constructors with
`@Named` parameters. With plugin 1.2.1, wiring a constructor from another module
can omit its parameter qualifier at runtime. Backup, transaction storage and the
widget expose local constructor modules, included by their feature DI modules.
The widget Koin test resolves the production module and exercises its dispatcher.

`DelegatingWorker` and its persisted input key remain available for previously
scheduled work. Its delegate is created by KoinWorkerFactory. New worker definitions
use Koin's worker DSL. The default WorkManager initializer is removed from the
manifest, and `workManagerFactory()` initializes it after the graph is loaded.
The Glance widget resolves dependencies through KoinComponent at its framework entry point.

## Validation

```sh
./gradlew clean :app:assembleDebug test ktlintCheck detektCheck tomlCheck noApiCheck --no-build-cache
./gradlew projectHealth lint checkSortDependencies
```

The normal release checks need production google-services.json supplied by CI.
For local release analysis, use a temporary test Firebase configuration with the
release package name and omit Crashlytics mapping upload. Do not publish or run
release artifacts built with that test configuration.

Removing `widgetDomainModule` from the root graph must fail compilation with
`KOIN-D001` for SpendingWidgetStateObserver; restore it before building the app.

Plugin 1.2.1 verifies Kotlin versions through 2.4.20 and warns on this project's
2.4.21. Debug/release compilation, runtime checks and unit tests therefore remain
necessary. D8/R8 also warn about duplicate generated relay hint methods for qualified
definitions; those methods are compile-time metadata, and R8 removes them.

This migration is based on develop after the Coil 3 and Ktor migrations. Coil
retains its Application factory. Koin provides one shared Json instance, one
Android HTTP engine and three named singleton Ktor clients. API provider functions
use @Named parameters to select their clients. Android features remain in their
existing modules.
