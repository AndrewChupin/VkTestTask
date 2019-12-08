package com.vk.task.presentation.view.swipable_view

import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.vk.task.utils.DirectionType

enum class ScrollType {
    AUTO,
    MANUAL,
    CANCEL
}

class SwipeableScroller(
    private val type: ScrollType,
    private val manager: CardHorizontalLayoutManager
) : RecyclerView.SmoothScroller() {

    companion object {
        const val DEFAULT_SWIPE_DURATION = 200
        const val DEFAULT_ACCELERATOR = 5
    }

    private val interpolator = AccelerateInterpolator()

    override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
        val x = targetView.translationX.toInt()
        val y = targetView.translationY.toInt()

        when (type) {
            ScrollType.CANCEL -> action.update(
                x, y,
                DEFAULT_SWIPE_DURATION, interpolator
            )
            ScrollType.AUTO -> action.update(
                getScrollByDistance(manager.swipeDirection), y,
                DEFAULT_SWIPE_DURATION, interpolator
            )
            ScrollType.MANUAL -> action.update(
                -x * DEFAULT_ACCELERATOR, -y * DEFAULT_ACCELERATOR,
                DEFAULT_SWIPE_DURATION, interpolator
            )
        }
    }

    override fun onStart() {
        manager.state = SwipeStateType.ANIMATING
    }

    private fun getScrollByDistance(direction: DirectionType): Int {
        return when (direction) {
            DirectionType.LEFT -> manager.width * 2
            DirectionType.RIGHT -> -manager.width * 2
        }
    }

    override fun onStop() {}
    override fun onSeekTargetStep(dx: Int, dy: Int, state: RecyclerView.State, action: Action) {}
}
