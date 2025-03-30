package io.instah.auron.gradlePlugin.android.manifest

import korlibs.io.file.VfsFile
import korlibs.io.serialization.xml.Xml

internal fun AndroidManifestConfig.generateManifest(
    projectDir: VfsFile
): String {
    var xml = Xml.parse(
        str = """
        <manifest xmlns:android="http://schemas.android.com/apk/res/android">
            <uses-permission android:name="android.permission.INTERNET" />
            <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
            <uses-permission android:name="android.permission.VIBRATE" />
            ${
                this.permissions.joinToString(
                    separator = "\n"
                ) { permissionName ->
                "   <uses-permission android:name=\"${permissionName}\" />"
                }
            }
        
            ${
                this.usedFeatures.joinToString(
                    separator = "\n"
                ) { featureName ->
                    "   <uses-feature android:name=\"${featureName}\" android:required=\"false\"/>"
                }
            }
        
        ${this.applicationConfig.generateManifestSection(projectDir)}
    </manifest>
        """.trimIndent().split('\n').filter(String::isNotBlank)
            .joinToString(separator = "\n")
    )

    this.manifestConfigureScope.processes.forEach {
        xml = it(xml)
    }

    return xml.toOuterXmlIndentedString()
}