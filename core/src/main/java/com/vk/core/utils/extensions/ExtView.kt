package com.vk.core.utils.extensions

import android.view.View
import android.view.ViewGroup

/**
 * Default LayoutParams is WRAP_CONTENT, WRAP_CONTENT
 * @see ViewGroup.generateDefaultLayoutParams
 */
fun <V: View> ViewGroup.putView(view: V): V {
    addView(view)
    return view
}

fun <V: View> ViewGroup.putView(view: V, params: ViewGroup.LayoutParams): V {
    addView(view, params)
    return view
}

fun View.setPadding(px: Int) = setPadding(px, px, px, px)

@Suppress("RecursivePropertyAccessor")
var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }


@Suppress("RecursivePropertyAccessor")
fun View.hideWithScale(duration: Long = 250, scaleX: Float = 0.5f, scaleY: Float = 0.5f) = this.animate()
    .scaleX(scaleX)
    .scaleY(scaleY)
    .alpha(0f)
    .setDuration(duration)


@Suppress("RecursivePropertyAccessor")
fun View.showWithScale(duration: Long = 250) = this.animate()
    .scaleX(1f)
    .scaleY(1f)
    .alpha(1f)
    .setDuration(duration)
