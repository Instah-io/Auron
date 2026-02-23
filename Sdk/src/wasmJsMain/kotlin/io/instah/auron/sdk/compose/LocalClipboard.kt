package io.instah.auron.sdk.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipboardItem
import kotlinx.browser.window
import androidx.compose.ui.platform.Clipboard as ComposeClipboard

actual class Clipboard actual constructor() {
   actual fun setComposeClipboard(clipboard: Clipboard) {
        composeClipboardActual = clipboard
    }

    private var composeClipboardActual: ComposeClipboard? = null
    actual val composeClipboard: Clipboard
        get() = composeClipboardActual!!
    actual suspend fun setText(text: String) {
        composeClipboardActual?.setClipEntry(ClipEntry.withPlainText(text))
    }
}