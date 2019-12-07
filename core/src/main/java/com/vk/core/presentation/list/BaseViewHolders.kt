package com.vk.core.presentation.list

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder<Item>(
    view: View
) : RecyclerView.ViewHolder(view), Bindable<Item> {

    @CallSuper
    override fun bind(item: Item) {
        this.item = item
    }
}

