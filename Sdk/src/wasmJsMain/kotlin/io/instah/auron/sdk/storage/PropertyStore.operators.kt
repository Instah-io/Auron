package io.instah.auron.sdk.storage

import kotlinx.browser.window
import kotlin.reflect.KProperty

actual inline operator fun <reified T> PropertyStore.field<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    return PropertyStore.decodeStringStorageValue(
        default = this.default,
        value = window.localStorage.getItem(property.name)
    )
}

actual inline operator fun <reified T> PropertyStore.field<T>.setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
) {
    window.localStorage.setItem(property.name, PropertyStore.encodeStringStorageValue(value))
}