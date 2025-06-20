package io.instah.auron.sdk.permissions

import io.instah.auron.permissions.ConfiguredPermission

expect object PermissionManager {
    suspend fun requestPermissions(
        vararg permissions: ConfiguredPermission
    )

    fun goToAppSettings()

    fun checkIsPermissionGranted(permission: ConfiguredPermission): Boolean

    fun checkIsManualInterventionRequired(permission: ConfiguredPermission): Boolean
}