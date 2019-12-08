package com.vk.task.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun getCountFromRecyclerView(): Int {
    var count = 0
    object : TypeSafeMatcher<RecyclerView>() {
        override fun matchesSafely(item: RecyclerView): Boolean {
            count = item.adapter!!.itemCount
            return true
        }
        override fun describeTo(description: Description?) {}
    }
    return count
}

fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: // has no item on such position
                return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}

fun withPosition(position: Int): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            view.findViewHolderForAdapterPosition(position) ?: return false
            return true
        }
    }
}
