package io.instah.auron.permissions

fun Permission.toAndroidPermissionNamesAndFeatures(): Pair<Set<String>, Set<String>> = when (this) {
    Permission.CAMERA -> setOf("android.permission.CAMERA") to setOf("android.hardware.camera")
    Permission.LOCATION -> setOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION") to emptySet()
    Permission.SEND_NOTIFICATIONS -> setOf("android.permission.POST_NOTIFICATIONS") to emptySet()
    Permission.REQUEST_APP_INSTALL -> setOf("android.permission.REQUEST_INSTALL_PACKAGES") to emptySet()
    Permission.CUSTOM -> emptySet<String>() to emptySet()
}