package com.vk.task.presentation.screens.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.vk.core.presentation.view.Content
import com.vk.core.presentation.view.FragmentStateful
import com.vk.core.utils.extensions.accept
import com.vk.task.app.AppInjector
import com.vk.task.presentation.view.swipable_view.SwipeableRecyclerView
import com.vk.task.utils.DirectionType
import ru.terrakok.cicerone.android.support.SupportAppScreen


class CardScreen: SupportAppScreen() {
    override fun getFragment(): Fragment = GameFragment.newInstance()
}

class GameFragment : FragmentStateful<CardDispatcher, CardViewState, GameScreenView>(), SwipeableRecyclerView.Delegate {

    companion object {
        fun newInstance(): GameFragment = GameFragment()
    }

    override fun onInject() = AppInjector.injectFragment(this)

    override fun getContent(context: Context): Content<GameScreenView> = Content.Layout(
        GameScreenView(context, this)
    )

    override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
        content accept {
            leftButton.setOnClickListener {
                recycler.swipeLeft()
            }

            rightButton.setOnClickListener {
                recycler.swipeRight()
            }
        }
    }

    override fun onCardDragging(direction: DirectionType, ratio: Float) {
        content.inflateButton(direction, ratio)
    }

    override fun onCardSwiped(direction: DirectionType) {
        dispatcher.swipedNextCard(direction)
        content.deflateButton(direction)
    }

    override fun render(state: CardViewState) {
        state accept {
            game bind content::setData
        }
    }
}