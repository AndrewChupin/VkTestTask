package com.vk.core.presentation.list

import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.vk.core.utils.diff.BaseDiffUtil
import com.vk.core.utils.diff.SingleDiffUtil


interface DiffAdapter<Data> {
    val diffUtilCallback: SingleDiffUtil<Data>
}

interface DataAdapter<Data> {
    fun updateData(data: Data)
}


abstract class BaseMultiplyAdapter<Data, VH : BaseViewHolder<Data>>(
    override val diffUtilCallback: SingleDiffUtil<List<Data>> = BaseDiffUtil()
) : androidx.recyclerview.widget.RecyclerView.Adapter<VH>(), DiffAdapter<List<Data>>, DataAdapter<List<Data>> {
    abstract var data: List<Data>

    @CallSuper
    override fun updateData(data: List<Data>) {
        this.data = data
    }

    abstract override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): VH

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}


abstract class BaseMultiplyNotTypedAdapter<Data>(
    override val diffUtilCallback: SingleDiffUtil<List<Data>> = BaseDiffUtil()
) : androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder<*>>(), DiffAdapter<List<Data>>,
    DataAdapter<List<Data>> {
    abstract var data: List<Data>

    @CallSuper
    override fun updateData(data: List<Data>) {
        this.data = data
    }

    abstract override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): BaseViewHolder<*>

    abstract override fun onBindViewHolder(viewHolder: BaseViewHolder<*>, position: Int)
}

abstract class BaseSingleAdapters<Data>(
    override val diffUtilCallback: SingleDiffUtil<Data>
) : androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder<*>>(), DiffAdapter<Data>, DataAdapter<Data> {
    abstract var data: Data?

    @CallSuper
    override fun updateData(data: Data) {
        this.data = data
    }

    abstract override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): BaseViewHolder<*>

    abstract override fun onBindViewHolder(viewHolder: BaseViewHolder<*>, position: Int)
}