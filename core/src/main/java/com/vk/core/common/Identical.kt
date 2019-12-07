package com.vk.core.common


/**
 * Use to specify object with ID
 * This interface using in [BaseIdenticalDiffUtil]
 * @author Andrew Chupin
 */
interface Identical<T> {
    val id: T
}