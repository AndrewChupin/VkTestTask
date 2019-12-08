package com.vk.task.presentation.screens.result

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.vk.core.presentation.view.Content
import com.vk.core.presentation.view.FragmentStateful
import com.vk.core.utils.extensions.accept
import com.vk.task.app.AppInjector
import ru.terrakok.cicerone.android.support.SupportAppScreen


class ResultScreen: SupportAppScreen() {
    override fun getFragment(): Fragment = ResultFragment.newInstance()
}

class ResultFragment : FragmentStateful<ResultDispatcher, ResultViewState, ResultScreenView>() {

    companion object {
        fun newInstance(): ResultFragment = ResultFragment()
    }

    override fun onInject() = AppInjector.injectFragment(this)

    override fun getContent(context: Context): Content<ResultScreenView> = Content.Layout(ResultScreenView(context))

    override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
        content accept {
            tryAgainButton.setOnClickListener {
                dispatcher.tryAgain()
            }
        }
    }

    override fun render(state: ResultViewState) {
        state accept {
            game bind content::provideData
        }
    }

    override fun onBackClick(): Boolean {
        dispatcher.tryAgain()
        return true
    }
}