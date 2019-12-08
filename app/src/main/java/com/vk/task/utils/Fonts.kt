package com.vk.task.utils

import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.vk.task.R
import com.vk.task.app.AppDelegate


val ROBOTO_THIN: Typeface? by lazy {
    ResourcesCompat.getFont(AppDelegate.context, R.font.roboto_thin)
}
val ROBOTO_LIGHT: Typeface? by lazy {
    ResourcesCompat.getFont(AppDelegate.context, R.font.roboto_light)
}
val ROBOTO_REGULAR: Typeface? by lazy {
    ResourcesCompat.getFont(AppDelegate.context, R.font.roboto_regular)
}
val ROBOTO_MEDIUM: Typeface? by lazy {
    ResourcesCompat.getFont(AppDelegate.context, R.font.roboto_medium)
}
val ROBOTO_BOLD: Typeface? by lazy {
    ResourcesCompat.getFont(AppDelegate.context, R.font.roboto_bold)
}