package io.instah.auron.gradlePlugin.desktop

import io.instah.auron.gradlePlugin.framework.AuronTargetLogic
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

class DesktopTargetLogic : AuronTargetLogic() {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    override fun executeInKotlinBlock(kotlinExtension: KotlinMultiplatformExtension): KotlinSourceSet = kotlinExtension.run {
        jvm("desktop") {
            if (!config.isLibrary) {
                mainRun {
                    mainClass.set(project.group.toString()+".MainKt")
                }
            }
        }

        return@run sourceSets.getByName("desktopMain")
    }
}