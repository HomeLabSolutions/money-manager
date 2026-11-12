# Metro dependency injection

The app uses Metro 1.5.2 with native Metro annotations. The shared
`moneymanager.di` convention plugin applies Metro to modules with dependency
injection, after their Kotlin or Android base plugin. Modules without DI do not
receive the compiler plugin or its runtime dependency.
Hilt/Dagger plugins, processors, runtime dependencies, and annotations are removed.
Room retains its KSP processor.

## Application graph and lifetimes

`AppGraph` is a `@DependencyGraph(AppScope::class)` created by the application's
lazy `appGraph` property. `@ContributesTo` interfaces contribute bindings directly
to the graph; object provider modules use `@BindingContainer`. Constructor
injection uses `@Inject`. Existing application singletons use
`@SingleIn(AppScope::class)`; unscoped bindings remain unscoped. `kotlin.Lazy`
replaces the previous Dagger lazy dependency.

`MainViewModel` requests backup, geocoding, location, and currency-update services
through `Lazy`. Their dependency chains are resolved when the corresponding
restore or user action first uses them, rather than when the ViewModel is created.
Category creation already follows the same pattern.

`BudgetInteractorImpl` is shared within AppScope. Other unscoped feature bindings
retain their previous lifetimes. Contributing a binding to AppScope does not make
it a singleton; `@SingleIn` determines whether the graph caches its instance.

The network module retains the shared Json instance, Android HTTP engine, and
three qualified singleton Ktor clients. The three base URL constants remain in
that module. Coil keeps its Application image loader factory and OkHttp fetcher.

The application declares the implementation dependencies needed by graph code
generation. `metro.hiddenDependencies.checkOnCompile` is enabled for the app;
checks fail if contributions are hidden behind implementation boundaries.
Project API dependencies remain forbidden by `noApiCheck`.

## ViewModels

MetroX supplies `MetroViewModelFactory` and `metroViewModel()`. The Activity sets
its default factory and provides `LocalMetroViewModelFactory` to Compose.
ViewModelStore ownership and navigation back stack entries continue to determine
ViewModel lifetimes.

ViewModels without runtime arguments contribute provider map entries. ViewModels
with navigation arguments contribute assisted factories; each creation obtains
its SavedStateHandle from the current CreationExtras using
`createSavedStateHandle()`. No shared application SavedStateHandle is registered.

## Android framework entry points

The app supports API 24, so it does not depend on MetroX Android's API 28
AppComponentFactory integration. The Activity obtains its factory from AppGraph.
The Application implements the typed widget entry point, which delegates to the
graph when Glance requests its interactor.

WorkManager obtains the custom MetroWorkerFactory from the Application's
Configuration.Provider. The default WorkManager startup initializer is removed
so the configured factory is used. Worker assisted factories are contributed to a
map keyed by worker KClass. Each factory receives Context and WorkerParameters at
creation time. Unknown workers return null to retain WorkManager's fallback.

DelegatingWorker and its persisted `RouterWorkerDelegateClassName2` key are
unchanged. Existing work names and schedules are preserved. WorkManager's consumer
R8 rules retain ListenableWorker subclass names and constructors.

## Validation

```sh
./gradlew :app:assembleDebug test ktlintCheck detektCheck tomlCheck noApiCheck checkSortDependencies
./gradlew projectHealth lint
```

Root graph tests verify singleton factories, lazy registration, assisted
navigation factories, and unknown-worker fallback. Worker factory tests verify
lazy child creation and forwarding runtime arguments. Removing WidgetDomainModule
from the root graph must fail compilation for the missing widget bindings.

Local release analysis needs a temporary Firebase test configuration with the
release package name; do not commit it or upload Crashlytics mapping files from
that configuration. CI supplies the production configuration.
