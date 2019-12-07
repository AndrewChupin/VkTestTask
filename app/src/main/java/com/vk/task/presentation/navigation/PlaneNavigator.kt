package com.vk.task.presentation.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject


class PlaneNavigator @Inject constructor(
    activity: FragmentActivity,
    @IdRes private val containerId: Int
) : SupportAppNavigator(activity, activity.supportFragmentManager, containerId) {

    override fun backToUnexisting(screen: SupportAppScreen) {}

}
