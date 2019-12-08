package com.vk.task.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vk.core.utils.extensions.accept
import com.vk.task.presentation.screens.main.MainActivity
import com.vk.task.screens.GameScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SmokeTests {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSwipeByBothButtonAndReturn() {
        GameScreen() accept {
            waitListReady()

            assertDisplay()
            assertTitle("Разделите героев сериалов")
            assertItemCount(10)

            openResultWithOnlyLeft() accept {
                waitListReady()

                assertDisplay()
                assertItemCount(11) // + Header
                assertScore(5, 10)
                assertTitle("Разделите героев сериалов")
                clickTryAgain()
            }

            waitListReady()
            assertDisplay()

            assertDisplay()
            assertTitle("Разделите героев сериалов")
            assertItemCount(10)

            openResultWithOnlyRight() accept {
                waitListReady()

                assertDisplay()
                assertItemCount(11) // + Header
                assertScore(5, 10)
                assertTitle("Разделите героев сериалов")
                clickTryAgain()
            }
        }
    }

    @Test
    fun testSwipeByBothSwipeAndReturn() {
        GameScreen() accept {
            waitListReady()

            assertDisplay()
            assertTitle("Разделите героев сериалов")
            assertItemCount(10)

            openResultWithOnlyLeftSwipe() accept {
                waitListReady()

                assertDisplay()
                assertItemCount(11) // + Header
                assertScore(5, 10)
                assertTitle("Разделите героев сериалов")
                clickTryAgain()
            }

            waitListReady()

            assertDisplay()
            assertTitle("Разделите героев сериалов")
            assertItemCount(10)

            openResultWithOnlyRight() accept {
                waitListReady()

                assertDisplay()
                assertItemCount(11) // + Header
                assertScore(5, 10)
                assertTitle("Разделите героев сериалов")
                clickTryAgain()
            }
        }
    }
}