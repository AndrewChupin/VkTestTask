package com.vk.task.presentation.screens.result

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.core.utils.extensions.*
import com.vk.core.utils.view.*
import com.vk.task.R
import com.vk.task.data.game.GameResult
import com.vk.task.utils.ROBOTO_MEDIUM


class ResultScreenView(context: Context) : ConstraintLayout(context) {

    companion object {
        val RECYCLER_ID = View.generateViewId()
        val GRADIENT_ID = View.generateViewId()
        val TRY_BUTTON_ID = View.generateViewId()
    }

    lateinit var recycler: RecyclerView
    lateinit var tryAgainButton: Button
    lateinit var resultAdapter: ResultAdapter

    private var showAnimator: ViewPropertyAnimator? = null
    private var hideAnimator: ViewPropertyAnimator? = null

    init { initView() }

    private fun initView() {
        val columnCounts = context.resources.getInteger(R.integer.column_count)

        resultAdapter = ResultAdapter(columnCounts)

        // RecyclerView
        recycler = putView(
            view = RecyclerView(context) accept {
                id = RECYCLER_ID
                clipToPadding = false
                layoutManager = GridLayoutManager(context, columnCounts) accept {
                    spanSizeLookup = resultAdapter.spanSizeLoopUp
                }

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            RecyclerView.SCROLL_STATE_DRAGGING -> {
                                if (hideAnimator == null) {
                                    showAnimator = null
                                    hideAnimator = tryAgainButton.hideWithScale()
                                }
                            }
                            RecyclerView.SCROLL_STATE_SETTLING,
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                if (showAnimator == null) {
                                    hideAnimator = null
                                    showAnimator = tryAgainButton.showWithScale()
                                }
                            }
                        }
                    }
                })

                addItemDecoration(GridItemTitleDecoration(columnCounts, dp(SPACE_BASE)))
                adapter = resultAdapter
                overScrollMode = View.OVER_SCROLL_NEVER

                setPadding(dp(SPACE_BASE), dp(SPACE_BASE), dp(SPACE_BASE), dp(75)) // For Button
            },
            params = constraintsParams(MATCH_PARENT, MATCH_CONSTRAINT)
        )

        putView(
            view = View(context) accept {
                id = GRADIENT_ID
                background = context.getDrawable(R.drawable.light_gradient)
            },
            params = constraintsParams(MATCH_PARENT, dp(65))
        )

        tryAgainButton = putView(
            view = Button(context) accept {
                id = TRY_BUTTON_ID
                background = context.getDrawable(R.drawable.selector_gray_button)
                text = context.getString(R.string.try_one_more)
                isAllCaps = false
                typeface = ROBOTO_MEDIUM

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TEXT)
                setPadding(dp(SPACE_QUARTER_LESS))
            }
        )

        // Constraints
        editConstraints {
            connect(RECYCLER_ID, CS_START, CS_PARENT_ID, CS_START)
            connect(RECYCLER_ID, CS_END, CS_PARENT_ID, CS_END)
            connect(RECYCLER_ID, CS_TOP, CS_PARENT_ID, CS_TOP)
            connect(RECYCLER_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM)

            connect(GRADIENT_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM)

            connect(TRY_BUTTON_ID, CS_START, CS_PARENT_ID, CS_START)
            connect(TRY_BUTTON_ID, CS_END, CS_PARENT_ID, CS_END)
            connect(TRY_BUTTON_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM, dp(SPACE_BASE))
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        tryAgainButton.clearAnimation()
        showAnimator = null
        hideAnimator = null
    }

    fun provideData(result: GameResult) = resultAdapter.calculateDiffs(result)
}