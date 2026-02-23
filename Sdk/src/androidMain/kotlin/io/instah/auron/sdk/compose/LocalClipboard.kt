package io.instah.auron.sdk.compose

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard as ComposeClipboard

actual class Clipboard actual constructor() {
    actual fun setComposeClipboard(clipboard: ComposeClipboard) {
        composeClipboardActual = clipboard
    }

    private var composeClipboardActual: ComposeClipboard? = null
    actual val composeClipboard: ComposeClipboard
        get() = composeClipboardActual!!

    actual suspend fun setText(text: String) {
        composeClipboardActual?.setClipEntry(ClipEntry(ClipData.newPlainText("entry", text)))
    }
}