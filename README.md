# Finance Tracker

A modern Android app built with Jetpack Compose for tracking personal income and expenses.

## Features

- **Add Transactions**: Easily add income and expense transactions with categories and notes
- **View Summary**: See your total income, expenses, and current balance at a glance
- **Transaction List**: View all transactions in a clean, organized list
- **Edit & Delete**: Modify or remove existing transactions
- **Real-time Updates**: Changes are reflected immediately thanks to reactive architecture
- **Modern UI**: Beautiful Material Design 3 interface with dark/light theme support

## Architecture

The app follows Clean Architecture principles with the following layers:

- **Presentation**: Jetpack Compose UI with MVVM pattern
- **Domain**: Business logic and use cases
- **Data**: Repository pattern with Room database
- **DI**: Hilt for dependency injection

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room with Kotlin Coroutines and Flow
- **DI**: Hilt
- **Navigation**: Navigation Compose
- **State Management**: StateFlow

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or device

## Project Structure

```
app/src/main/java/com/example/financetracker/
├── data/
│   ├── local/           # Room database and DAO
│   ├── model/           # Database entities
│   └── repositories/    # Repository implementations
├── domain/
│   ├── model/           # Domain models
│   ├── repositories/    # Repository interfaces
│   └── usecases/        # Business logic use cases
├── presentation/
│   ├── features/        # Feature-specific screens
│   │   ├── home/        # Home screen with transaction list
│   │   └── details/     # Add/Edit transaction screen
│   └── navigation/      # Navigation setup
├── di/                  # Dependency injection modules
└── ui/theme/            # App theme and styling
```

## Usage

1. **Adding Transactions**: Tap the + button to add a new transaction
2. **Viewing Transactions**: All transactions are displayed on the home screen
3. **Editing**: Tap the edit icon on any transaction to modify it
4. **Deleting**: Tap the delete icon to remove a transaction
5. **Summary**: View your financial summary at the top of the home screen

## Screenshots

The app features a clean, modern interface with:
- Summary cards showing income, expenses, and balance
- Transaction list with edit/delete options
- Add transaction dialog with form validation
- Material Design 3 theming

## Contributing

Feel free to submit issues and enhancement requests! 