package com.vk.core.traits

import com.vk.core.utils.core.Functions.emptyErrorFun
import com.vk.core.utils.core.Functions.emptyFun
import com.vk.core.utils.core.Functions.typedDefaultFun
import com.vk.core.common.Trait
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Trait for all Observable types - [Single] [Observable] [Maybe] [Flowable] [Completable]
 *
 * @defaults subscribeOn - Schedulers.io() | observeOn - AndroidSchedulers.mainThread()
 * @author Andrew Chupin
 */
@Trait
interface ReactiveActions {

    /**
     * [CompositeDisposable] keeping all [Disposable] from [bindSubscribe]
     * You need set it when implement this interface
     */
    val disposables: CompositeDisposable

    val errorHandler: (Throwable) -> Boolean get() = emptyErrorFun

    /**
     * Automatically puts [Disposable] inside [disposables]
     */
    fun Disposable.bindDisposable() {
        disposables.add(this)
    }

    fun Disposable.tryToAddDisposable() = disposables.add(this)

    /**
     * Helper for [Single]
     * @default [scheduler] - Schedulers.io()
     * @default [onSuccess] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     */
    fun <T> Single<T>.bindSubscribe(
        scheduler: Scheduler = Schedulers.io(),
        onSuccess: (T) -> Unit = ::typedDefaultFun,
        onError: ((Throwable) -> Unit)? = null
    ) = subscribeOn(scheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, {
            if (errorHandler(it)) {
                return@subscribe
            }

            onError?.invoke(it)
        })
        .bindDisposable()

    /**
     * Helper for [Single]
     * @default [scheduler] - Schedulers.io()
     * @default [onSuccess] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     */
    fun <T> Maybe<T>.bindSubscribe(
        scheduler: Scheduler = Schedulers.io(),
        onSuccess: (T) -> Unit = ::typedDefaultFun,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: () -> Unit = emptyFun
    ) = subscribeOn(scheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, {
            if (errorHandler(it)) {
                return@subscribe
            }

            onError?.invoke(it)
        }, onComplete)
        .bindDisposable()

    /**
     * Helper for [Observable]
     * @default [scheduler] - Schedulers.io()
     * @default [onNext] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     * @default [onComplete] - () -> Unit
     */
    fun <T> Observable<T>.bindSubscribe(
        scheduler: Scheduler = Schedulers.io(),
        onNext: (T) -> Unit = ::typedDefaultFun,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: () -> Unit = emptyFun
    ) = subscribeOn(scheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, {
            if (errorHandler(it)) {
                return@subscribe
            }

            onError?.invoke(it)
        }, onComplete)
        .bindDisposable()

    /**
     * Helper for [Flowable]
     * @default [scheduler] - Schedulers.io()
     * @default [onNext] - (T) -> Unit
     * @default [onError] - (Throwable) -> Unit
     * @default [onComplete] - () -> Unit
     */
    fun <T> Flowable<T>.bindSubscribe(
        scheduler: Scheduler = Schedulers.io(),
        onNext: (T) -> Unit = ::typedDefaultFun,
        onError: ((Throwable) -> Unit)? = null,
        onComplete: () -> Unit = emptyFun
    ) = subscribeOn(scheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, {
            if (errorHandler(it)) {
                return@subscribe
            }

            onError?.invoke(it)
        }, onComplete)
        .bindDisposable()

    /**
     * Helper for [Completable]
     * @default [scheduler] - Schedulers.io()
     * @default [onSuccess] - () -> Unit
     * @default [onError] - (Throwable) -> Unit
     */
    fun Completable.bindSubscribe(
        scheduler: Scheduler = Schedulers.io(),
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: () -> Unit = emptyFun
    ) = subscribeOn(scheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, {
            if (errorHandler(it)) {
                return@subscribe
            }

            onError?.invoke(it)
        })
        .bindDisposable()

    /**
     * Clear all keeping disposables inside [disposables]
     * You must unbindAll it to avoid memory leaks
     */
    fun clearDisposables() = disposables.clear()
}
