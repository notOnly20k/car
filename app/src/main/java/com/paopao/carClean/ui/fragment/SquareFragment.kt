package com.paopao.carClean.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.adapter.SquareAdapter
import com.paopao.carClean.bean.News
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.activity.SquareDetailActivity
import com.paopao.carClean.ui.base.BaseFragment
import com.paopao.carClean.util.ActivityUtil
import com.whynuttalk.foreignteacher.ext.e
import kotlinx.android.synthetic.main.fragment_list.*


class SquareFragment : BaseFragment(), SquareAdapter.OnItemClickListener {
    override fun goDetailPage(news: News) {
        ActivityUtil.create(context!!).go(SquareDetailActivity::class.java).put("news",news).start()
    }

    lateinit var squareAdapter: SquareAdapter
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_list


    override fun onFirstVisibleToUser() {
        squareAdapter = SquareAdapter(context!!)
        squareAdapter.setOnItemClickListener(this)
        rec.adapter = squareAdapter
        rec.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
        sl.setOnRefreshListener {
            getDate()
        }
        getDate()

    }

    fun getDate() {
        sl.isRefreshing = true
        appComponent.dbService.getGoodsData()
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    sl.isRefreshing = false
                    squareAdapter.setDate(it)
                }
    }

    override fun onVisibleToUser() {
        logger.e { "onVisibleToUser" }
    }

    override fun onInvisibleToUser() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }
}