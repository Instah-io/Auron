package io.instah.auron.sdk

object Platform {
    enum class Target {
        Web, Android, Ios, Desktop
    }
}

expect val Platform.target: Platform.Target