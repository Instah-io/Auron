package io.instah.auron.permissions

fun ConfiguredPermission.toPermissionNames(): Set<String> = when (this) {
    ConfiguredPermission.CAMERA -> setOf("android.permission.CAMERA")
    is ConfiguredPermission.LOCATION -> if (precise) setOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION") else
        setOf("android.permission.ACCESS_COARSE_LOCATION")
    ConfiguredPermission.SEND_NOTIFICATIONS -> setOf("android.permission.POST_NOTIFICATIONS")
    ConfiguredPermission.REQUEST_APP_INSTALL -> setOf("android.permission.REQUEST_INSTALL_PACKAGES")
    is ConfiguredPermission.CUSTOM -> permissionIds
}