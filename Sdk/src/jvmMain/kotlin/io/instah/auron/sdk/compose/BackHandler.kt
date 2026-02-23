package io.instah.auron.sdk.compose

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(
    enabled: Boolean,
    action: () -> Unit
) {}