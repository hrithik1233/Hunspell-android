plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.fitmind.hunsspell"
    compileSdk = 36

    defaultConfig {
        minSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }
}

// Sources Jar
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs())
    from(android.sourceSets["main"].kotlin.srcDirs())
}

// Publish manually
publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.fitmind"
            artifactId = "hunsspell"
            version = "1.0.0"

            // Manually reference the AAR output
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")

            // Add sources jar
            artifact(sourcesJar.get())
        }
    }
}