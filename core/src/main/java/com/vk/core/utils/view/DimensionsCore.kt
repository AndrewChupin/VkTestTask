package com.vk.core.utils.view

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

const val SPACE_HUGE = 48
const val SPACE_DOUBLE = 32
const val SPACE_ONE_AND_HALF = 24
const val SPACE_BASE = 16
const val SPACE_QUARTER_LESS = 12
const val SPACE_HALF = 8
const val SPACE_QUARTER = 4
const val SPACE_TINY = 2

const val FONT_SIZE_BIG_TITLE = 28f
const val FONT_SIZE_TITLE = 24f
const val FONT_SIZE_BIG_TEXT = 20f
const val FONT_SIZE_TEXT = 16f
const val FONT_SIZE_DESCRIPTION = 14f
const val FONT_SIZE_TINY = 12f

val screenWidth: Int by lazy {
    val metrics = Resources.getSystem().displayMetrics
    metrics.widthPixels
}

val screenHeight: Int by lazy {
    val metrics = Resources.getSystem().displayMetrics
    metrics.heightPixels
}

val density: Float by lazy {
    val metrics = Resources.getSystem().displayMetrics
    metrics.densityDpi / 160f
}


@Suppress("NOTHING_TO_INLINE")
inline fun dp(dp: Int): Int = (dp * density).roundToInt()
@Suppress("NOTHING_TO_INLINE")
fun px(px: Float): Float = px / density

fun columnWidth(count: Int, padding: Int): Int {
    if (count < 1) {
        return 0
    }

    val summaryPadding = padding * (count + 1)
    return (screenWidth - summaryPadding) / count
}