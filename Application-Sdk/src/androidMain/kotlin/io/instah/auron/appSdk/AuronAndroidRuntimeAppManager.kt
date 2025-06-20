package io.instah.auron.appSdk

import androidx.compose.runtime.Composable
import kotlinx.coroutines.sync.Mutex

internal object AuronAndroidRuntimeAppManager {
    var initSetContentLambda: ((@Composable () -> Unit) -> Unit)? = null
    internal val permissionDataPreferencesMutex = Mutex()
}