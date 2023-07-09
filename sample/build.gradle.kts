@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.detekt)
}

android {
    namespace = "dev.obvionaoe.compose.swipeablecard.sample"
    compileSdk = 33

    defaultConfig {
        applicationId = "dev.obvionaoe.compose.swipeablecard.sample"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    detektPlugins(libs.bundles.detekt.plugins)

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.libraries)
    implementation(project(mapOf("path" to ":swipeablecard")))
}

detekt {
    autoCorrect = false
    baseline = rootProject.file("config/detekt/baseline.xml")
    basePath = rootProject.path
    config.setFrom(rootProject.file("config/detekt/detekt.yml").path)
    parallel = true
}
