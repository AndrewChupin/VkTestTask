package com.vk.task.presentation.screens.result

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class GridItemTitleDecoration(
    private val spansCount: Int,
    private val spaceSize: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        // If title or no position do nothing
        if (position == RecyclerView.NO_POSITION || position == 0) {
            return
        }

        // position - 1 (without title)
        val column = (position - 1) % spansCount

        outRect.left = column * spaceSize / spansCount

        outRect.right = spaceSize - (column + 1) * spaceSize / spansCount

        if (position - 1 >= spansCount) {
            outRect.top = spaceSize
        }
    }
}