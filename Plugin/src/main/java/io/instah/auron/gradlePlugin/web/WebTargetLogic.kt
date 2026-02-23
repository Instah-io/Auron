package io.instah.auron.gradlePlugin.web

import io.instah.auron.gradlePlugin.framework.AuronTargetLogic
import korlibs.io.file.std.toVfs
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

//TODO: Add support for PWAs
class WebTargetLogic : AuronTargetLogic() {
    @OptIn(ExperimentalWasmDsl::class)
    override fun executeInKotlinBlock(kotlinExtension: KotlinMultiplatformExtension): KotlinSourceSet = kotlinExtension.run {
        wasmJs("web") {
            outputModuleName.set(kotlinExtension.project.name)

            if (!config.isLibrary) {
                binaries.executable()

                browser {
                    commonWebpackConfig {
                        it.outputFileName = "${config.configUUID}-main.js"
                    }

                    if (config.useExperimentalWebOptimizations) {
                        useEsModules()
                    }
                }
            } else {
                browser()
            }
        }

        val webMain = sourceSets.getByName("webMain")
        val webGeneratedMain = sourceSets.create("webGeneratedMain")
        webGeneratedMain.dependsOn(sourceSets.getByName("commonMain"))
        webMain.dependsOn(webGeneratedMain)

        if (!config.isLibrary) {
            runBlocking {
                webGeneratedMain.resources.srcDirs.first().let {
                    it.mkdirs()

                    it.toVfs()["index.html"].writeString(
                        generateIndexHtml(config)
                    )
                }
            }
        }

        return@run webMain
    }
}