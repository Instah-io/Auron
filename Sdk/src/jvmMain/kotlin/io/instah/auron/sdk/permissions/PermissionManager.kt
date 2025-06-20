package io.instah.auron.sdk.permissions

import io.instah.auron.permissions.ConfiguredPermission

@Deprecated("PermissionManager on desktop is not functional (Every permission is granted)")
actual object PermissionManager {
    actual suspend fun requestPermissions(
        vararg permissions: ConfiguredPermission
    ) {}

    actual fun goToAppSettings() {}

    actual fun checkIsPermissionGranted(permission: ConfiguredPermission) = true

    actual fun checkIsManualInterventionRequired(permission: ConfiguredPermission) = false
}