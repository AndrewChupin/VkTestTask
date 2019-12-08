package com.vk.task.screens

import com.vk.task.extensions.assertText
import com.vk.task.extensions.click
import com.vk.task.extensions.isDisplayed
import com.vk.task.presentation.screens.result.ResultHeaderView
import com.vk.task.presentation.screens.result.ResultScreenView
import com.vk.task.utils.getCountFromRecyclerView
import com.vk.task.utils.viewById
import com.vk.task.utils.waitPredict


class ResultScreen {

    private val list = viewById(ResultScreenView.RECYCLER_ID)
    private val tryAgainButton = viewById(ResultScreenView.TRY_BUTTON_ID)
    private val header = ResultHeaderItem()

    fun waitListReady() {
        waitPredict(message = "Не удалось дождаться списка результата") {
            getCountFromRecyclerView() > 0
        }
    }

    fun assertDisplay(isVisible: Boolean = true) {
        assert(list.isDisplayed() == isVisible)
        assert(tryAgainButton.isDisplayed() == isVisible)
        header.assertDisplay()
    }

    fun assertItemCount(count: Int) {
        assert(getCountFromRecyclerView() == count)
    }

    fun assertScore(earned: Int, total: Int) {
        header.counter.assertText("$earned / $total")
    }

    fun assertTitle(title: String) {
        header.title.assertText(title)
    }

    fun clickTryAgain() {
        tryAgainButton.click()
    }
}


class ResultHeaderItem {

    internal val title = viewById(ResultHeaderView.RESULT_NAME_ID)
    internal val counter = viewById(ResultHeaderView.RESULT_COUNTER_ID)

    fun assertDisplay(isVisible: Boolean = true) {
        assert(title.isDisplayed() == isVisible)
        assert(counter.isDisplayed() == isVisible)
    }
}