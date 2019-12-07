package com.vk.core.utils.diff

import com.vk.core.common.Identical


open class BaseDiffUtil<Type> : SingleDiffUtil<List<Type>>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = itemOld ?: return false
        val new = itemNew ?: return false
        return old[oldItemPosition].hashCode() == new[newItemPosition].hashCode()
    }

    override fun getOldListSize(): Int {
        val old = itemOld ?: return 0
        return old.size
    }

    override fun getNewListSize(): Int {
        val new = itemNew ?: return 0
        return new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = itemOld ?: return false
        val new = itemNew ?: return false
        return old[oldItemPosition] == new[newItemPosition]
    }
}


open class BaseIdenticalDiffUtil<Type : Identical<Id>, Id> : SingleDiffUtil<List<Type>>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = itemOld ?: return false
        val new = itemNew ?: return false
        return old[oldItemPosition].id == new[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        val old = itemOld ?: return 0
        return old.size
    }

    override fun getNewListSize(): Int {
        val new = itemNew ?: return 0
        return new.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = itemOld ?: return false
        val new = itemNew ?: return false
        return old[oldItemPosition] == new[newItemPosition]
    }
}