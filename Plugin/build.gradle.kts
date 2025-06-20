import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `java-gradle-plugin`
    id("com.vanniktech.maven.publish")
    id("com.github.gmazzo.buildconfig") version "5.5.1"
    signing
}

buildConfig {
    buildConfigField("version", version.toString())
}

gradlePlugin {
    plugins {
        create("Gradle") {
            id = "io.instah.Auron-Gradle"
            implementationClass = "io.instah.auron.gradlePlugin.AuronPlugin"
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(group.toString(), "gradle-plugin", version.toString())

    pom {
        name = "Auron Gradle Plugin"
        description = "The Auron Gradle Plugin"

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

dependencies {
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:2.1.20")
    implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:latest.release")
    api("com.android.library:com.android.library.gradle.plugin:8.7.3")
    api("com.android.application:com.android.application.gradle.plugin:8.7.3")
    implementation("com.soywiz.korge:korge-core:latest.release")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.7.3")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:2.1.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("com.github.ajalt.mordant:mordant:3.0.2")
    implementation(project(":Permissions"))
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

