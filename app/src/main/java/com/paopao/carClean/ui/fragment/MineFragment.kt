package com.paopao.carClean.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.activity.LoginActivity
import com.paopao.carClean.ui.activity.MyCarActivity
import com.paopao.carClean.ui.activity.RegisterActivity
import com.paopao.carClean.ui.base.BaseFragment
import com.paopao.carClean.util.ActivityUtil
import com.whynuttalk.foreignteacher.ext.e
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment:BaseFragment(){
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_mine


    override fun onFirstVisibleToUser() {
        tv_name.text=appComponent.preference.currentUser?.name
        btn_logout.setOnClickListener {
            appComponent.preference.currentUser=null
            appComponent.preference.clear()
            ActivityUtil.create(context!!).go(LoginActivity::class.java).start()
            activity?.finish()
        }

        tv_car.setOnClickListener {
            ActivityUtil.create(context!!).go(MyCarActivity::class.java).start()
        }

        tv_pwd.setOnClickListener {
            ActivityUtil.create(context!!).go(RegisterActivity::class.java).put("edit",1).start()
        }

    }

    override fun onStart() {
        super.onStart()
        tv_name.text=appComponent.preference.currentUser?.name
    }

    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }
}