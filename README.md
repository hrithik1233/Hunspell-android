HunSpell Android

A lightweight Android library for spell checking and suggestions using HunSpell dictionaries. Works seamlessly with Kotlin and Jetpack Compose.

Features
✅ Check if a word is spelled correctly.
✅ Suggest correct spellings for incorrect words.
✅ Normalize text automatically (trim, lowercase, etc.).
✅ Easy integration into any Android project.
Installation

Add the library to your build.gradle:
[![](https://jitpack.io/v/hrithik1233/Hunspell-android.svg)](https://jitpack.io/#hrithik1233/Hunspell-android)

dependencies {
    implementation("com.github.hrithik1233:HunSpell-android:Tag")
}
Usage
Initialize
val spellChecker = SpellChecker()
spellChecker.init(
    affPath = "path/to/en_US.aff",
    dicPath = "path/to/en_US.dic"
)
Check Spelling
val word = "exampel"
val isCorrect = spellChecker.check(word)
println(isCorrect) // false
Get Suggestions
val suggestions = spellChecker.suggest("exampel")
println(suggestions.joinToString()) // "example, exempt, exemplar"
Normalize Text
val normalized = spellChecker.normalize("  Exampel  ")
println(normalized) // "exampel"
ProGuard/R8

No special rules are required for this library.

Contributing
Fork the repository
Create your feature branch (git checkout -b feature/MyFeature)
Commit your changes (git commit -am 'Add MyFeature')
Push to the branch (git push origin feature/MyFeature)
Create a Pull Request
License

MIT License © Hrithik
