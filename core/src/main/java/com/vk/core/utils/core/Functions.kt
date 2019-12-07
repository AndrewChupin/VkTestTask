package com.vk.core.utils.core


object Functions {
    val emptyFun: () -> Unit = {}

    val emptyErrorFun: (Throwable) -> Boolean = { error ->
        log(error)
        false
    }

    @Suppress("UNUSED_PARAMETER")
    fun <T> typedDefaultFun(t: T) {}
}