package com.vk.core.utils.view


import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.vk.core.utils.extensions.accept


// BASE
const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

data class Margins(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0
) {
    constructor(side: Int): this(side, side, side, side)
}


// LINEAR LAYOUT
fun linearParams(
    width: Int,
    height: Int,
    margins: Margins = Margins(0)
): LinearLayout.LayoutParams {
    return LinearLayout.LayoutParams(width, height) accept {
        setMargins(
            dp(margins.left),
            dp(margins.top),
            dp(margins.right),
            dp(margins.bottom)
        )
    }
}


// FRAME LAYOUT
fun frameParams(
    width: Int,
    height: Int,
    gravity: Int = Gravity.NO_GRAVITY,
    margins: Margins = Margins(0)
): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(width, height, gravity) accept {
        setMargins(
            dp(margins.left),
            dp(margins.top),
            dp(margins.right),
            dp(margins.bottom)
        )
    }
}


// CONSTRAINTS LAYOUT
const val MATCH_CONSTRAINT = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT

const val CS_START = ConstraintSet.START
const val CS_END = ConstraintSet.END
const val CS_TOP = ConstraintSet.TOP
const val CS_BOTTOM = ConstraintSet.BOTTOM
const val CS_PARENT_ID = ConstraintSet.PARENT_ID

fun constraintsParams(
    width: Int,
    height: Int,
    margins: Margins = Margins(0)
): ConstraintLayout.LayoutParams {
    return ConstraintLayout.LayoutParams(width, height) accept {
        setMargins(
            dp(margins.left),
            dp(margins.top),
            dp(margins.right),
            dp(margins.bottom)
        )
    }
}

fun ConstraintLayout.editConstraints(actions: ConstraintSet.() -> Unit) {
    val set = ConstraintSet()
    set.clone(this)
    actions(set)
    set.applyTo(this)
}