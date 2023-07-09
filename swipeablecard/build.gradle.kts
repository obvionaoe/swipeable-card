@file:Suppress("UnstableApiUsage")

import com.vanniktech.maven.publish.SonatypeHost
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.maven.publish)
}

fun version(): String {
    val os = ByteArrayOutputStream()
    project.exec {
        commandLine = "git describe".split(" ")
        standardOutput = os
    }
    return String(os.toByteArray()).trim()
}

android {
    namespace = "dev.obvionaoe.compose.swipeablecard"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    detektPlugins(libs.bundles.detekt.plugins)

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.libraries)
}

detekt {
    autoCorrect = false
    baseline = rootProject.file("config/detekt/baseline.xml")
    basePath = rootProject.path
    config.setFrom(rootProject.file("config/detekt/detekt.yml").path)
    parallel = true
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01, true)

    signAllPublications()

    coordinates("dev.obvionaoe.compose", "swipeable-card", version())

    pom {
        name.set("SwipeableCard")
        description.set("A swipeable Material 3 Card composable")
        inceptionYear.set("2023")
        url.set("https://github.com/obvionaoe/swipeable-card/")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/obvionaoe/swipeable-card/blob/master/LICENSE")
                distribution.set("https://github.com/obvionaoe/swipeable-card/blob/master/LICENSE")
            }
        }
        developers {
            developer {
                id.set("obvionaoe")
                name.set("obvionaoe")
                url.set("https://github.com/obvionaoe/")
            }
        }
        scm {
            url.set("https://github.com/obvionaoe/swipeable-card/")
            connection.set("scm:git:git://github.com/obvionaoe/swipeable-card.git")
            developerConnection.set("scm:git:ssh://git@github.com/obvionaoe/swipeable-card.git")
        }
    }
}
