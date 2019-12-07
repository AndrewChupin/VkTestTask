package com.vk.core.utils.extensions


/**
 * Equal to kotlin.apply with infix
 * @see kotlin.apply
 * @author Andrew Chupin
 */
inline infix fun <T> T.accept(block: T.() -> Unit): T {
    block()
    return this
}


/**
 * Equal to kotlin.apply with infix
 * @see kotlin.apply
 * @author Andrew Chupin
 */
inline infix fun <T, R> T.transform(block: T.() -> R): R {
    return block()
}


/**
 * Equal to
 *
 * foo?.let {
 * 	 DO SOMETHING
 * }
 *
 * @author Andrew Chupin
 */
inline infix fun <T, R> T?.optional(block: (T) -> R): R? {
    return this?.let(block)
}
