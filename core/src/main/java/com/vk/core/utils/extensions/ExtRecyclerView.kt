package com.vk.core.utils.extensions

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vk.core.presentation.list.DataAdapter
import com.vk.core.presentation.list.DiffAdapter


fun <Data, Adapter> Adapter.calculateDiffs(
    new: Data,
    detectMoves: Boolean = false
) where Adapter : RecyclerView.Adapter<*>,
        Adapter : DiffAdapter<Data>,
        Adapter : DataAdapter<Data> {
    diffUtilCallback.updateItem(new)
    val result = DiffUtil.calculateDiff(diffUtilCallback, detectMoves)
    updateData(new)
    result.dispatchUpdatesTo(this)
}
