**Test Project**

This is a sample project developed using Android Flamingo framework and follows the Kotlin MVVM architecture pattern with dependency injection using Dagger Hilt.

**Project Setup**

To build the project, follow these steps:
Open the project in Android Studio.
Wait for all the Gradle dependencies to be downloaded.
Go to the top menu and select Build.
Choose either Build Bundle(s) or Build APK(s) option.
**Build Bundle(s):** Generates an Android App Bundle that optimizes APK delivery based on the target device configuration.
**Build APK(s):** Generates individual APK files for each module in the project.
After selecting the desired build option, Android Studio will start building the project.
Once the build process is complete, you can find the generated APK(s) or App Bundle(s) in the project's outputs directory.


**Project Structure**
The project follows the MVVM architecture pattern with a modularized structure. Here's an overview of the project structure:

**app:** Contains the main application module, which includes the entry point of the application.
**data:** Contains data-related classes, including data models, repositories, and data sources.
**di:** Contains classes and modules for dependency injection using Dagger Hilt.
**ui:** Contains the user interface-related classes, including activities, fragments.
**viewmodel:** Contains the ViewModel classes that handle the app's logic and provide data to the UI.

**Feel free to explore the project structure and modify it according to your requirements.**

**Libraries and Frameworks Used**


**The project utilizes the following libraries and frameworks:**

**Android Flamingo:** An Android framework for simplifying development, providing pre-built UI components, and enhancing productivity.
**Kotlin Coroutines:** A concurrency design pattern that simplifies the execution of asynchronous tasks in Kotlin.
**Dagger Hilt:** A dependency injection library for Android that reduces boilerplate code and manages dependencies.
**ViewModel:** A component of the Android Architecture Components that manages UI-related data and survives configuration changes.
**LiveData:** An observable data holder that notifies its observers when the underlying data changes.
**Retrofit:** A type-safe HTTP client for Android and Java that simplifies the process of consuming RESTful APIs.
**Gson:** A JSON serialization/deserialization library for Java and Kotlin.
**OkHttp:** A powerful HTTP client for Android and Java applications.


**Feel free to explore the project's codebase and make changes according to your needs.**


**Contributing**
Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to create a pull request or open an issue in the project's repository.

License
This project is licensed under the MIT License. You are free to use, modify, and distribute the code in this repository. Please see the LICENSE file for more details.
