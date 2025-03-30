package io.instah.auron.gradlePlugin.android.manifest

import io.instah.auron.gradlePlugin.config.AuronConfigScope.ManifestConfigureScope

data class AndroidManifestConfig(
    val permissions: List<String>,
    val usedFeatures: List<String>,
    val applicationConfig: AndroidManifestApplicationConfig? = null,
    val manifestConfigureScope: ManifestConfigureScope = ManifestConfigureScope()
) {
    data class AndroidManifestApplicationConfig(
        val name: String
    )
}
