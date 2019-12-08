package com.vk.task.presentation.screens.game

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vk.core.presentation.list.BaseMultiplyAdapter
import com.vk.core.presentation.list.BaseViewHolder
import com.vk.core.utils.core.log
import com.vk.core.utils.extensions.*
import com.vk.core.utils.view.*
import com.vk.task.R
import com.vk.task.data.game.GameCard
import com.vk.task.utils.ROBOTO_MEDIUM
import com.vk.task.utils.SimpleGlideListener


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
    private lateinit var gradient: View

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
        gradient = putView(
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
        personNameText.isVisible = false
        gradient.isVisible = false

        Glide.with(context)
            .load(card.character.image)
            .transform(CenterCrop(), RoundedCorners(dp(SPACE_BASE)))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .addListener(SimpleGlideListener {
                // Glide don't call if dead
                personNameText.isVisible = true
                gradient.isVisible = true
            })
            .into(imageView)

        personNameText.text = card.character.name
    }
}
