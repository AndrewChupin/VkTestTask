package com.vk.task.presentation.view.swipable_view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.vk.task.utils.DirectionType

class SwipeableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    interface Delegate {
        fun onScrolling(direction: DirectionType, ratio: Float, position: Int)
        fun onSwiped(direction: DirectionType, position: Int)
        fun onStartSwiping(position: Int)
    }

    override fun setLayoutManager(manager: LayoutManager?) {
        require(manager is CardHorizontalLayoutManager) {
            "SwipeableRecyclerView allows to use just CardHorizontalLayoutManager adapter"
        }

        super.setLayoutManager(manager)
    }

    fun swipeLeft() {
        val safeLm = layoutManager

        if (safeLm == null || safeLm !is CardHorizontalLayoutManager) {
            return
        }

        safeLm.swipeDirection = DirectionType.LEFT
        smoothScrollToPosition(safeLm.currentPosition + 1)
    }

    fun swipeRight() {
        val safeLm = layoutManager

        if (safeLm == null || safeLm !is CardHorizontalLayoutManager) {
            return
        }

        safeLm.swipeDirection = DirectionType.RIGHT
        smoothScrollToPosition(safeLm.currentPosition + 1)
    }
}
