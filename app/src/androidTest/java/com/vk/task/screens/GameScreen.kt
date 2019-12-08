package com.vk.task.screens

import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.contrib.RecyclerViewActions
import com.vk.task.extensions.assertText
import com.vk.task.extensions.click
import com.vk.task.extensions.isDisplayed
import com.vk.task.presentation.screens.game.GameScreenView
import com.vk.task.presentation.screens.game.PersonCardViewHolder
import com.vk.task.utils.getCountFromRecyclerView
import com.vk.task.utils.sleep
import com.vk.task.utils.viewById
import com.vk.task.utils.waitPredict


class GameScreen {

    private val leftButton = viewById(GameScreenView.LEFT_BUTTON_ID)
    private val rightButton = viewById(GameScreenView.RIGHT_BUTTON_ID)
    private val list = viewById(GameScreenView.CARD_LIST_ID)
    private val title = viewById(GameScreenView.TASK_NAME_ID)

    fun waitListReady() {
        waitPredict(message = "Не удалось дождаться начала игры") {
            getCountFromRecyclerView() > 0
        }
    }

    fun assertDisplay(isVisible: Boolean = true) {
        assert(list.isDisplayed() == isVisible)
        assert(rightButton.isDisplayed() == isVisible)
        assert(leftButton.isDisplayed() == isVisible)
        assert(title.isDisplayed() == isVisible)
    }

    fun assertTitle(text: String) {
        title.assertText(text)
    }


    fun assertItemCount(count: Int) {
        assert(getCountFromRecyclerView() == count)
    }

    fun openResultWithOnlyLeft(): ResultScreen {
        (0..9).forEach { _ ->
            sleep(250)
            leftButton.click()
        }

        return ResultScreen()
    }

    fun openResultWithOnlyRight(): ResultScreen {
        (0..9).forEach { _ ->
            sleep(250)
            leftButton.click()
        }

        return ResultScreen()
    }

    fun openResultWithOnlyLeftSwipe(): ResultScreen {
        (0..9).forEach { position ->
            sleep(250)
            list.perform(
                RecyclerViewActions.actionOnItemAtPosition<PersonCardViewHolder>(
                    position,
                    GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT, Press.THUMB)
                )
            )
        }
        return ResultScreen()
    }

    fun openResultWithOnlyRightSwipe(): ResultScreen {
        (0..9).forEach { position ->
            sleep(250)
            list.perform(
                RecyclerViewActions.actionOnItemAtPosition<PersonCardViewHolder>(
                    position,
                    GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT, Press.THUMB)
                )
            )
        }
        return ResultScreen()
    }
}

