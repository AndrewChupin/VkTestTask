package com.vk.core.presentation.view.state

import androidx.annotation.MainThread
import androidx.arch.core.executor.ArchTaskExecutor
import com.vk.core.common.Disposal
import kotlin.collections.ArrayList

/**
 * Lite alternative for androidx.LiveData
 * </br>
 * @author Andrew Chupin
 */
@MainThread
class ViewProperty<T> internal constructor(
    initValue: T?
) {
    companion object {
        fun <T> create(value: T?): ViewProperty<T> = ViewProperty(value)

        internal fun assertMainThread(methodName:String) {
            check(ArchTaskExecutor.getInstance().isMainThread) {
                ("Cannot invoke $methodName on a background thread")
            }
        }
    }

    private val observers = ArrayList<PropertyDisposal<T?>>()
    private var value: T? = initValue

    fun currentValue(): T? = value

    infix fun observe(onChange: (T?) -> Unit): Disposal {
        assertMainThread("observe")

        val disposal = PropertyDisposal(onChange)
        observers.add(disposal)

        onChange(value)

        return disposal
    }

    // TODO-1: Create MutableViewProperty
    fun update(newValue: T?) {
        assertMainThread("update")

        if (value == newValue) {
            return
        }

        value = newValue
        val safeValue = value

        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val disposal = iterator.next()
            when (disposal.isDisposed()) {
                true -> iterator.remove()
                else -> disposal.observer?.invoke(safeValue)
            }
        }
    }

    open class PropertyDisposal<T>(fn: ((T?) -> Unit)) : Disposal {

        private var isDisposed: Boolean = false
        internal var observer: ((T?) -> Unit)? = fn

        override fun dispose() {
            if (isDisposed) {
                return
            }

            isDisposed = true
            observer = null
        }

        override fun isDisposed(): Boolean = isDisposed
    }
}