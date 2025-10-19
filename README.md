# ☀️ ClimateChangeForecastApp 🌍
The ClimateChangeForecastApp explores the weather with our intuitive app, which presents two distinct views. The initial screen enables a quick city search to display its current weather and an accompanying 24-hour forecast. A dedicated "Details" view provides a deeper look, including data like humidity and wind direction, along with a curvy diagram that visually illustrates temperature changes over time for an easy-to-understand forecast. This app uses the [WeatherAPI](https://www.weatherapi.com/).
<video src="https://github.com/user-attachments/assets/072b1e6a-0d82-4a90-8cb6-e73e86114a19" controls></video>

## 📋 Key Features

This application is designed to give you quick access to essential weather information with a focus on both immediate and near-term forecasts.

### 1. Comprehensive Current Weather & Forecast

The main view provides a complete overview of a selected city's weather, including the current conditions and a detailed **24-hour hourly forecast**.

* **Automatic Location Detection:** Upon launch, the app intelligently requests your current geographical location to instantly display relevant weather data for your immediate surroundings.
* **Global Search Functionality:** Users can search for and select **any city worldwide** to retrieve its detailed current weather information and 24-hour forecast.

### 2. Detailed Forecast View

Go beyond the hourly snapshot. This feature expands upon the initial data to provide more details.

### 3. Advanced Weather Details

By navigating to the dedicated "Details" view, you gain a deeper understanding of the weather. This section provides more information about the **current weather** and the **forecast**, including:

* Specific metrics like **humidity**, **wind speed**, and **wind direction**.
* A **visual, curvy diagram** that plots the change in **temperature over time**, offering a clear and intuitive understanding of how conditions will fluctuate throughout the day.

## 🏗️ Architecture

This project is built following the **Model-View-ViewModel (MVVM)** architectural pattern.

This structure provides a clear separation of concerns, making the codebase easier to maintain, test, and scale:

* **Model:** Handles the data logic, including networking calls to fetch weather data from the API.
* **View:** The user interface layer (UI). It observes changes in the **ViewModel** and updates the UI accordingly. It remains lightweight and contains no business logic.
* **ViewModel:** Acts as a bridge between the **Model** and the **View**. The ViewModel exposes the necessary data through **StateFlows**, which the View collects. Any change in a **StateFlow** automatically triggers **recomposition** in the View, ensuring the UI is always up-to-date with the latest weather information.

## 🧩 Modules

The project is organized into the following modules to promote reusability and maintainability:
* **App:** The main application module.
* **Core:** Contains common and reusable components.
  * **common:** General utility classes and UI components.
  * **location:** Handles all location-related logic, including requesting permissions and fetching coordinates.
  * **network:** Manages API calls and data fetching from the weather service.
  * **threading:** Provides components for managing coroutines and background operations.
* **Feature:** Contains the distinct feature modules.
  * **currentweather:** Implements the current weather and search functionality.
  * **forecast:** Implements the detailed weather forecast screen.

## ⚙️ Technologies and Libraries
The application leverages a modern Android development stack:

* **Language:** [Kotlin](https://kotlinlang.org/).
* **UI Framework:** [Jetpack Compose](https://developer.android.com/compose) for a declarative UI.
* **Asynchronous Operations:** [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for managing background tasks.
* **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for a clean and scalable DI setup.
* **Location Services:** [Google Play Services Location](https://developer.android.com/develop/sensors-and-location/location) for accurate location data.
* **Image Loading:** [Coil](https://github.com/coil-kt/coil) for image loading and caching.
* **Permissions:** [Accompanist Permissions](https://google.github.io/accompanist/permissions/) to handle runtime permissions gracefully.
* **Navigation:** [Jetpack Navigation](https://developer.android.com/develop/ui/compose/navigation) for app navigation.
* **Networking:**
  * [Retrofit](https://square.github.io/retrofit/) for type-safe HTTP client.
  * [OkHttp](https://square.github.io/okhttp/) for efficient network requests.
  * [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html#what-s-next) for JSON serialization/deserialization.

 ## 🎨 Design
The UI design is inspired by the following Figma community design: [Weather App for iOS or Android](https://www.figma.com/design/y0P4TouoUWlZNpxvt9gfLV/Weather-App-for-iOS-or-Android--Community-?node-id=0-1&p=f&t=jJLjZ0lhFHGBPgib-0).
