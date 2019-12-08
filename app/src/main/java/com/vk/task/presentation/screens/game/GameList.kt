package com.vk.task.presentation.screens.game

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vk.core.presentation.list.BaseMultiplyAdapter
import com.vk.core.presentation.list.BaseViewHolder
import com.vk.core.utils.diff.BaseIdenticalDiffUtil
import com.vk.core.utils.extensions.accept
import com.vk.core.utils.extensions.compatColor
import com.vk.core.utils.extensions.putView
import com.vk.core.utils.extensions.setPadding
import com.vk.core.utils.view.*
import com.vk.task.R
import com.vk.task.data.game.CharacterInfo
import com.vk.task.data.game.GameCard
import com.vk.task.utils.ROBOTO_BOLD
import com.vk.task.utils.ROBOTO_MEDIUM
import com.vk.task.utils.ext.setBackgroundNinePatch


class PersonCardAdapter : BaseMultiplyAdapter<GameCard, PersonCardViewHolder>() {
    override var data: List<GameCard> = listOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): PersonCardViewHolder {
        return PersonCardViewHolder(
            PersonCardView(viewGroup.context) accept {
                layoutParams = frameParams(MATCH_PARENT, MATCH_PARENT)
            }
        )
    }
}

class PersonCardViewHolder(
    private val content: PersonCardView
) : BaseViewHolder<GameCard>(content) {

    override lateinit var item: GameCard

    override fun bind(item: GameCard) {
        super.bind(item)
        content.setData(item)
    }
}

class PersonCardView(context: Context) : FrameLayout(context) {

    companion object {
        val CARD_IMAGE_ID = View.generateViewId()
        val PERSON_NAME_ID = View.generateViewId()
        val GRADIENT_ID = View.generateViewId()
    }

    init { initView() }

    private lateinit var imageView: ImageView
    private lateinit var personNameText: TextView

    private fun initView() {
        setPadding(dp(SPACE_BASE))

        imageView = putView(
            ImageView(context) accept {
                id = CARD_IMAGE_ID
                scaleType = ImageView.ScaleType.CENTER_CROP
            },
            frameParams(MATCH_PARENT, MATCH_PARENT)
        )

        // Gradient
        putView(
            View(context) accept {
                id = GRADIENT_ID
                background = context.getDrawable(R.drawable.dark_gradient)
            },
            frameParams(MATCH_PARENT, dp(75), Gravity.BOTTOM)
        )

        personNameText = putView(
            TextView(context) accept {
                id = PERSON_NAME_ID
                maxLines = 2
                typeface = ROBOTO_MEDIUM
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER

                setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SIZE_BIG_TITLE)
                setTextColor(context.compatColor(R.color.text_light_main))
            },
            frameParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM ,
                Margins(SPACE_ONE_AND_HALF)
            )
        )
    }

    fun setData(card: GameCard) {
        Glide.with(context)
            .load(card.character.image)
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(dp(SPACE_BASE))))
            .into(imageView)

        personNameText.text = card.character.name
    }
}
