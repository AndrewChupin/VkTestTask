package com.vk.core.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.vk.core.presentation.view.delegates.ActivityActionDelegate
import com.vk.core.presentation.view.delegates.ActivityActionHolder


abstract class BaseActivity : AppCompatActivity(), ActivityActionHolder {

    @LayoutRes
    open val layoutId: Int? = null

    override var delegate: ActivityActionDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId?.let {
            setContentView(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val isReturn = delegate?.onBackClick() ?: run {
            super.onBackPressed()
            return
        }

        if (isReturn) return
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        delegate?.onScreenResult(requestCode, resultCode, data)
    }
}
