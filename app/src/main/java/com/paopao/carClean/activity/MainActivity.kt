package com.paopao.carClean.activity

import android.os.Bundle
import com.paopao.carClean.R
import com.paopao.carClean.activity.base.BaseActivity


class MainActivity : BaseActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
