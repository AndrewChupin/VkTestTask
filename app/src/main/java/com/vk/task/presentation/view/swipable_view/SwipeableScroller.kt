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
        when (type) {
            ScrollType.CANCEL -> action.update(
                targetView.translationX.toInt(), targetView.translationY.toInt(),
                DEFAULT_SWIPE_DURATION, interpolator
            )
            ScrollType.AUTO -> {
                val distX = when (manager.swipeDirection) {
                    DirectionType.LEFT -> manager.width * 2
                    DirectionType.RIGHT -> -manager.width * 2
                }
                action.update(distX, targetView.translationY.toInt(), DEFAULT_SWIPE_DURATION, interpolator)
            }
            ScrollType.MANUAL -> action.update(
                -targetView.translationX.toInt() * DEFAULT_ACCELERATOR,
                -targetView.translationY.toInt() * DEFAULT_ACCELERATOR,
                DEFAULT_SWIPE_DURATION, interpolator
            )
        }

        if (type != ScrollType.CANCEL) {
            manager.delegate.onStartSwiping(manager.currentPosition)
        }
    }

    override fun onStart() {
        manager.state = SwipeStateType.ANIMATING
    }

    override fun onStop() {}
    override fun onSeekTargetStep(dx: Int, dy: Int, state: RecyclerView.State, action: Action) {}
}
