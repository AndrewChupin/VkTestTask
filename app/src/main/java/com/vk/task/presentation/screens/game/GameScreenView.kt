package com.vk.task.presentation.screens.game

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.vk.core.utils.view.*
import com.vk.task.R
import com.vk.task.utils.ROBOTO_MEDIUM
import android.graphics.drawable.NinePatchDrawable
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.vk.core.utils.extensions.*
import com.vk.task.data.game.Game
import com.vk.task.presentation.view.swipable_view.stuff.CardHorizontalLayoutManager
import com.vk.task.presentation.view.swipable_view.SwipeableRecyclerView
import com.vk.task.presentation.view.swipable_view.stuff.SwipeableSnapHelper
import com.vk.task.utils.DirectionType


@SuppressLint("ViewConstructor")
class GameScreenView(
	context: Context,
	private val delegate: SwipeableRecyclerView.Delegate
) : ConstraintLayout(context) {

	companion object {
		val TASK_TITLE_ID = View.generateViewId()
		val TASK_NAME_ID = View.generateViewId()
		val LEFT_BUTTON_ID = View.generateViewId()
		val RIGHT_BUTTON_ID = View.generateViewId()
		val CARD_LIST_ID = View.generateViewId()
	}

	lateinit var recycler: SwipeableRecyclerView
	lateinit var gameRuleTitle: TextView
	lateinit var leftButton: Button
	lateinit var rightButton: Button

	private var cardAdapter: PersonCardAdapter = PersonCardAdapter()

	init { initView() }

	private fun initView() {
		// Result Title
		putView(
			view = TextView(context) accept {
				id = TASK_TITLE_ID
				maxLines = 1
				typeface = ROBOTO_MEDIUM
				ellipsize = TextUtils.TruncateAt.END
				isAllCaps = true
				text = context.getString(R.string.task)
				gravity = Gravity.CENTER

				setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TEXT)
				setTextColor(context.compatColor(R.color.text_dark_description))
			},
			params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
		)

		// Text Title
		gameRuleTitle = putView(
			view = TextView(context) accept {
				id = TASK_NAME_ID
				maxLines = 3
				typeface = ROBOTO_MEDIUM
				ellipsize = TextUtils.TruncateAt.END
				gravity = Gravity.CENTER

				setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TITLE)
				setTextColor(context.compatColor(R.color.text_dark_main))
			},
			params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
		)

		leftButton = putView(
			view = Button(context) accept {
				id = LEFT_BUTTON_ID
				isAllCaps = false
				stateListAnimator = null
				typeface = ROBOTO_MEDIUM
				maxLines = 2
				ellipsize = TextUtils.TruncateAt.END

				setTextColor(context.compatColor(R.color.white))
				setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TEXT)

				val ninepatch: NinePatchDrawable
				val image = BitmapFactory.decodeResource(resources, R.drawable.bg_answer_left)
				if (image.ninePatchChunk != null) {
					val chunk = image.ninePatchChunk
					val paddingRectangle = Rect(
						(dp(SPACE_DOUBLE) * 1.5).toInt(),
						dp(SPACE_BASE),
						(dp(SPACE_DOUBLE) * 1.5).toInt(),
						dp(SPACE_ONE_AND_HALF)
					)
					ninepatch = NinePatchDrawable(resources, image, chunk, paddingRectangle, null)
					background = ninepatch
				}
			},
			params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
		)

		rightButton = putView(
			view = Button(context) accept {
				id = RIGHT_BUTTON_ID
				isAllCaps = false
				stateListAnimator = null
				typeface = ROBOTO_MEDIUM
				maxLines = 2
				ellipsize = TextUtils.TruncateAt.END

				setTextColor(context.compatColor(R.color.white))
				setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TEXT)

				val ninepatch: NinePatchDrawable
				val image = BitmapFactory.decodeResource(resources, R.drawable.bg_answer_right)
				if (image.ninePatchChunk != null) {
					val chunk = image.ninePatchChunk
					val paddingRectangle = Rect(
						(dp(SPACE_DOUBLE) * 1.5).toInt(),
						dp(SPACE_BASE),
						(dp(SPACE_DOUBLE) * 1.5).toInt(),
						dp(SPACE_ONE_AND_HALF)
					)
					ninepatch = NinePatchDrawable(resources, image, chunk, paddingRectangle, null)
					background = ninepatch
				}
			},
			params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
		)

		recycler = putView(
			view = SwipeableRecyclerView(context) accept {
				id = CARD_LIST_ID
				adapter = cardAdapter
				layoutManager = CardHorizontalLayoutManager(delegate) accept {
					itemAnimator.apply {
						if (this is DefaultItemAnimator) {
							supportsChangeAnimations = false
						}
					}
				}
				SwipeableSnapHelper().attachToRecyclerView(this)
				overScrollMode = RecyclerView.OVER_SCROLL_NEVER
			},
			params = constraintsParams(MATCH_PARENT, MATCH_CONSTRAINT)
		)

		editConstraints {
			connect(TASK_TITLE_ID, CS_START, CS_PARENT_ID, CS_START)
			connect(TASK_TITLE_ID, CS_END, CS_PARENT_ID, CS_END)
			connect(TASK_TITLE_ID, CS_TOP, CS_PARENT_ID, CS_TOP, dp(SPACE_BASE))

			connect(TASK_NAME_ID, CS_START, CS_PARENT_ID, CS_START)
			connect(TASK_NAME_ID, CS_END, CS_PARENT_ID, CS_END)
			connect(TASK_NAME_ID, CS_TOP, TASK_TITLE_ID, CS_BOTTOM, dp(SPACE_QUARTER))

			connect(LEFT_BUTTON_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM, dp(SPACE_BASE))
			connect(LEFT_BUTTON_ID, CS_START, CS_PARENT_ID, CS_START, dp(SPACE_HALF))
			connect(LEFT_BUTTON_ID, CS_END, RIGHT_BUTTON_ID, CS_START)

			connect(RIGHT_BUTTON_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM, dp(SPACE_BASE))
			connect(RIGHT_BUTTON_ID, CS_END, CS_PARENT_ID, CS_END, dp(SPACE_HALF))
			connect(RIGHT_BUTTON_ID, CS_START, LEFT_BUTTON_ID, CS_END)

			connect(CARD_LIST_ID, CS_TOP, TASK_NAME_ID, CS_BOTTOM, dp(SPACE_BASE))
			connect(CARD_LIST_ID, CS_BOTTOM, LEFT_BUTTON_ID, CS_TOP, dp(SPACE_BASE))

			// Same for RtoL and LtoR
			createHorizontalChain(
				CS_PARENT_ID, ConstraintSet.LEFT,
				CS_PARENT_ID, ConstraintSet.RIGHT,
				intArrayOf(LEFT_BUTTON_ID, RIGHT_BUTTON_ID), null, ConstraintSet.CHAIN_PACKED
			)
		}
	}

	fun inflateButton(direction: DirectionType, ratio: Float) {
		val button = when (direction) {
			DirectionType.LEFT -> leftButton
			DirectionType.RIGHT -> rightButton
		}

		val newScale = 1f + ratio / 4
		button.scaleX = newScale
		button.scaleY = newScale
	}

	fun deflateButton(direction: DirectionType) {
		val button = when (direction) {
			DirectionType.LEFT -> leftButton
			DirectionType.RIGHT -> rightButton
		}

		button.scaleX = 1f
		button.scaleY = 1f
	}

	fun setData(game: Game) {
		gameRuleTitle.text = game.title
		leftButton.text = game.leftShow.name
		rightButton.text = game.rightShow.name
		cardAdapter.calculateDiffs(game.cards)
	}
}
