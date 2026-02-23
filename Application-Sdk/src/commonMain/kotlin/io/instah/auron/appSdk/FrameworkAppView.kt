package io.instah.auron.appSdk

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.instah.auron.sdk.compose.Clipboard
import io.instah.auron.sdk.compose.LocalClipboard
import io.instah.auron.sdk.language.TranslationManager

@Composable
internal fun FrameworkAppView(
    content: @Composable () -> Unit
) {
    val composeClipboard = androidx.compose.ui.platform.LocalClipboard.current

    CompositionLocalProvider(LocalClipboard provides Clipboard().apply { setComposeClipboard(composeClipboard) }) {
        AnimatedContent(
            targetState = TranslationManager.currentLanguage,
            transitionSpec = {
                if (initialState == null) {
                    fadeIn(tween(0)) togetherWith
                            fadeOut(tween(0))
                } else fadeIn() togetherWith fadeOut()
            }
        ) {
            content()
        }
    }
}