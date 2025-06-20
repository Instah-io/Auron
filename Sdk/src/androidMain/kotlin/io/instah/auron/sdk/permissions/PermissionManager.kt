package io.instah.auron.sdk.permissions

import io.instah.auron.permissions.ConfiguredPermission
import io.instah.auron.permissions.toPermissionNames
import io.instah.auron.sdk.AuronRuntimeManager
import io.instah.auron.sdk.Signal
import io.instah.auron.sdk.runtimeManager.checkIsManualPermissionGrantRequired
import io.instah.auron.sdk.runtimeManager.checkIsPermissionGranted
import io.instah.auron.sdk.runtimeManager.goToAppSettings
import io.instah.auron.sdk.runtimeManager.permissionRequestLauncher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual object PermissionManager {
    val onPermissionDecision = Signal<String>()

    @OptIn(ExperimentalUuidApi::class)
    actual suspend fun requestPermissions(
        vararg permissions: ConfiguredPermission
    ) = coroutineScope {
        if (permissions.isEmpty()) throw Exception("You cannot request no permissions")

        val allPermissionsAsked = permissions.map {
            it.toPermissionNames()
        }.flatMap { it }

        if (allPermissionsAsked.isEmpty()) return@coroutineScope

        val requestUUID = Uuid.random().toString()

        val signalAwait = async {
            onPermissionDecision.awaitConditional { it == requestUUID }
        }

        AuronRuntimeManager.permissionRequestLauncher?.launch(
            allPermissionsAsked.toTypedArray() + "auron-fake-permissions:permission-request-uuid-$requestUUID"
        )

        signalAwait.await()
    }

    actual fun goToAppSettings() {
        AuronRuntimeManager.goToAppSettings?.invoke()
    }

    actual fun checkIsPermissionGranted(permission: ConfiguredPermission): Boolean {
        val underlyingPermissions = permission.toPermissionNames()
        if (underlyingPermissions.isEmpty()) return true
        return underlyingPermissions.map {
            AuronRuntimeManager.checkIsPermissionGranted!!(it)
        }.all { it }
    }

    actual fun checkIsManualInterventionRequired(permission: ConfiguredPermission): Boolean {
        val underlyingPermissionsToCheck = permission.toPermissionNames().filter {
            !AuronRuntimeManager.checkIsPermissionGranted!!(it)
        }

        if (underlyingPermissionsToCheck.isEmpty()) return false

        return underlyingPermissionsToCheck.map { permissionName ->
            AuronRuntimeManager.checkIsManualPermissionGrantRequired!!(permissionName)
        }.any { !it }
    }
}