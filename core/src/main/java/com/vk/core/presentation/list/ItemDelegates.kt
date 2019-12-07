package com.vk.core.presentation.list


interface Bindable<Item> {
    var item: Item
    fun bind(item: Item)
}
