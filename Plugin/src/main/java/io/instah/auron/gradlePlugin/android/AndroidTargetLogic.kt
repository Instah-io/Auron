package io.instah.auron.gradlePlugin.android

import com.android.build.gradle.BaseExtension
import io.instah.auron.gradlePlugin.android.manifest.AndroidManifestConfig
import io.instah.auron.gradlePlugin.android.manifest.generateManifest
import io.instah.auron.gradlePlugin.framework.AuronTargetLogic
import io.instah.auron.gradlePlugin.util.getSdkDir
import io.instah.auron.gradlePlugin.util.introducePropertyIfNotPresent
import io.instah.auron.permissions.toAndroidPermissionNamesAndFeatures
import korlibs.io.file.std.get
import korlibs.io.file.std.localVfs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

//TODO: Make the activity compose version to be fetched from somewhere
class AndroidTargetLogic : AuronTargetLogic("mobileMain") {
    override fun executeInProject(project: Project) {
        introducePropertyIfNotPresent(
            file = localVfs(project.rootProject.projectDir.absolutePath)["local.properties"],
            property = "sdk.dir", value = getSdkDir()
        )

        introducePropertyIfNotPresent(
            file = localVfs(project.rootProject.projectDir.absolutePath)["gradle.properties"],
            property = "android.useAndroidX", value = "true"
        )

        val androidExtension = project.extensions.findByName("android") as? BaseExtension
        androidExtension?.namespace = project.group.toString()
        androidExtension?.compileSdkVersion = "android-36"

        androidExtension?.compileOptions {
            it.sourceCompatibility = JavaVersion.toVersion(config.jvmToolchain)
            it.targetCompatibility = JavaVersion.toVersion(config.jvmToolchain)
        }

        androidExtension?.defaultConfig {
            it.minSdk = 28
            it.targetSdk = 36

            if (!config.isLibrary) {
                it.applicationId = config.applicationId
            }
        }

        androidExtension?.buildFeatures?.compose = config.useCompose

        /*val mainSourceSet = androidExtension?.sourceSets?.getByName("main")
        val manifestFile = mainSourceSet!!.java.srcDirs.first().parentFile
            .parentFile["androidGeneratedMain"]["AndroidManifest.xml"]
        mainSourceSet.manifest.srcFile(manifestFile.absolutePath)*/
    }

    override fun executeInKotlinBlock(extension: KotlinMultiplatformExtension): KotlinSourceSet = extension.run {
        androidTarget("androidGenerated")

        val androidMain = sourceSets.create("androidMain")

        val androidGeneratedMain = sourceSets.getByName("androidGeneratedMain") {
            it.dependencies {
                implementation("androidx.activity:activity-compose:1.10.1")
            }

            it.dependsOn(androidMain)
        }

        val generatedCodeDir = androidGeneratedMain.kotlin.srcDirs.first()

        if (!config.isLibrary) {
            generatedCodeDir.mkdirs()
            generatedCodeDir["mainLink.kt"].writeText(
                text = """
                            package io.instah.auron.mainLink
        
                            val mainLink: () -> Unit = { ${project.group}.main() }
                        """.trimIndent()
            )
        }

        val sourceSetDirectory = androidGeneratedMain.kotlin.srcDirs.first().parentFile
        sourceSetDirectory.mkdirs()

        sourceSetDirectory["AndroidManifest.xml"].writeText(
            text = AndroidManifestConfig(
                permissions = config.permissions.flatMap { it.toAndroidPermissionNamesAndFeatures().first } + config.customAndroidPermissions,
                usedFeatures = config.permissions.flatMap { it.toAndroidPermissionNamesAndFeatures().second } + config.customAndroidFeatures,
                applicationConfig = if (!config.isLibrary) AndroidManifestConfig.AndroidManifestApplicationConfig(
                    name = config.applicationName
                ) else null, manifestConfigureScope = config.manifestConfigureScope
            ).generateManifest(localVfs(project.projectDir.absolutePath))
        )

        return@run androidMain
    }
}