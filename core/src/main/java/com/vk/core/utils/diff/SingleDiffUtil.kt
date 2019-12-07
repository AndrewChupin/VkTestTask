package com.vk.core.utils.diff

import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil


abstract class SingleDiffUtil<Data> : DiffUtil.Callback() {

    protected var itemOld: Data? = null
    protected var itemNew: Data? = null

    @MainThread
    fun updateItem(newItem: Data) {
        this.itemOld = this.itemNew
        this.itemNew = newItem
    }
}