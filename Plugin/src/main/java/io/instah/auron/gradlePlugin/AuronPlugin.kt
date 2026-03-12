package io.instah.auron.gradlePlugin

import io.instah.auron.gradlePlugin.config.AuronAdditionalProperties
import AuronTarget
import korlibs.io.file.VfsFile
import korlibs.io.file.std.localVfs
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project

//TODO: Predict android plugin fails (when config has changed but the plugin is still applied)
class AuronPlugin : Plugin<Project> {
    companion object {
        val prettyJson = Json {
            prettyPrint = true
        }

        var additionalProperties: AuronAdditionalProperties = AuronAdditionalProperties()

        lateinit var additionalPropertiesFile: VfsFile
    }

    override fun apply(target: Project) {
        var additionalPropertiesFileExists = false
        additionalPropertiesFile = localVfs(target.projectDir.absolutePath)["auron.json"]
        val noAuronFileExists = runBlocking { localVfs(target.projectDir.absolutePath)[".noAuron"].exists() }

        runBlocking {
            if (additionalPropertiesFile.exists()) {
                additionalPropertiesFileExists = true
                val additionalProperties = Json.decodeFromString<AuronAdditionalProperties>(
                    additionalPropertiesFile.readString()
                )

                AuronPlugin.additionalProperties = additionalProperties
            }
        }

        if (additionalPropertiesFileExists || noAuronFileExists) {
            target.pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            if (noAuronFileExists) return
        }

        if (additionalProperties.targets.contains(AuronTarget.Android)) {
            if (additionalProperties.isLibrary) {
                target.pluginManager.apply("com.android.library")
            } else {
                target.pluginManager.apply("com.android.application")
            }
        }

        if (additionalProperties.useCompose) {
            target.pluginManager.apply("org.jetbrains.compose")
            target.pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        }

        target.repositories.google()
        target.repositories.mavenCentral()
    }
}