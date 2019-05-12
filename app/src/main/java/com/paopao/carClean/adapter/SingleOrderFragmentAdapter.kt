package com.paopao.carClean.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.paopao.carClean.ui.base.BaseFragment


class SingleOrderFragmentAdapter(val fm: FragmentManager, val list: List<BaseFragment>,val titles: List<String>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return list.size
    }

}
