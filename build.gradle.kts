// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false

    // 🔑 Declare Kotlin Android ONCE at top level
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    // Compose plugin (depends on Kotlin)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false

    // KSP
    alias(libs.plugins.ksp) apply false

    // Hilt
    alias(libs.plugins.hilt) apply false
}