plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.devtoolsKsp) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    id("org.jetbrains.dokka") version "2.0.0" apply false
    alias(libs.plugins.hilt.android) apply false
}
