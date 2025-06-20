@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
    id("com.vanniktech.maven.publish")
    kotlin("plugin.serialization")
    signing
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        publishLibraryVariants("release")
    }

    jvm()
    wasmJs {
        browser()
    }

    sourceSets {
        androidMain.dependencies {
            api("androidx.activity:activity-compose:1.10.1")
            implementation("androidx.glance:glance:1.2.0-alpha01")
            implementation("androidx.glance:glance-appwidget:1.2.0-alpha01")
        }

        commonMain.dependencies {
            api(compose.ui)
            api(compose.foundation)
            api(compose.runtime)
            api(compose.animation)
            api(project(":Permissions"))
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            api(compose.material3)
        }
    }
}
//TODO: Add shadow jar function to desktop

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(group.toString(), "sdk", version.toString())

    pom {
        name = "Auron SDK"
        description = "The SDK for Auron"

        inceptionYear = "2023"
        url = "https://github.com/instah-pl/auron"

        developers {
            developer {
                id = "rebokdev"
                name = "rebokdev"
                email = "rebok@duck.com"
            }
        }

        licenses {
            license {
                name = "MIT license"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        scm {
            connection = "scm:git:https://github.com/instah-pl/auron.git"
            developerConnection = "scm:git:git@github.com:instah-pl/auron.git"
            url = "https://github.com/instah-pl/auron"
        }
    }
}

android {
    compileSdkVersion = "android-35"
    namespace = "io.instah.auron"

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
