package com.vk.task.presentation.view.swipable_view.stuff

import android.graphics.PointF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.vk.core.utils.extensions.optional
import com.vk.core.utils.view.MATCH_PARENT
import com.vk.task.presentation.view.swipable_view.SwipeableRecyclerView
import com.vk.task.utils.DirectionType
import kotlin.math.abs
import kotlin.math.min


enum class SwipeStateType {
    STATIC, DRAGGING, ANIMATING, RESTORING
}


class CardHorizontalLayoutManager(
    private val cardStackListener: SwipeableRecyclerView.Delegate
) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    companion object {
        private const val DEFAULT_ITEM_CACHE = 3
        private const val DEFAULT_PREVIOUS_SCALE = 0.9f
    }

    private var currentX = 0
    private var currentScale = 0.0f

    internal var state = SwipeStateType.STATIC
    internal var currentPosition = 0
    internal var targetPosition = RecyclerView.NO_POSITION

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
                targetPosition == RecyclerView.NO_POSITION || targetPosition == currentPosition -> {
                    state = SwipeStateType.STATIC
                    targetPosition = RecyclerView.NO_POSITION
                }
                currentPosition < targetPosition -> smoothScrollTo(targetPosition)
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

    internal fun updateScales(y: Float) {
        if (currentPosition < itemCount) {
            val view = findViewByPosition(currentPosition)
            if (view != null) {
                val half = height / 2.0f
                currentScale = -(y - half - view.top.toFloat()) / half
            }
        }
    }

    private fun onChange(recycler: RecyclerView.Recycler) {
        if (isSwipeCompleted()) {
            currentCard optional { card ->
                removeAndRecycleView(card, recycler)
            }

            state = SwipeStateType.RESTORING
            currentPosition++
            currentX = 0
            if (currentPosition == targetPosition) {
                targetPosition = RecyclerView.NO_POSITION
            }

            cardStackListener.onCardSwiped(getScrollDirection())
        }

        detachAndScrapAttachedViews(recycler)

        val parentTop = paddingTop
        val parentLeft = paddingLeft
        val parentRight = width - paddingLeft
        val parentBottom = height - paddingBottom

        var i = currentPosition

        while (i < currentPosition + DEFAULT_ITEM_CACHE && i < itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child, 0)
            measureChildWithMargins(child, 0, 0)
            layoutDecoratedWithMargins(child, parentLeft, parentTop, parentRight, parentBottom)

            resetTranslation(child)
            resetScale(child)
            resetRotation(child)

            if (i == currentPosition) {
                updateTranslation(child)
                resetScale(child)
                updateRotation(child)
            } else {
                val currentIndex = i - currentPosition
                updateScale(child, currentIndex)
                resetRotation(child)
            }
            i++
        }

        if (state == SwipeStateType.DRAGGING) {
            cardStackListener.onCardDragging(getScrollDirection(), getCurrentRatio())
        }
    }


    // Transition
    private fun updateTranslation(view: View) {
        view.translationX = currentX.toFloat()
    }

    private fun resetTranslation(view: View) {
        view.translationX = 0.0f
    }


    // Scale
    private fun updateScale(view: View, index: Int) {
        val nextIndex = index - 1
        val currentScale = 1.0f - index * (1.0f - DEFAULT_PREVIOUS_SCALE)
        val nextScale = 1.0f - nextIndex * (1.0f - DEFAULT_PREVIOUS_SCALE)
        val targetScale = currentScale + (nextScale - currentScale) * getCurrentRatio()

        view.scaleX = targetScale
        view.scaleY = targetScale
    }

    private fun resetScale(view: View) {
        view.scaleX = 1.0f
        view.scaleY = 1.0f
    }


    // Rotation
    private fun updateRotation(view: View) {
        val degree = currentX * 30.0f / width * currentScale // card degree
        view.rotation = degree
    }

    private fun resetRotation(view: View) {
        view.rotation = 0.0f
    }


    // Scrolls
    private fun canScrollToPosition(position: Int, itemCount: Int): Boolean {
        if (currentPosition < position || position < 0 || itemCount < position) {
            return false
        }

        return state == SwipeStateType.STATIC
    }

    private fun smoothScrollTo(position: Int) {
        currentScale = 0.0f
        targetPosition = position

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

    private fun getCurrentRatio(): Float {
        val ratio = abs(currentX) / (width / 2.0f)
        return min(ratio, 1.0f)
    }

    private fun isSwipeCompleted(): Boolean {
        if (state != SwipeStateType.ANIMATING || currentPosition >= targetPosition) {
            return false
        }

        if (width >= abs(currentX)) {
            return false
        }

        return true
    }

    private fun isScrollBlocked(): Boolean = state == SwipeStateType.RESTORING || currentPosition == itemCount
}
