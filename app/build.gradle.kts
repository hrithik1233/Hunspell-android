plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.fitmind.hunsspell"
    compileSdk = 36

    defaultConfig {
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }

        // 🔥 IMPORTANT (ABI support)
        ndk {
            abiFilters += listOf(
                "armeabi-v7a",
                "arm64-v8a",
                "x86",
                "x86_64"
            )
        }

        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    externalNativeBuild {
        cmake {
            path ("src/main/cpp/CMakeLists.txt")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }







    // ✅ THIS is what was missing — registers the "release" component
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}



afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"]) // ✅ Now this will actually find it
                groupId = "com.github.hrithik1233"
                artifactId = "HunSpell-android"
                version = "1.0.8"
            }
        }
    }
}

tasks.withType<PublishToMavenLocal> {
    dependsOn("assembleRelease")
}