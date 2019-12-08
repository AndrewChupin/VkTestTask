package com.vk.task.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.vk.task.dependency.injector.DefaultInjectorPlugin


open class AppDelegate : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        lateinit var context: Context
    }

    override fun onCreate() {
        var hasError = false

        try {
            context = applicationContext
        } catch (ignore: Throwable) {
            hasError = true
        }

        super.onCreate()

        if (hasError) {
            context = applicationContext
        }

        AppInjector.init(this, DefaultInjectorPlugin)
    }
}