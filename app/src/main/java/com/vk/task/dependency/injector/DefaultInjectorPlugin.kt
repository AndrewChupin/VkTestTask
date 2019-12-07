package com.vk.task.dependency.injector

import android.app.Application
import androidx.annotation.IdRes
import com.vk.task.dependency.AppComponent
import com.vk.task.dependency.DaggerAppComponent
import com.vk.task.dependency.MainComponent
import com.vk.task.presentation.main.MainActivity


object DefaultInjectorPlugin : InjectorPlugin {

    override fun representAppComponent(
        application: Application
    ): AppComponent = DaggerAppComponent.builder()
        .withApplication(application)
        .build()

    override fun representMainComponent(
        appComponent: AppComponent,
        activity: MainActivity,
        @IdRes layoutId: Int
    ): MainComponent = appComponent
        .mainComponentBuilder()
        .withActivity(activity)
        .withContainer(layoutId)
        .build()
}
