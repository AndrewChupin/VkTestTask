package com.vk.task.utils.ext

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.NinePatchDrawable
import android.view.View
import androidx.annotation.DrawableRes


fun View.setBackgroundNinePatch(@DrawableRes resId: Int, padding: Rect = Rect()) {
    val ninepatch: NinePatchDrawable
    val image = BitmapFactory.decodeResource(resources, resId)
    if (image.ninePatchChunk != null) {
        val chunk = image.ninePatchChunk
        ninepatch = NinePatchDrawable(resources, image, chunk, padding, null)
        background = ninepatch
    }
}
