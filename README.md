# StackOverflow Top Users App

An Android application that displays the top 20 StackOverflow users by reputation with follow/unfollow functionality.

## Demo

<p align="center">
  <img src="app-demo.gif" width="300" alt="App Demo"/>
</p>

## Architecture

The app follows **MVVM + Clean Architecture** with three main layers:

### Data Layer
- `StackOverflowApi`: Retrofit interface for API calls
- `UserDto/UsersResponse`: Network models
- `UserRepositoryImpl`: Implementation handling API calls and local storage

### Domain Layer
- `User`: Domain model
- `UserRepository`: Repository interface
- `GetUsersUseCase`: Fetches users from repository
- `ToggleFollowUseCase`: Toggles follow status

### Presentation Layer
- `UsersViewModel`: Manages UI state using StateFlow
- `UsersScreen`: Compose UI components

## State Management

The app uses a sealed interface `UsersUiState` for state machine pattern:
- `Loading`: Initial loading state
- `Success`: Users loaded successfully
- `Error`: Network/API error with retry option

## Technical Stack

- Kotlin
- Jetpack Compose
- Hilt
- Retrofit
- Coil
- Coroutines/Flow
- StateFlow

## Testing

Unit tests using MockK for ViewModels, Domain and Data layers.

Run tests:
```bash
./gradlew test
```

## Building & Running

Just clone the repository, open in Android Studio and Run.

## Features

- Display top 20 StackOverflow users
- Profile image, name, and formatted reputation
- Follow/Unfollow functionality (persisted locally)
- Error handling with retry option
- Loading state indicator

## Design Decisions

1. **Clean Architecture**: Separates concerns and makes testing easier
2. **StateFlow**: Provides lifecycle-aware state observation
3. **SharedPreferences**: Simple persistence for followed users
4. **Result type**: Kotlin's Result for error handling without exceptions
