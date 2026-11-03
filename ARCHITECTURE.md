# Kids English — Android Starter Architecture & Design System Guide

## 1. Project Architecture Overview

**Kids English** is built from the ground up as a modular, production-ready Android application using **Kotlin**, **Jetpack Compose**, **Material 3**, **Single Activity Architecture**, **Clean Architecture**, **MVVM**, **Kotlin Coroutines & StateFlow**, **Hilt Dependency Injection**, **Navigation Compose (Type-Safe Serialization)**, **AndroidX SplashScreen API**, and a **100% Centralized Resource & Design System**.

The application runs in **Horizontal (Landscape) Orientation** (`sensorLandscape`) to support both the playful **Splash Screen** and the **Horizontal Levels Roadmap Template** (featuring the 3x6 All-Topics Tilted Card Grid and the Horizontal Scrolling Topic & Activity Cards Roadmap).

```
com.kidsenglish.app
├── KidsEnglishApplication.kt            # @HiltAndroidApp entry point
├── MainActivity.kt                      # Single Activity with installSplashScreen() & enableEdgeToEdge()
├── core
│   ├── constants
│   │   └── AppConstants.kt              # Timing constants, preferences keys, and UI TestTags
│   ├── designsystem
│   │   ├── colors
│   │   │   └── AppColors.kt             # All semantic, Splash, and Horizontal Roadmap color & brush tokens
│   │   ├── dimensions
│   │   │   └── AppDimensions.kt         # All spacing, card sizes, HUD sizes, radii, and font size tokens
│   │   ├── icons
│   │   │   └── AppIcons.kt              # Centralized icon registry (AppLogo, HUD icons, 18 Topic icons)
│   │   ├── images
│   │   │   └── AppImages.kt             # Centralized illustration drawable registry
│   │   ├── shapes
│   │   │   └── AppShapes.kt             # Centralized Material 3 rounded shapes
│   │   ├── theme
│   │   │   └── AppTheme.kt              # Root MaterialTheme wrapper
│   │   └── typography
│   │       └── AppTypography.kt         # Custom Fredoka & Nunito font families + Material 3 Typography
│   ├── navigation
│   │   ├── AppDestination.kt            # Type-safe @Serializable route definitions (Splash, LevelsRoadmap)
│   │   └── AppNavigation.kt             # Root NavHost graph
│   └── resources
│       └── AppStrings.kt                # Centralized @StringRes registry grouped by feature
├── data
│   ├── preferences
│   │   └── AppPreferencesDataSource.kt  # DataStore Preferences wrapper for launch count, stickers, audio
│   └── repository
│       └── AppStartupRepositoryImpl.kt  # Concrete repository providing startup config & roadmap sections
├── di
│   └── AppModule.kt                     # Hilt @Module bindings for DataStore and repositories
├── domain
│   ├── model
│   │   ├── AppStartupConfig.kt          # Domain model for startup state & persisted progress
│   │   └── RoadmapModels.kt             # Domain models for 3x6 Topic Categories & Horizontal Roadmap Cards
│   ├── repository
│   │   └── AppStartupRepository.kt      # Domain repository contract
│   └── usecase
│       └── InitializeAppStartupUseCase.kt # Business logic for cold-start initialization
└── feature
    ├── splash
    │   └── presentation
    │       ├── SplashScreen.kt          # SplashRoute & stateless SplashScreen
    │       ├── SplashUiState.kt         # Immutable Splash UI state
    │       ├── SplashViewModel.kt       # @HiltViewModel managing startup progress
    │       └── components               # Modular Splash UI components
    └── roadmap
        └── presentation
            ├── RoadmapScreen.kt         # RoadmapRoute & stateless Horizontal Levels Roadmap Screen
            ├── RoadmapUiState.kt        # Immutable Roadmap UI state
            ├── RoadmapViewModel.kt      # @HiltViewModel managing 3x6 grid & horizontal roadmap sections
            └── components
                ├── AlphabetWatermarkBackground.kt
                ├── HorizontalSectionsView.kt
                ├── RoadmapTopHudBar.kt
                └── TopicsGridView.kt
```

---

## 2. Package Responsibilities & Dependency Flow

### Dependency Rule
Dependencies flow strictly inward toward the **Domain** layer:

`Presentation (feature/*) ──► Domain (domain/*) ◄── Data (data/*)`

- **`core/`**: Cross-cutting design system tokens, centralized resource registries (`AppStrings`, `AppColors`, `AppDimensions`, `AppIcons`, `AppImages`), type-safe navigation routes, and application constants.
- **`domain/`**: Pure Kotlin business models (`AppStartupConfig`, `RoadmapTopicCategory`, `RoadmapActivityCard`, `RoadmapHorizontalSection`), repository interfaces (`AppStartupRepository`), and use cases (`InitializeAppStartupUseCase`). Contains zero UI or Android framework dependencies (except `@StringRes` annotations).
- **`data/`**: Concrete implementations of domain repositories (`AppStartupRepositoryImpl`) backed by Jetpack DataStore (`AppPreferencesDataSource`).
- **`di/`**: Hilt modules (`AppModule`) providing singleton instances of `DataStore<Preferences>` and binding `AppStartupRepositoryImpl` to `AppStartupRepository`.
- **`feature/`**: Self-contained presentation vertical slices (`feature/splash`, `feature/roadmap`), each following unidirectional data flow (`UiState` + `ViewModel` + stateless `*Screen` + modular `components/`).

---

## 3. UI State Management & Navigation Approach

