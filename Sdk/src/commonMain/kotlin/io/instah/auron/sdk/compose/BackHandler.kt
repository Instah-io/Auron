package io.instah.auron.sdk.compose

import androidx.compose.runtime.Composable

//Without any parameters it just blocks back press handling
@Composable
expect fun BackHandler(
    enabled: Boolean = true,
    action: () -> Unit = {}
)