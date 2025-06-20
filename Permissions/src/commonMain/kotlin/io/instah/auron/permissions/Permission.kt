package io.instah.auron.permissions

enum class Permission {
    LOCATION, CAMERA, SEND_NOTIFICATIONS, REQUEST_APP_INSTALL, CUSTOM
}

/*
enum class Permission(
    val underlyingPermissionNames: Set<String>,
    val underlyingUsedFeatureNames: Set<String> = setOf()
) {
    LOCATION(setOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION")),
    CAMERA(setOf("android.permission.CAMERA"), setOf("android.hardware.camera")),
    SEND_NOTIFICATIONS(setOf("android.permission.POST_NOTIFICATIONS")),
    REQUEST_APP_INSTALL(setOf("android.permission.REQUEST_INSTALL_PACKAGES"))
}*/
