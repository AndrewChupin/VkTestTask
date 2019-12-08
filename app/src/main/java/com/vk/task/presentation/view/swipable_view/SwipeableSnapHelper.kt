package com.vk.task.presentation.view.swipable_view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.vk.core.utils.extensions.accept
import kotlin.math.abs


class SwipeableSnapHelper : SnapHelper() {

    companion object {
        private const val DEFAULT_SNAP_THRESHOLD = 0.25f
        private const val DEFAULT_VELOCITY_EDGE = 4000
    }

    private var actualVelocityX = 0

    override fun findSnapView(manager: RecyclerView.LayoutManager): View? {
        require(manager is CardHorizontalLayoutManager) { "SwipeableRecyclerView allows to use just CardHorizontalLayoutManager adapter" }

        val view = manager.currentCard
        if (view?.translationX != 0f) {
            return view
        }

        return null
    }

    override fun findTargetSnapPosition(manager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        require(manager is CardHorizontalLayoutManager) { "SwipeableRecyclerView allows to use just CardHorizontalLayoutManager adapter" }

        actualVelocityX = velocityX
        return manager.currentPosition
    }

    override fun calculateDistanceToFinalSnap(manager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        require(manager is CardHorizontalLayoutManager) { "SwipeableRecyclerView allows to use just CardHorizontalLayoutManager adapter" }

        val currentX = targetView.translationX

        @Suppress("MoveVariableDeclarationIntoWhen")
        val isScrollCanceled = actualVelocityX > DEFAULT_VELOCITY_EDGE
                || DEFAULT_SNAP_THRESHOLD < abs(currentX) / targetView.width

        val scrollType = when (isScrollCanceled) {
            true -> ScrollType.MANUAL
            else -> ScrollType.CANCEL
        }

        if (scrollType == ScrollType.MANUAL) {
            manager.targetPosition = manager.currentPosition + 1
        }

        SwipeableScroller(scrollType, manager) accept {
            targetPosition = manager.currentPosition
            manager.startSmoothScroll(this)
        }

        return IntArray(2)
    }
}
