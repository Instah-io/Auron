package io.instah.auron.sdk.compose

import androidx.compose.runtime.*

//implement reflection based memory snapshots for after app memory free restore
//inputs and custom keys practically make no sense
@Composable
actual fun <T : Any> rememberSaveable(
    init: () -> T
): T {
    //max radix from the compose source code
    val key = currentCompositeKeyHash.toString(36)

    DisposableEffect(Unit) {
        onDispose {
            ComposeStorageRegistry.remove(key)
        }
    }

    if (ComposeStorageRegistry[key] == null) {
        val newValue = init()
        ComposeStorageRegistry[key] = newValue
        return newValue
    }

    @Suppress("UNCHECKED_CAST")
    return (ComposeStorageRegistry[key] ?: init()) as T
}