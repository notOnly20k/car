package com.paopao.carClean.ui.activity

import android.os.Bundle
import com.paopao.carClean.R
import com.paopao.carClean.bean.User
import com.paopao.carClean.ui.base.BaseActivity
import com.paopao.carClean.ui.base.BaseFragment
import com.paopao.carClean.ui.fragment.MineFragment
import com.paopao.carClean.ui.fragment.OrderFragment
import com.paopao.carClean.ui.fragment.SquareFragment
import com.whynuttalk.foreignteacher.ext.e
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList


class MainActivity : BaseActivity() {

    private val mFragmentList = ArrayList<BaseFragment>()
    private var previousPosition=0
    private var currentPosition=0

    override val contentViewResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentList.add(OrderFragment())
        mFragmentList.add(SquareFragment())
        mFragmentList.add(MineFragment())
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content, mFragmentList[0]).commit()
        bottomBar.setOnTabSelectListener { it->
            run {
                when (it) {
                    R.id.tab_order -> {
                      currentPosition=0
                    }
                    R.id.tab_square -> {
                        currentPosition=1
                    }
                    R.id.tab_mine -> {
                        currentPosition=2
                    }
                }
                changeFragment(currentPosition)
            }
        }

    }

    private fun changeFragment(currentPosition: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        if (currentPosition != previousPosition) {
            transaction.hide(mFragmentList[previousPosition])
            if (!mFragmentList[currentPosition].isAdded) {
                transaction.add(R.id.fl_content, mFragmentList[currentPosition])
            }
            previousPosition=currentPosition
            transaction.show(mFragmentList[currentPosition]).commit()
        }
    }
}
