package io.instah.auron.appSdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import io.instah.auron.sdk.window.LocalWindow

actual fun auronApp(
    title: String,
    ui: @Composable () -> Unit
) {
    if (AuronAndroidRuntimeAppManager.initSetContentLambda == null) throw Exception("UI initialization not available")
    AuronAndroidRuntimeAppManager.initSetContentLambda?.invoke({
        CompositionLocalProvider(LocalWindow provides AuronRuntimeAppManager.mainWindow) {
            FrameworkAppView {
                ui()
            }
        }
    })
}
