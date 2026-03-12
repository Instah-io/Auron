include(":Sdk")
include(":Plugin")
//include(":Example-application")
//include(":Example-library")
include(":Application-Sdk")
include(":Permissions")
include(":Main-Link-Dummy")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    kotlin("multiplatform") version "2.3.10" apply false
    kotlin("jvm") version "2.3.10" apply false
    kotlin("plugin.serialization") version "2.3.10" apply false
    kotlin("plugin.compose") version "2.3.10" apply false
    id("org.jetbrains.compose") version "1.10.2" apply false
    id("com.android.application") version "8.12.3" apply false
    id("com.android.library") version "8.12.3" apply false
    id("com.vanniktech.maven.publish") version "0.34.0" apply false
}

rootProject.name = "Auron"

