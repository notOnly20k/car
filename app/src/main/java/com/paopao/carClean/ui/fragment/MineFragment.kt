package com.paopao.carClean.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.ui.base.BaseFragment


class MineFragment:BaseFragment(){
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_mine
    }

    override fun onFirstVisibleToUser() {

    }

    override fun onVisibleToUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInvisibleToUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }
}