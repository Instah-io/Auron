package io.instah.auron.sdk.window

//TODO: Auron feature checks (check is a feature supported on a platform)
//TODO: Window height and width
//TODO: Improve the whole screen orientation API
data class Window(
    private val setTitle: (String) -> Unit = {},
    private var initialTitle: String = "",
    private val getScreenOrientation: () -> ScreenOrientation = { ScreenOrientation.Undefined },
    val setScreenOrientation: (orientation: ScreenOrientation, lock: Boolean) -> Unit = { _, _ -> },
    val unlockScreenOrientation: () -> Unit = {}
) {
    var title = initialTitle
        set(value) {
            setTitle(value)
            field = value
        }

    var screenOrientation
        get() = getScreenOrientation()
        set(value) = setScreenOrientation(value, false)

    //TODO: Remove undefined in favour of comparing screen dimensions
    enum class ScreenOrientation {
        Landscape, Portrait, Undefined
    }
}