# Kids English Learning App

A beautiful, cartoon-style English learning app for children and absolute beginners.
Primary explanations are in **Uzbek**.

## Tech Stack
- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Hilt
- Room (ready)
- DataStore (ready)
- Custom SoundManager

## Features currently implemented
- Animated cartoon Roadmap (bottom → top path)
- Cartoon kid with smooth bounce animation
- Locked / Available / Current / Completed lesson states
- Unlock stars & sparkles animation
- Full Lesson flow (7 sections):
  - Welcome
  - Discover (vocabulary)
  - Understand
  - Practice (interactive exercises)
  - Mini Game
  - Review
  - Completion (stars + score)
- SoundManager (background music + SFX placeholders)
- Sample content for letter **C – Cat / Mushuk**

## How to run
1. Open the project in **Android Studio** (Hedgehog or newer recommended)
2. Let Gradle sync
3. Run on an emulator or device (API 26+)

## Project structure
```
app/src/main/java/com/kidsenglish/app/
├── core/audio/          → SoundManager
├── domain/model/        → Roadmap & Lesson data models
├── ui/
│   ├── roadmap/         → RoadmapScreen
│   ├── lesson/          → LessonScreen (all 7 sections)
│   └── theme/           → Colors & Theme
├── MainActivity.kt
└── KidsEnglishApp.kt
```

## Next steps you can continue with
- Add real audio files into `res/raw/`
- Implement remaining letters A–Z
- Add Room database for progress
- Integrate Gemini TTS
- More mini-games

Enjoy building!
