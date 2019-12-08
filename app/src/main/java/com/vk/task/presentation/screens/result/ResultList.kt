package com.vk.task.presentation.screens.result

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.vk.core.presentation.list.BaseSingleAdapters
import com.vk.core.presentation.list.BaseViewHolder
import com.vk.core.utils.extensions.accept
import com.vk.core.utils.extensions.compatColor
import com.vk.core.utils.extensions.isVisible
import com.vk.core.utils.extensions.putView
import com.vk.core.utils.view.*
import com.vk.task.R
import com.vk.task.data.game.GameAnswer
import com.vk.task.data.game.GameResult
import com.vk.task.utils.ROBOTO_BOLD
import com.vk.task.utils.ROBOTO_MEDIUM
import com.vk.task.utils.ROBOTO_REGULAR
import com.vk.task.utils.SimpleGlideListener


class ResultAdapter(
    private val columnsCount: Int
) : BaseSingleAdapters<GameResult>(diffUtilCallback = ResultDiffUtils()) {

    companion object {
        private const val HEIGHT_MULTIPLIER = 1.25f

        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override var data: GameResult? = null

    val spanSizeLoopUp = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (position) {
                0 -> columnsCount
                else -> 1
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): BaseViewHolder<*> {
        return when (type) {
            TYPE_HEADER -> ResultHeaderViewHolder(
                ResultHeaderView(viewGroup.context) accept {
                    layoutParams = linearParams(MATCH_PARENT, WRAP_CONTENT)
                })

            else -> ResultItemViewHolder(ResultItemView(viewGroup.context) accept {
                val columnWidth = columnWidth(columnsCount, dp(SPACE_BASE))
                val columnHeight = (columnWidth * HEIGHT_MULTIPLIER).toInt()
                layoutParams = linearParams(columnWidth, columnHeight)
            })
        }
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder<*>, position: Int) {
        val safeData = data ?: return
        when (viewHolder) {
            is ResultHeaderViewHolder -> viewHolder.bind(safeData)
            is ResultItemViewHolder -> viewHolder.bind(safeData.answers[position - 1])
        }
    }

    override fun getItemCount(): Int {
        val safeData = data
        return when (safeData == null) {
            true -> 0
            else -> safeData.answers.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position == 0) {
            true -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }
}


// ViewHolders
class ResultHeaderViewHolder(
    val content: ResultHeaderView
) : BaseViewHolder<GameResult>(content) {
    override lateinit var item: GameResult

    override fun bind(item: GameResult) {
        super.bind(item)
        content.setData(item)
    }
}

class ResultItemViewHolder(
    val content: ResultItemView
) : BaseViewHolder<GameAnswer>(content) {

    override lateinit var item: GameAnswer

    override fun bind(item: GameAnswer) {
        super.bind(item)
        content.setData(item)
    }
}


// Views
class ResultItemView(context: Context) : ConstraintLayout(context) {

    companion object {
        val PERSON_IMAGE_ID = View.generateViewId()
        val ACCURACY_ID = View.generateViewId()
        val PERSON_NAME = View.generateViewId()
        val WRONG_ANSWER_ID = View.generateViewId()
        val RIGHT_ANSWER_ID = View.generateViewId()
        val GRADIENT_ID = View.generateViewId()
    }

    private lateinit var image: ImageView
    private lateinit var accuracyText: TextView
    private lateinit var personNameText: TextView
    private lateinit var rightFilmNameText: TextView
    private lateinit var wrongFilmNameText: TextView
    private lateinit var gradient: View

    init { initView() }

    private fun initView() {
        // Image
        image = putView(
            view = ImageView(context) accept {
                id = PERSON_IMAGE_ID
                scaleType = ImageView.ScaleType.CENTER_CROP
            },
            params = constraintsParams(MATCH_PARENT, MATCH_PARENT)
        )

        // Gradient
        gradient = putView(
            view = View(context) accept {
                id = GRADIENT_ID
                background = context.getDrawable(R.drawable.dark_gradient)
            },
            params = constraintsParams(MATCH_PARENT, WRAP_CONTENT)
        )

        // Accuracy Text
        accuracyText = putView(
            view = TextView(context) accept {
                id = ACCURACY_ID
                maxLines = 1
                typeface = ROBOTO_MEDIUM
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TINY)
                setPadding(dp(SPACE_HALF), dp(SPACE_QUARTER), dp(SPACE_HALF), dp(SPACE_QUARTER))
                setTextColor(context.compatColor(R.color.text_light_main))
            }
        )

        // Person Name Text
        personNameText = putView(
            view = TextView(context) accept {
                id = PERSON_NAME
                maxLines = 2
                typeface = ROBOTO_BOLD
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TEXT)
                setTextColor(context.compatColor(R.color.text_light_main))
            },
            params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
        )

        // Text Wrong Name of Film
        wrongFilmNameText = putView(
            view = TextView(context) accept {
                id = WRONG_ANSWER_ID
                textSize = FONT_SIZE_TINY
                maxLines = 1
                typeface = ROBOTO_REGULAR
                ellipsize = TextUtils.TruncateAt.END
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TINY)
                setTextColor(context.compatColor(R.color.text_dark_description))
            },
            params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
        )

        // Text Right Name of Film
        rightFilmNameText = putView(
            view = TextView(context) accept {
                id = RIGHT_ANSWER_ID
                maxLines = 1
                typeface = ROBOTO_REGULAR
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_DESCRIPTION)
            },
            params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
        )

        editConstraints {
            connect(PERSON_IMAGE_ID, CS_START, CS_PARENT_ID, CS_START)
            connect(PERSON_IMAGE_ID, CS_TOP, CS_PARENT_ID, CS_TOP)
            connect(PERSON_IMAGE_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM)
            connect(PERSON_IMAGE_ID, CS_END, CS_PARENT_ID, CS_END)

            connect(RIGHT_ANSWER_ID, CS_START, CS_PARENT_ID, CS_START, dp(SPACE_TINY))
            connect(RIGHT_ANSWER_ID, CS_END, CS_PARENT_ID, CS_END, dp(SPACE_TINY))
            connect(RIGHT_ANSWER_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM, dp(SPACE_QUARTER_LESS))

            connect(WRONG_ANSWER_ID, CS_START, CS_PARENT_ID, CS_START, dp(SPACE_BASE))
            connect(WRONG_ANSWER_ID, CS_END, CS_PARENT_ID, CS_END, dp(SPACE_BASE))
            connect(WRONG_ANSWER_ID, CS_BOTTOM, RIGHT_ANSWER_ID, CS_TOP, dp(SPACE_TINY))

            connect(PERSON_NAME, CS_START, CS_PARENT_ID, CS_START, dp(SPACE_BASE))
            connect(PERSON_NAME, CS_END, CS_PARENT_ID, CS_END, dp(SPACE_BASE))
            connect(PERSON_NAME, CS_BOTTOM, WRONG_ANSWER_ID, CS_TOP)

            connect(ACCURACY_ID, CS_END, CS_PARENT_ID, CS_END)
            connect(ACCURACY_ID, CS_TOP, CS_PARENT_ID, CS_TOP)

            connect(GRADIENT_ID, CS_BOTTOM, CS_PARENT_ID, CS_BOTTOM)
        }
    }


    fun setData(item: GameAnswer) {
        accuracyText.isVisible = false
        personNameText.isVisible = false
        rightFilmNameText.isVisible = false
        wrongFilmNameText.isVisible = false
        gradient.isVisible = false

        Glide.with(context)
            .load(item.character.image)
            .transform(CenterCrop(), RoundedCorners(dp(SPACE_BASE)))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .addListener(SimpleGlideListener {
                // Glide don't call if dead
                accuracyText.isVisible = true
                personNameText.isVisible = true
                rightFilmNameText.isVisible = true
                wrongFilmNameText.isVisible = !item.isRightAnswer
                gradient.isVisible = true
            })
            .into(image)

        personNameText.text = item.character.name
        rightFilmNameText.text = item.rightShow.name

        if (item.isRightAnswer) {
            accuracyText.background = context.getDrawable(R.drawable.shape_corner_angle_right)
            accuracyText.text = context.getString(R.string.right)
            rightFilmNameText.setTextColor(context.compatColor(R.color.text_dark_description))

            wrongFilmNameText.isVisible = false
        } else {
            accuracyText.background = context.getDrawable(R.drawable.shape_corner_angle_wrong)
            accuracyText.text = context.getString(R.string.wrong)

            wrongFilmNameText.isVisible = true
            wrongFilmNameText.text = item.answerShow.name
            rightFilmNameText.setTextColor(context.compatColor(R.color.text_light_main))
        }
    }
}



