# HunSpell Android

A lightweight Android library for spell checking and suggestions using HunSpell dictionaries. Works seamlessly with Kotlin and Jetpack Compose.

---

## Features

- ✅ Check if a word is spelled correctly.
- ✅ Suggest correct spellings for incorrect words.
- ✅ Normalize text automatically (trim, lowercase, etc.).
- ✅ Easy integration into any Android project.

---

## Installation

### JitPack Version

[![](https://jitpack.io/v/hrithik1233/Hunspell-android.svg)](https://jitpack.io/#hrithik1233/Hunspell-android)

### Using JitPack

Add JitPack repository to your root `build.gradle`:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the library to your module build.gradle dependencies:
```
dependencies {
    implementation("com.github.hrithik1233:HunSpell-android:Tag")
}
```
