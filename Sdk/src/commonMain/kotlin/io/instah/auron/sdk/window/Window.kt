package io.instah.auron.sdk.window

//TODO: Auron feature checks (check is a feature supported on a platform)
//TODO: Window height and width
data class Window(
    private val setTitle: (String) -> Unit = {},
    private var initialTitle: String = ""
) {
    var title = initialTitle
        set(value) {
            setTitle(value)
            field = value
        }
}