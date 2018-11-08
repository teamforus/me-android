me-android
==========
Android implementation of the [me app](https://github.com/teamforus/me)

Getting Started
===============

1. Clone this code to your machine
2. Download and fully install [Android Studio](https://developer.android.com/studio/index.html)
3. Open this code in Android Studio (Choosing the containing directory in Android Studio `Open` dialog works)
4. Sync Project with Gradle Files
5. Select product flavor for development (`Build` -> `Select Build Variant` -> `devDebug`)
6. Click the `Play` button (`Shift` + `F10`)

Making a new build for Play store (Dev only)
============================================

1. Open app/build.gradle
2. Increase the androidVersionCode by 1.
3. Optionally change the androidVersionName to whatever you want.
5. Select build variant `prodRelease`
4. Build the APK however you want (in Android Studio or via gradle)
    - You'll need the release certificate, alias and password.
5. Commit the changes to build.gradle, upload the APKs to Play store


<a href='https://play.google.com/store/apps/details?id=io.forus.me'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width='220px'/></a>
