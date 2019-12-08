package com.vk.core.utils.extensions


fun <Data> List<Data>?.concat(data: List<Data>): List<Data> {
    val newData: MutableList<Data> = mutableListOf()
    this?.let {
        newData.addAll(it)
    }

    newData.addAll(data)
    return newData
}
