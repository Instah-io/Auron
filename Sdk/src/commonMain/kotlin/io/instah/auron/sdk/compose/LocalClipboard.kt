package io.instah.auron.sdk.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.Clipboard as ComposeClipboard

val LocalClipboard = compositionLocalOf<Clipboard> { error("Clipboard not initialized") }

expect class Clipboard() {
    fun setComposeClipboard(clipboard: ComposeClipboard)
    val composeClipboard: ComposeClipboard

    suspend fun setText(text: String)
}