### Unidirectional Data Flow (MVVM)
1. Each screen defines an immutable `data class *UiState` (`SplashUiState`, `RoadmapUiState`).
2. Each `@HiltViewModel` exposes a read-only `StateFlow<*UiState>` and public intent functions.
3. Route composables (`SplashRoute`, `RoadmapRoute`) collect state lifecycle-safely via `collectAsStateWithLifecycle()` and pass state + event lambdas down to stateless screen composables (`SplashScreen`, `RoadmapScreen`).

### Type-Safe Navigation
- All routes are defined as `@Serializable` objects/classes inside `sealed interface AppDestination` (`AppDestination.Splash`, `AppDestination.LevelsRoadmap`).
- `AppNavigation` hosts the `NavHost`, starting at `AppDestination.Splash` and automatically transitioning to `AppDestination.LevelsRoadmap` (popping `Splash` off the backstack) as soon as `SplashUiState.isReady` becomes `true`.

---

## 4. Centralized Resource Management & Design System Rules

**Hardcoded values are strictly prohibited inside Composable UI files.**

| Resource Type | Centralized Source | XML / Underlying Source |
| :--- | :--- | :--- |
| **Strings** | `AppStrings.<Feature>.<KEY>` | `res/values/strings.xml`, `values-uz/`, `values-en/`, `values-ru/` |
| **Colors & Brushes** | `AppColors.<Token>` | `core/designsystem/colors/AppColors.kt` |
| **Dimensions & Font Sizes** | `AppDimensions.<Token>` | `core/designsystem/dimensions/AppDimensions.kt` |
| **Icons** | `AppIcons.<Token>` | `core/designsystem/icons/AppIcons.kt` & `res/drawable/ic_*.xml` |
| **Images & Illustrations** | `AppImages.<Token>` | `core/designsystem/images/AppImages.kt` & `res/drawable/img_*.xml` |
| **Shapes** | `AppShapes` | `core/designsystem/shapes/AppShapes.kt` |
| **Typography** | `AppTypography` | `core/designsystem/typography/AppTypography.kt` (`res/font/fredoka.ttf`, `nunito.ttf`) |

---

## 5. Multi-Language String Localization Structure

All user-facing text is localized across four resource directories:
- `app/src/main/res/values/strings.xml` — Default strings (Uzbek primary learning language)
- `app/src/main/res/values-uz/strings.xml` — Uzbek localization
- `app/src/main/res/values-en/strings.xml` — English localization
- `app/src/main/res/values-ru/strings.xml` — Russian localization

Every string resource is documented with a comment in `strings.xml` and exposed through `AppStrings.kt` by feature (`AppStrings.Global`, `AppStrings.Splash`, `AppStrings.Roadmap`).

---

## 6. Step-by-Step Developer Guides

### How to Add a New Feature or Screen
1. Create a new feature package under `feature/<feature_name>/presentation/`.
2. Define `<Feature>UiState.kt`, `<Feature>ViewModel.kt` (`@HiltViewModel`), and `<Feature>Screen.kt` (`<Feature>Route` + stateless `<Feature>Screen`).
3. Add a `@Serializable` destination inside `core/navigation/AppDestination.kt`.
4. Register the destination in `core/navigation/AppNavigation.kt` using `composable<AppDestination.<NewFeature>>`.

### How to Add New Strings & Languages
1. Add the `<string name="feature_key_name">...</string>` entry with a descriptive comment to:
   - `res/values/strings.xml`
   - `res/values-uz/strings.xml`
   - `res/values-en/strings.xml`
   - `res/values-ru/strings.xml`
2. Expose the `@StringRes val KEY_NAME: Int = R.string.feature_key_name` inside the appropriate nested object in `core/resources/AppStrings.kt`.
3. Consume in Compose using `stringResource(id = AppStrings.<Feature>.KEY_NAME)`.
4. To add a new language (e.g., Kazakh `values-kk`), create `app/src/main/res/values-kk/strings.xml` and translate all keys from `values/strings.xml`.

### How to Add a New Color
1. Open `core/designsystem/colors/AppColors.kt`.
2. Add a documented `val` property explaining which screen/component uses the color (and add a reusable `Brush` if it is part of a gradient).
3. Reference `AppColors.<NewColor>` in your Composable. Never call `Color(0x...)` inline in UI files.

### How to Add a New Dimension or Font Size
1. Open `core/designsystem/dimensions/AppDimensions.kt`.
2. Add a documented `Dp` or `TextUnit` property under the appropriate section.
3. Reference `AppDimensions.<NewDimension>` in your Composable. Never write `.dp` or `.sp` literals inline in UI files.

### How to Add a New Image or Icon
1. Place vector/bitmap assets in `app/src/main/res/drawable/` following the naming convention (`ic_<feature>_<name>.xml` for icons, `img_<feature>_<name>.xml` for illustrations).
2. Register the drawable resource ID or `ImageVector` inside `core/designsystem/icons/AppIcons.kt` or `core/designsystem/images/AppImages.kt` with a comment stating where it is used.
3. Reference `AppIcons.<Name>` or `AppImages.<Name>` in your Composable.

---

## 7. Resource Naming Conventions

- **Strings (`strings.xml`)**: `<feature>_<element>_<purpose>` in `snake_case` (e.g., `splash_welcome`, `roadmap_stickers_today`, `topic_colours`, `activity_ice_cream_paint`, `cd_splash_mascot`).
- **Icons (`res/drawable/`)**: `ic_<feature>_<name>.xml` (e.g., `ic_app_logo.xml`).
- **Illustrations (`res/drawable/`)**: `img_<feature>_<name>.xml` (e.g., `img_splash_mascot.xml`).
- **Compose Test Tags (`AppConstants.TestTags`)**: `<screen_or_component>_<element>` in `snake_case` (e.g., `splash_screen_root`, `hud_grid_toggle_button`, `topics_grid_container`).
