package com.paopao.carClean.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.bean.Car
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseActivity
import com.whynuttalk.foreignteacher.ext.e
import kotlinx.android.synthetic.main.activity_car_detail.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class CarDetailActivity:BaseActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_car_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        if (intent.getSerializableExtra("car")!=null){
            var car=intent.getSerializableExtra("car") as Car
            et_name.setText(car.name)
            et_type.setText(car.type)
            et_date.text= format1.format(car.date)
            btn_delete.visibility=View.VISIBLE
            btn_delete.setOnClickListener {
                appComponent.dbService.deleteCar(car.id)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe {
                            if (it==-1){
                                Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
            }
        }
        btn_save.setOnClickListener {
            if (et_name.text.isNotEmpty()&&et_date.text.isNotEmpty()&&et_type.text.isNotEmpty()) {
                if (intent.getSerializableExtra("car")!=null)
                appComponent.dbService.updateCar(Car(0, et_name.text.toString(),
                        Timestamp(format1.parse(et_date.text.toString()).time)
                        , et_type.text.toString(), appComponent.preference.currentUser!!.id))
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe {
                            if (it == -1) {
                                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                else
                    appComponent.dbService.insCar(Car(0, et_name.text.toString(),
                            Timestamp(format1.parse(et_date.text.toString()).time)
                            , et_type.text.toString(), appComponent.preference.currentUser!!.id))
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .subscribe {
                                if (it == -1) {
                                    Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
            }else{
                Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show()
            }
        }
        et_date.setOnClickListener {
            val calender=Calendar.getInstance()
            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val mouth1 = if (monthOfYear <= 9) {
                    "0" + (monthOfYear + 1)
                } else {
                    (monthOfYear + 1).toString()
                }
                val day1 = if (dayOfMonth <= 9) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                val dateStr = year.toString() + "-" + mouth1 + "-" + day1
                et_date.text=dateStr
            }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }


    }
}