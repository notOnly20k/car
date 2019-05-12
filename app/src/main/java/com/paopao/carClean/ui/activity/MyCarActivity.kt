package com.paopao.carClean.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.paopao.carClean.R
import com.paopao.carClean.adapter.MyCarAdapter
import com.paopao.carClean.bean.Car
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseActivity
import com.paopao.carClean.util.ActivityUtil
import kotlinx.android.synthetic.main.activity_mycar.*

class MyCarActivity:BaseActivity(){
    private lateinit var carAdapter:MyCarAdapter
    override val contentViewResId: Int
        get() = R.layout.activity_mycar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carAdapter= MyCarAdapter(this)
        carAdapter.setOnItemClickListener(object :MyCarAdapter.OnItemClickListener{
            override fun goDetailPage(car: Car) {
                ActivityUtil.create(this@MyCarActivity).put("car",car).go(CarDetailActivity::class.java).start()
            }

        })
        rec_mycar.adapter=carAdapter
        rec_mycar.layoutManager=LinearLayoutManager(this).apply { orientation=LinearLayoutManager.VERTICAL }

        sl.setOnRefreshListener { getData() }

        btn_add.setOnClickListener {
            ActivityUtil.create(this@MyCarActivity).go(CarDetailActivity::class.java).start()
        }

    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData() {
        sl.isRefreshing=true
        appComponent.dbService.getMyCars(appComponent.preference.currentUser!!.id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    carAdapter.setDate(it)
                    sl.isRefreshing=false
                }
    }
}