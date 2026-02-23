package io.instah.auron.sdk.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import java.awt.datatransfer.StringSelection
import androidx.compose.ui.platform.Clipboard as ComposeClipboard

actual class Clipboard actual constructor() {
    actual fun setComposeClipboard(clipboard: Clipboard) {
        composeClipboardActual = clipboard
    }

    private var composeClipboardActual: ComposeClipboard? = null
    actual val composeClipboard: Clipboard
        get() = composeClipboardActual!!

    @OptIn(ExperimentalComposeUiApi::class)
    actual suspend fun setText(text: String) {
        composeClipboardActual?.setClipEntry(ClipEntry(StringSelection(text)))
    }
}