@SuppressLint("ViewConstructor")
class ResultHeaderView(context: Context) : ConstraintLayout(context) {

    companion object {
        val RESULT_TITLE_ID = View.generateViewId()
        val RESULT_NAME_ID = View.generateViewId()
        val RESULT_COUNTER_ID = View.generateViewId()
    }

    private lateinit var resultTitle: TextView
    private lateinit var resultName: TextView
    private lateinit var resultCounter: TextView

    init { initView() }

    private fun initView() {
        setPadding(0, dp(SPACE_BASE), 0, dp(SPACE_BASE))

        // Result Title
        resultTitle = putView(
            view = TextView(context) accept {
                id = RESULT_TITLE_ID
                maxLines = 1
                typeface = ROBOTO_MEDIUM
                ellipsize = TextUtils.TruncateAt.END
                isAllCaps = true
                text = context.getString(R.string.your_result)
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TEXT)
                setTextColor(context.compatColor(R.color.text_dark_description))
            },
            params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
        )

        // Text Title
        resultName = putView(
            view = TextView(context) accept {
                id = RESULT_NAME_ID
                maxLines = 3
                typeface = ROBOTO_MEDIUM
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TITLE)
                setTextColor(context.compatColor(R.color.text_dark_main))
            },
            params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
        )

        // Text Description
        resultCounter = putView(
            view = TextView(context) accept {
                id = RESULT_COUNTER_ID
                maxLines = 2
                typeface = ROBOTO_MEDIUM
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_TITLE)
                setTextColor(context.compatColor(R.color.text_light_highlight))
            },
            params = constraintsParams(MATCH_CONSTRAINT, WRAP_CONTENT)
        )

        editConstraints {
            connect(RESULT_TITLE_ID, CS_START, CS_PARENT_ID, CS_START)
            connect(RESULT_TITLE_ID, CS_END, CS_PARENT_ID, CS_END)
            connect(RESULT_TITLE_ID, CS_TOP, CS_PARENT_ID, CS_TOP)

            connect(RESULT_NAME_ID, CS_START, CS_PARENT_ID, CS_START)
            connect(RESULT_NAME_ID, CS_END, CS_PARENT_ID, CS_END)
            connect(RESULT_NAME_ID, CS_TOP, RESULT_TITLE_ID, CS_BOTTOM, dp(SPACE_QUARTER))

            connect(RESULT_COUNTER_ID, CS_START, CS_PARENT_ID, CS_START)
            connect(RESULT_COUNTER_ID, CS_END, CS_PARENT_ID, CS_END)
            connect(RESULT_COUNTER_ID, CS_TOP, RESULT_NAME_ID, CS_BOTTOM, dp(SPACE_QUARTER))
        }
    }

    fun setData(data: GameResult) {
        resultCounter.text = context.getString(R.string.counter, data.earnedPoints, data.totalPoints)
        resultName.text = data.title
    }
}
