package com.vk.task.presentation.screens.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.task.app.AppInjector
import com.vk.task.presentation.screens.game.CardScreen
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector.injectActivity(this, android.R.id.content)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            router.newRootChain(CardScreen())
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
