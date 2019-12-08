package com.vk.task.extensions

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.vk.task.utils.sleep
import java.util.concurrent.TimeUnit


private const val LEAST_DISPLAYED_TIME = 90
const val LONG_TIME_WAIT = 30L
const val SHORT_TIME_WAIT = 15L

fun ViewInteraction.assertText(text: String) = check(ViewAssertions.matches(ViewMatchers.withText(text)))


fun ViewInteraction.click() = perform(ViewActions.click())


fun ViewInteraction.isDisplayed(): Boolean {
	val isDisplayed = booleanArrayOf(true)
	withFailureHandler { _, _ ->
		isDisplayed[0] = false
	}.check(ViewAssertions.matches(ViewMatchers.isDisplayingAtLeast(LEAST_DISPLAYED_TIME)))
	return isDisplayed[0]
}


fun ViewInteraction.hasAlpha(alpha: Float): Boolean {
	val hasAlpha = booleanArrayOf(true)
	withFailureHandler { _, _ ->
		hasAlpha[0] = false
	}.check(ViewAssertions.matches(ViewMatchers.withAlpha(alpha)))
	return hasAlpha[0]
}
