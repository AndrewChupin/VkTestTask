package com.vk.task.utils

import androidx.test.platform.app.InstrumentationRegistry


fun getString(id: Int): String {
    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    return targetContext.resources.getString(id)
}


fun getStringFormat(id: Int, vararg args: String): String {
    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    return targetContext.resources.getString(id, *args)
}
