# Heart Rate Recovery for Fitness on a Smartwatch

## File Structure

The root directory for the project can be found at:

`..\src\smartwatch\WorkoutTracker\` 

This includes the Gradle build files, responsible for compiling and downloading dependencies

The Kotlin files responsible for the function of the application are stored in:

`..\src\smartwatch\WorkoutTracker\app\src\main\java\com\example\workouttracker\presentation` 

This contains all of the Kotlin files available to view

## Requirements

There are only two requirements for the project to run as expected

- Android Studio Iguana*
- Mobvoi TicWatch Pro 3 GPS smartwatch with Android 11 “R” installed*

*Note -  The application *could* work on other devices or Android/Android Studio versions, however this is a known working installation method for the libraries the application is reliant on

## Build Instructions

To build the application:

- Launch Android Studio
- Click the hamburger menu in the top-left of the screen
- Hover over `Build` > `Build Bundle(s) / APK(s)`
- Click `Build APK(s)`

This will install all the required dependencies and create an Android Package (APK) to be installed onto the watch.

The resulting APK can be found at:

`..\src\smartwatch\WorkoutTracker\app\build\outputs\apk\debug\app-debug.apk`

At this time, Gradle will also ensure correctness in code, logging any errors in the build process. This can also be done without building the application to an APK by clicking F9, or the hammer icon located on the top ribbon

## Running Instructions

- To debug the application on the watch, connect your laptop/PC and the TicWatch to the same Wi-Fi network.
- Ensure the watch is in developer mode, by going into “Settings”, then “System”, then “About”, then “Versions”, then clicking on the “Build Number” 7 times consecutively
- Turn on “ADB Debugging” and “Wireless Debugging” in the Developer Settings
- Launch Android Studio and open the “Workout Tracker” folder
- On Android Studio, navigate to “Device Manager” from the right hand ribbon
- Click on the Wi-Fi symbol to Debug over Wi-Fi
- Click on the “Pair using pairing code”
    - From experience, this can unfortunately be temperamental. If the smartwatch isn’t shown try restarting Android Studio, or disconnect and reconnect to Wi-Fi
- Select your device and enter the pairing code, shown on the watch
- Once the watch is connect, you can click the Play icon on the device to build and install the application to the watch