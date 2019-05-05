package com.paopao.carClean.ui.activity

import android.os.Bundle
import com.paopao.carClean.R
import com.paopao.carClean.ui.activity.base.BaseActivity
import com.roughike.bottombar.OnTabSelectListener
import com.whynuttalk.foreignteacher.ext.e
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomBar.setOnTabSelectListener { it->
            logger.e { it }
            run {
                when (it) {
                    R.id.tab_order -> {
                        logger.e { "tab_order" }
                    }
                    R.id.tab_square -> {
                        logger.e { "tab_order" }
                    }
                    R.id.tab_mine -> {
                        logger.e { "tab_order" }
                    }
                }
            }
        }
    }
}
