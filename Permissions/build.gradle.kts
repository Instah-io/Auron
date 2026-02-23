@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
    //signing
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
}

mavenPublishing {
    publishToMavenCentral()
    //signAllPublications()

    coordinates(group.toString(), "permissions", version.toString())

    pom {
        name = "Auron Permissions Library"
        description = "The Permissions Library for Auron"

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
    compileSdkVersion = "android-36"
    namespace = "io.instah.auron"

    defaultConfig {
        minSdk = 28
    }
}

/*
signing {
    useGpgCmd()
    sign(publishing.publications)
}*/
