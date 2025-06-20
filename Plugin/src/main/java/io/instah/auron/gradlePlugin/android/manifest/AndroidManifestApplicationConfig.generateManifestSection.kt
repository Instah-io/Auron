package io.instah.auron.gradlePlugin.android.manifest

import korlibs.io.file.VfsFile
import kotlinx.coroutines.runBlocking

internal fun AndroidManifestConfig.AndroidManifestApplicationConfig?.generateManifestSection(
    projectDir: VfsFile
): String {
    val hasIcon = runBlocking {
        projectDir["src"]["androidMain"]["res"].let {
            if (it.exists()) it.listNames().any {
                it.contains("mipmap")
            } else false
        }
    }

    if (this == null) return ""

    return """
        <application
                android:allowBackup="true"
                android:label="${this.name}"
                ${if (hasIcon) "android:icon=\"@mipmap/ic_launcher\"" else ""}
                android:supportsRtl="true"
                android:launchMode="singleTop"
                android:theme="@android:style/Theme.Material.Light.NoActionBar">
          
                
            <activity
                    android:exported="true"
                    android:configChanges="orientation|screenSize|screenLayout|keyboardHidden|mnc|colorMode|density|fontScale|fontWeightAdjustment|keyboard|layoutDirection|locale|mcc|navigation|smallestScreenSize|touchscreen|uiMode"
                    android:name="io.instah.auron.appSdk.MainActivity">
                <intent-filter>
                    <action android:name="android.intent.action.MAIN" />

                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
            </activity>
        </application>
    """
}

/*in <application>
<receiver
                android:name="io.instah.auron.appSdk.WidgetProvider"
                android:exported="true">
                <intent-filter>
                    <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
                </intent-filter>
                <meta-data
                    android:name="android.appwidget.provider"
                    android:resource="@xml/my_widget_info" />
            </receiver>
 */