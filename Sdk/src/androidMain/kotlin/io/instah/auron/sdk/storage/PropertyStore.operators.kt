//TODO: Add dedicated type serializers
package io.instah.auron.sdk.storage

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import io.instah.auron.sdk.AuronRuntimeManager
import io.instah.auron.sdk.runtimeManager.getSharedPreferences
import kotlin.reflect.KProperty
import androidx.core.content.edit

@OptIn(ExperimentalSerializationApi::class)
actual inline operator fun <reified T> PropertyStore.field<T>.getValue(thisRef: Any?, property: KProperty<*>): T =
    runBlocking {
        this@getValue.parent.mutex.withLock {
            val value = AuronRuntimeManager.getSharedPreferences!!("app:${this@getValue.parent.name}").getString(
                property.name, "0"
            )!!

            return@runBlocking PropertyStore.decodeStringStorageValue(this@getValue.default, value)
        }
    }

@OptIn(ExperimentalSerializationApi::class)
actual inline operator fun <reified T> PropertyStore.field<T>.setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
) =
    runBlocking {
        this@setValue.parent.mutex.withLock {
            val result = PropertyStore.encodeStringStorageValue(value)

            AuronRuntimeManager.getSharedPreferences!!("app:${this@setValue.parent.name}").edit {
                putString(
                    property.name, result
                )
            }
        }
    }