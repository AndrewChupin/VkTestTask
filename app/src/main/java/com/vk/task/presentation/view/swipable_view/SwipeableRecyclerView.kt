package com.vk.task.presentation.view.swipable_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.vk.task.presentation.view.swipable_view.stuff.CardHorizontalLayoutManager
import com.vk.task.utils.DirectionType

class SwipeableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    interface Delegate {
        fun onCardDragging(direction: DirectionType, ratio: Float)
        fun onCardSwiped(direction: DirectionType)
    }

    override fun setLayoutManager(manager: LayoutManager?) {
        require(manager is CardHorizontalLayoutManager) {
            "SwipeableRecyclerView allows to use just CardHorizontalLayoutManager adapter"
        }

        super.setLayoutManager(manager)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val safeLm = layoutManager

        if (safeLm == null || safeLm !is CardHorizontalLayoutManager) {
            return super.onInterceptTouchEvent(event)
        }

        if (event.action != MotionEvent.ACTION_DOWN) {
            safeLm.updateScales(event.y)
        }

        return super.onInterceptTouchEvent(event)
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
