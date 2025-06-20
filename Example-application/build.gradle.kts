@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("io.instah.Auron-Gradle") version "LATEST-SNAPSHOT"
}

group = "com.example"

auron {
    setTargets(AuronTarget.Android)

    dependencies {
        implementation(auron.voyager.navigator)
        implementation(project(":Example-library"))

        android {
            implementation("androidx.activity:activity-compose:1.10.1")
            implementation("androidx.glance:glance:1.2.0-alpha01")
            implementation("androidx.glance:glance-appwidget:1.2.0-alpha01")
        }
    }
}