package com.vk.task.presentation.view.swipable_view

import android.graphics.PointF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.vk.core.utils.extensions.optional
import com.vk.core.utils.view.MATCH_PARENT
import com.vk.task.utils.DirectionType
import kotlin.math.abs
import kotlin.math.min


enum class SwipeStateType {
    STATIC, DRAGGING, ANIMATING, RESTORING
}


class CardHorizontalLayoutManager(
    val delegate: SwipeableRecyclerView.Delegate
) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    companion object {
        private const val DEFAULT_ITEM_CACHE = 3
        private const val DEFAULT_SCALE_REDUCER = 0.1f
        private const val DEFAULT_ROTATION = 2.25f
    }

    private var currentX = 0

    internal var state = SwipeStateType.STATIC
    internal var currentPosition = 0
    internal var innerTargetPosition = RecyclerView.NO_POSITION

    var swipeDirection: DirectionType = DirectionType.LEFT
    val currentCard: View? get() = findViewByPosition(currentPosition)

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        onChange(recycler)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, s: RecyclerView.State): Int {
        if (isScrollBlocked()) {
            return 0
        }

        currentX -= dx
        onChange(recycler)
        return dx
    }

    override fun onScrollStateChanged(scrollState: Int) {
        when (scrollState) {
            RecyclerView.SCROLL_STATE_IDLE -> when {
                innerTargetPosition == RecyclerView.NO_POSITION || innerTargetPosition == currentPosition -> {
                    state = SwipeStateType.STATIC
                    innerTargetPosition = RecyclerView.NO_POSITION
                }
                currentPosition < innerTargetPosition -> smoothScrollTo(innerTargetPosition)
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> state = SwipeStateType.DRAGGING
            RecyclerView.SCROLL_STATE_SETTLING -> Unit // Because don't call every time
        }
    }

    override fun scrollToPosition(position: Int) {
        if (canScrollToPosition(position, itemCount)) {
            currentPosition = position
            requestLayout()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, s: RecyclerView.State?, position: Int) {
        if (canScrollToPosition(position, itemCount)) {
            smoothScrollTo(position)
        }
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        return null
    }

    private fun onChange(recycler: RecyclerView.Recycler) {
        if (isSwipeCompleted()) {
            currentCard optional { card ->
                removeAndRecycleView(card, recycler)
            }

            state = SwipeStateType.RESTORING
            delegate.onSwiped(getScrollDirection(), currentPosition)

            currentPosition++
            currentX = 0
            if (currentPosition == innerTargetPosition) {
                innerTargetPosition = RecyclerView.NO_POSITION
            }
        }

        detachAndScrapAttachedViews(recycler)

        if (state == SwipeStateType.DRAGGING) {
            delegate.onScrolling(getScrollDirection(), getCurrentRatio(), currentPosition)
        }

        var position = currentPosition
        while (position < currentPosition + DEFAULT_ITEM_CACHE && position < itemCount) {
            val child = recycler.getViewForPosition(position)
            addView(child, 0)
            measureChildWithMargins(child, 0, 0)
            layoutDecoratedWithMargins(child, paddingLeft, paddingTop, width - paddingLeft, height - paddingBottom)

            child.translationX = 0.0f
            child.scaleX = 1.0f
            child.scaleY = 1.0f
            child.rotation = 0.0f

            if (position == currentPosition) {
                child.translationX = currentX.toFloat()
                child.scaleX = 1.0f
                child.scaleY = 1.0f
                child.rotation = abs(getCurrentRatio()) * DEFAULT_ROTATION
            } else {
                val diffPosition = position - currentPosition
                updateScale(child, diffPosition)
                child.rotation = 0.0f
            }

            position++
        }
    }

    // Scale
    private fun updateScale(view: View, diff: Int) {
        val newDiff = diff - 1

        val currentScale = 1.0f - diff * DEFAULT_SCALE_REDUCER
        val nextScale = 1.0f - newDiff * DEFAULT_SCALE_REDUCER
        val diffScale = nextScale - currentScale

        val newScale = currentScale + diffScale * getCurrentRatio()

        view.scaleX = newScale
        view.scaleY = newScale
    }


    // Scrolls
    private fun canScrollToPosition(position: Int, itemCount: Int): Boolean {
        if (currentPosition > position || position < 0 || itemCount < position) {
            return false
        }

        return state == SwipeStateType.STATIC
    }

    private fun smoothScrollTo(position: Int) {
        innerTargetPosition = position

        val scroller = SwipeableScroller(ScrollType.AUTO, this)
        scroller.targetPosition = currentPosition
        startSmoothScroll(scroller)
    }


    // Use while scrolling
    private fun getScrollDirection(): DirectionType {
        return when (currentX < 0.0f) {
            true -> DirectionType.LEFT
            else -> DirectionType.RIGHT
        }
    }

    private fun getCurrentRatio(): Float = min(abs(currentX) / (width / 2.0f), 1.0f)

    private fun isSwipeCompleted(): Boolean {
        if (state != SwipeStateType.ANIMATING || currentPosition >= innerTargetPosition) {
            return false
        }

        if (width >= abs(currentX)) {
            return false
        }

        return true
    }

    private fun isScrollBlocked(): Boolean = state == SwipeStateType.RESTORING || currentPosition == itemCount
}
