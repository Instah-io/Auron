package io.instah.auron.appSdk

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.content.edit
import androidx.core.net.toUri
import io.instah.auron.mainLink.mainLink
import io.instah.auron.sdk.App
import io.instah.auron.sdk.AuronRuntimeManager
import io.instah.auron.sdk.permissions.PermissionManager
import io.instah.auron.sdk.runtimeManager.*
import io.instah.auron.sdk.window.LocalWindow
import io.instah.auron.sdk.window.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    @SuppressLint("SwitchIntDef")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuronRuntimeManager.checkIsPermissionGranted = {
            this.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }

        AuronRuntimeManager.getSharedPreferences = { getSharedPreferences(it, MODE_PRIVATE) }

        AuronRuntimeManager.checkIsManualPermissionGrantRequired = lambda@{
            if (
                getSharedPreferences("auron:permission-data", MODE_PRIVATE)
                    .getBoolean("initial-permission-grant-${it}", true)
            ) {
                return@lambda false
            }

            return@lambda !shouldShowRequestPermissionRationale(it)
        }

        AuronRuntimeManager.goToAppSettings = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = "package:${baseContext.packageName}".toUri()
            startActivity(intent)
        }

        val keys = intent.extras?.keySet() ?: setOf()
        AuronRuntimeManager.initialIntention = keys.mapNotNull { key ->
            intent.getStringExtra(key)?.let { value ->
                key to value
            }
        }.toMap()

        AuronRuntimeManager.permissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions: Map<String, Boolean> ->
            CoroutineScope(Dispatchers.Default).launch {
                permissions.map { it.key }.forEach { permissionName ->
                    if (
                        getSharedPreferences("auron:permission-data", MODE_PRIVATE)
                            .getBoolean("initial-permission-grant-$permissionName", true)
                    ) {
                        AuronAndroidRuntimeAppManager.permissionDataPreferencesMutex.withLock {
                            getSharedPreferences("auron:permission-data", MODE_PRIVATE).edit {
                                putBoolean(
                                    "initial-permission-grant-$permissionName", false
                                )
                            }
                        }
                    }
                }

                permissions.firstNotNullOfOrNull {
                    if (it.key.startsWith("auron-fake-permissions:permission-request-uuid-")) {
                        it.key.removePrefix("auron-fake-permissions:permission-request-uuid-")
                    } else null
                }?.let { PermissionManager.onPermissionDecision.emit(it) }
            }
        }

        AuronRuntimeManager.executeInActivity = {
            it(this@MainActivity)
        }

        val setScreenOrientation: (Window.ScreenOrientation, Boolean) -> Unit = { orientation, _ ->
            requestedOrientation = when (orientation) {
                Window.ScreenOrientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Window.ScreenOrientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }

        AuronRuntimeAppManager.mainWindow = Window(
            getScreenOrientation = {
                when (getSystemService(WindowManager::class.java).defaultDisplay.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> Window.ScreenOrientation.Portrait
                    Configuration.ORIENTATION_LANDSCAPE -> Window.ScreenOrientation.Landscape
                    else -> Window.ScreenOrientation.Undefined
                }
            }, setScreenOrientation = setScreenOrientation, unlockScreenOrientation = {
                setScreenOrientation(Window.ScreenOrientation.Undefined, false)
            }
        )

        AuronAndroidRuntimeAppManager.initSetContentLambda = { content ->
            setContent(
                content = {
                    CompositionLocalProvider(LocalWindow provides AuronRuntimeAppManager.mainWindow) {
                        content()
                    }
                }
            )
        }

        AuronRuntimeManager.quitApp = {
            this.finish()
            exitProcess(0)
        }

        mainLink()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }

    override fun onResume() {
        super.onResume()
        App.Callbacks.resume.registered.forEach {
            it.value()
        }
    }

    //TODO: Handle invalid Intents
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val keys = intent.extras?.keySet() ?: setOf()
        if (keys.isEmpty()) return

        val values = keys.associate { it to intent.getStringExtra(it)!! }

        App.Callbacks.newIntention.registered.forEach { registeredHandler ->
            registeredHandler.value(values)
        }
    }
}
