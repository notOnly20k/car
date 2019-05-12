package com.paopao.carClean.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.adapter.CarCleanAdapter
import com.paopao.carClean.bean.Car
import com.paopao.carClean.bean.CarOrder
import com.paopao.carClean.bean.CleanType
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseFragment
import com.paopao.carClean.ui.dialog.MyCarDialogFragment
import kotlinx.android.synthetic.main.fragment_list.*

class CarCleanFragment:BaseFragment() {
    private lateinit var carCleanAdapter: CarCleanAdapter
    private var list:List<Car> = emptyList()
    override val contentViewLayoutID: Int
        get() =  R.layout.fragment_list

    override fun onFirstVisibleToUser() {
        carCleanAdapter= CarCleanAdapter(context!!)
        carCleanAdapter.setOnItemClickListener(object :CarCleanAdapter.OnItemClickListener{
            override fun goDetailPage(cleanType: CleanType) {
                MyCarDialogFragment.Builder()
                        .create()
                        .setListener(object :MyCarDialogFragment.SeletDateDialogListener{
                            override fun clickSure(carOrder: CarOrder, dialog: MyCarDialogFragment) {
                                carOrder.price=cleanType.price
                                carOrder.cleanType=cleanType.name
                                appComponent.dbService.insCarOrder(carOrder)
                                        .doOnSubscribe { addDisposable(it) }
                                        .subscribe {
                                            if (it==-1){
                                                Toast.makeText(context,"预约失败",Toast.LENGTH_SHORT).show()
                                            }else{
                                                Toast.makeText(context,"预约成功",Toast.LENGTH_SHORT).show()
                                                dialog.dismiss()
                                            }
                                        }
                            }

                        })
                       .show(childFragmentManager, "date")


            }

        })
        rec.adapter=carCleanAdapter
        rec.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }

        sl.setOnRefreshListener {
            getDate()
        }

        getDate()

    }

    fun getDate() {
        sl.isRefreshing = true
        appComponent.dbService.getCarCleanData()
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    sl.isRefreshing = false
                    carCleanAdapter.setDate(it)
                }

    }

    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }
}