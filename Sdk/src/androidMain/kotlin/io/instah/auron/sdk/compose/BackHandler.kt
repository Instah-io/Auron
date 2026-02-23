package io.instah.auron.sdk.compose

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(
    enabled: Boolean,
    action: () -> Unit
) = BackHandler(enabled,action)