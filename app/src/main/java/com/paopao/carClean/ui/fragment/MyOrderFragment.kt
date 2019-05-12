package com.paopao.carClean.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.adapter.MyOrderAdapter
import com.paopao.carClean.bean.CarOrder
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list.*

class MyOrderFragment:BaseFragment() {
    private lateinit var myOrderAdapter: MyOrderAdapter
    override val contentViewLayoutID: Int
        get() = R.layout.fragment_list

    override fun onFirstVisibleToUser() {
        myOrderAdapter= MyOrderAdapter(context!!)
        myOrderAdapter.setOnItemClickListener(object:MyOrderAdapter.OnItemClickListener{
            override fun goDetailPage(order: CarOrder) {
                appComponent.dbService.deleteOrder(order.id)
                        .doOnSubscribe { addDisposable(it) }
                        .subscribe {
                            if (it==-1){
                                Toast.makeText(context,"删除失败", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context,"删除成功", Toast.LENGTH_SHORT).show()
                                getDate()
                            }
                        }
            }

        })
        rec.adapter=myOrderAdapter
        rec.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
        sl.setOnRefreshListener {
            getDate()
        }
        getDate()

    }

    fun getDate() {
        sl.isRefreshing = true
        appComponent.dbService.getOrders(appComponent.preference.currentUser!!.id)
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    sl.isRefreshing = false
                    myOrderAdapter.setDate(it)
                }
    }
    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }
}