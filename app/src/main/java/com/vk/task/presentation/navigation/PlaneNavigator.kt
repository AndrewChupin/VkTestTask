package com.vk.task.presentation.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.vk.task.R
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject


class PlaneNavigator @Inject constructor(
    activity: FragmentActivity,
    @IdRes private val containerId: Int
) : SupportAppNavigator(activity, activity.supportFragmentManager, containerId) {

    override fun backToUnexisting(screen: SupportAppScreen) {}

    override fun setupFragmentTransaction(
        command: Command?,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction?
    ) {
        currentFragment?.let {
            fragmentTransaction?.setCustomAnimations(
                R.anim.enter_right_to_left,
                R.anim.exit_right_to_left,
                R.anim.pop_enter_left_to_right,
                R.anim.pop_exit_left_to_right
            )
        }
    }
}
