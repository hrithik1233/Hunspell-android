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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0" // must match Compose version
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")

    // Lifecycle & runtime
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")
}

// Sources jar
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs())
    from(android.sourceSets["main"].kotlin.srcDirs())
}

// Publishing
publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.hrithik1233"
            artifactId = "HunSpell-android"
            version = "v1.0.0"

            artifact("$buildDir/outputs/aar/${project.name}-release.aar")
            artifact(sourcesJar.get())
        }
    }
}

// Make sure AAR exists before publishing
tasks.withType<PublishToMavenLocal> {
    dependsOn("assembleRelease")
}