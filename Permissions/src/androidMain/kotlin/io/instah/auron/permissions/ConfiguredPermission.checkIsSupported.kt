package io.instah.auron.permissions

fun ConfiguredPermission.checkIsSupported() = toPermissionNames().isNotEmpty()