package com.vk.core.presentation.view

import android.view.View
import androidx.annotation.LayoutRes


sealed class Content<V : View> {

    data class Layout<V : View>(
        val view: V
    ) : Content<V>()

    data class Resource(
        @LayoutRes
        val layoutId: Int
    ) : Content<View>()
}