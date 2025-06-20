package io.instah.auron.permissions

sealed class ConfiguredPermission(
    val permissionCategory: Permission
) {
    data object CAMERA : ConfiguredPermission(Permission.CAMERA)
    data class LOCATION(val precise: Boolean) : ConfiguredPermission(Permission.LOCATION)
    data object SEND_NOTIFICATIONS : ConfiguredPermission(Permission.SEND_NOTIFICATIONS)
    data object REQUEST_APP_INSTALL : ConfiguredPermission(Permission.REQUEST_APP_INSTALL)
    data class CUSTOM(val permissionIds: Set<String>) : ConfiguredPermission(Permission.CUSTOM)
}