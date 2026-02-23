package io.instah.auron.sdk.storage

import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json

//TODO: Add a property store version system
abstract class PropertyStore(
    val name: String = this::class.simpleName!!
) {
    val mutex = Mutex()

    inner class field<T>(
        val default: T
    ) {
        var parent = this@PropertyStore
    }

    companion object {
        inline fun <reified T> decodeStringStorageValue(
            default: T,
            value: String? = "0"
        ): T {
            if ((value ?: "0") == "0") {
                return default
            }

            if ((value ?: "0") == "1") {
                return null as T
            }

            //value is 2 then
            return Json.decodeFromString<T>((value ?: "0").drop(1))
        }

        inline fun <reified T> encodeStringStorageValue(
            value: T
        ): String = if (value == null) {
            "1"
        } else {
            "2" + Json.encodeToString(value)
        }
    }
}