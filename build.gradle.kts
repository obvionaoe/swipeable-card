@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.lib) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.maven.publish)
}
