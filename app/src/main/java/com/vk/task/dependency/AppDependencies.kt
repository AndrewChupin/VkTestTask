package com.vk.task.dependency

import android.app.Application
import android.content.Context
import com.vk.task.app.AppDelegate
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NavigationModule::class
    ]
)
interface AppComponent {

    // Injects
    fun inject(appDelegate: AppDelegate)

    // Builders
    fun mainComponentBuilder(): MainComponent.Builder

    // Builder
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application): Builder

        fun build(): AppComponent
    }
}

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

}

@Module
class NavigationModule {

    private val cicerone: Cicerone<Router> = Cicerone.create(Router())

    @Provides
    @Singleton
    fun provideRouter(): Router = cicerone.router

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.navigatorHolder
}