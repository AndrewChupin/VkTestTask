package com.vk.core.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes


@ColorInt
fun Context.compatColor(@ColorRes id: Int): Int {
    return if (Build.VERSION.SDK_INT >= 23) {
        getColor(id)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(id)
    }
}