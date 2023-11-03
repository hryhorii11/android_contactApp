
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.6.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false

}
buildscript {

    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.7.2"
        val hilVersion="2.48"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilVersion")
    }
}