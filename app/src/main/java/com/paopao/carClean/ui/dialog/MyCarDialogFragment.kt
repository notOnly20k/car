package com.paopao.carClean.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.util.DisplayMetrics
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.bean.Car
import com.paopao.carClean.bean.CarOrder
import com.paopao.carClean.ext.appComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_order_clean.*
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MyCarDialogFragment : AppCompatDialogFragment() {
    private val logger = LoggerFactory.getLogger("SelectDateDialog")


    private lateinit var listener: SeletDateDialogListener
    private var compositeSubscription: CompositeDisposable? = null
    private var carId=0


    override fun onStart() {
        super.onStart()
        var dialog = dialog
        if (dialog != null) {
            dialog.setTitle("预约保养")
            var dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            dialog.window.setLayout( ((dm.widthPixels * 0.75).toInt()), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return LayoutInflater.from(context).inflate(R.layout.dialog_order_clean, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compositeSubscription = CompositeDisposable()
        val calender = Calendar.getInstance()
        et_date.setOnClickListener {
           DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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
                et_date.setText(year.toString() + "-" + mouth1 + "-" + day1)
            }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH)).apply { show() }
        }

        et_time.setOnClickListener {
            TimePickerDialog(context,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val dateStr = "${if (hourOfDay <= 9) {
                    "0$hourOfDay"
                } else {
                    hourOfDay.toString()
                }}:${if (minute <= 9) {
                    "0$minute"
                } else {
                    minute.toString()
                }}"
                et_time.setText(dateStr)
            },calender.get(Calendar.HOUR_OF_DAY),
                    calender.get(Calendar.MINUTE),false).apply { show() }
        }

        btn_sure.setOnClickListener {
            if (et_date.text.isNotEmpty()&&et_time.text.isNotEmpty()&&carId!=0){
                val formatter  = SimpleDateFormat("yyyy-MM-dd HH:mm")
                listener.clickSure(CarOrder(0,"","", Timestamp(formatter.parse(et_date.text.toString()+" "+et_time.text.toString()).time),
                        carId,appComponent.preference.currentUser!!.id,""),this)
            }else
                Toast.makeText(context,"完善信息", Toast.LENGTH_SHORT).show()
        }

        btn_cancel.setOnClickListener { dismiss() }



        getData()
    }


    fun getData() {
        appComponent.dbService.getMyCars(appComponent.preference.currentUser!!.id)
                .doOnSubscribe { compositeSubscription?.add(it) }
                .subscribe {
                    if (it.isEmpty()) {
                        sp_car.adapter=ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item, listOf("暂无车辆"))
                    }else{
                        sp_car.adapter=ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,it.map { it.name })
                        sp_car.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                carId=it[position].id
                            }

                        }
                    }

                }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription?.dispose()
    }


    fun setListener(wheelDialogListener: SeletDateDialogListener): MyCarDialogFragment {
        listener = wheelDialogListener
        return this
    }

    interface SeletDateDialogListener {
        fun clickSure(carOrder: CarOrder, dialog: MyCarDialogFragment)
    }

    class Builder {
        fun create(): MyCarDialogFragment {
            return MyCarDialogFragment()
        }
    }
}