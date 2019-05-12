package com.paopao.carClean.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.adapter.SingleOrderFragmentAdapter
import com.paopao.carClean.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment:BaseFragment(){
    private val fragmentList = ArrayList<BaseFragment>()
    private val titles = ArrayList<String>()

    override val contentViewLayoutID: Int
        get() = R.layout.fragment_order


    override fun onFirstVisibleToUser() {
        titles.add("保养")
        titles.add("我的预约")
        fragmentList.add(CarCleanFragment())
        fragmentList.add(MyOrderFragment())
        vp_order.adapter = SingleOrderFragmentAdapter(childFragmentManager, fragmentList,titles)
        tab_order.setupWithViewPager(vp_order)
        tab_order.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vp_order.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        vp_order.currentItem = 0
        tab_order.getTabAt(0)!!.select()
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