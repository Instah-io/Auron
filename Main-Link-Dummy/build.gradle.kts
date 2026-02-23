plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget()

    jvmToolchain(17)
}

android {
    compileSdkVersion = "android-36"
    namespace = "io.instah.auron"

    defaultConfig {
        minSdk = 28
    }
}