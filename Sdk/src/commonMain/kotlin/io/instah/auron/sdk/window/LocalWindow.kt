package io.instah.auron.sdk.window

import androidx.compose.runtime.compositionLocalOf

val LocalWindow = compositionLocalOf<Window> {
    error("Value not provided")
}