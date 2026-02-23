package io.instah.auron.sdk.storage

import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty

expect inline operator fun <reified T> PropertyStore.field<T>.getValue(thisRef: Any?, property: KProperty<*>): T

expect inline operator fun <reified T> PropertyStore.field<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T)