package com.vk.task.utils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vk.task.extensions.SHORT_TIME_WAIT
import java.util.concurrent.TimeUnit


fun viewById(@IdRes redId: Int): ViewInteraction = onView(withId(redId))


fun viewByText(@StringRes redId: Int): ViewInteraction = onView(withText(getString(redId)))


fun sleep(millis: Long) {
	try {
		TimeUnit.MICROSECONDS.sleep(millis)
	} catch (e: InterruptedException) {
		e.printStackTrace()
	}
}


fun waitPredict(message: String, timeout: Long = SHORT_TIME_WAIT, predict: () -> Boolean) {
	val startTime = System.currentTimeMillis()
	while (predict()) {
		if ((System.currentTimeMillis() - startTime) > TimeUnit.SECONDS.toMillis(timeout)) {
			throw Exception("$message за $timeout секунд")
		}
		sleep(1000)
	}
}
