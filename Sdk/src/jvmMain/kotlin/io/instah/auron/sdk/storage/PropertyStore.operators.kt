package io.instah.auron.sdk.storage

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import java.util.prefs.Preferences
import kotlin.reflect.KProperty

//TODO: Not init preferences each time
val PropertyStore.javaPreferences: Preferences
    get() = Preferences.userNodeForPackage(this::class.java)

actual inline operator fun <reified T> PropertyStore.field<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    return PropertyStore.decodeStringStorageValue(
        default = this.default,
        value = this.parent.javaPreferences.get("${this.parent.name}-${property.name}", "0")
    )
}

actual inline operator fun <reified T> PropertyStore.field<T>.setValue(
    thisRef: Any?,
    property: KProperty<*>,
    value: T
) {
    runBlocking {
        this@setValue.parent.mutex.withLock {
            this@setValue.parent.javaPreferences.put(
                "${this@setValue.parent.name}-${property.name}",
                PropertyStore.encodeStringStorageValue(value)
            )
        }
    }
}