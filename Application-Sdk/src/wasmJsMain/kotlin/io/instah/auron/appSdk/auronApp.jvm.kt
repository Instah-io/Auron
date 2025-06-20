package io.instah.auron.appSdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.instah.auron.sdk.window.LocalWindow
import io.instah.auron.sdk.window.Window
import kotlinx.browser.document

//TODO: Add Api for changing window title
@OptIn(ExperimentalComposeUiApi::class)
actual fun auronApp(
    title: String, ui: @Composable (() -> Unit)
) = CanvasBasedWindow(title) {
    LaunchedEffect(Unit) {
        AuronRuntimeAppManager.mainWindow = Window(
            setTitle = { document.title = it },
            initialTitle = title
        )
    }

    CompositionLocalProvider(LocalWindow provides AuronRuntimeAppManager.mainWindow) {
        FrameworkAppView {
            ui()
        }
    }
}
