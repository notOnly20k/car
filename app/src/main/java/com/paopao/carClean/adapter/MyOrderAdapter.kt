package com.paopao.carClean.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.bean.CarOrder
import kotlinx.android.synthetic.main.item_order.view.*
import java.text.SimpleDateFormat

class MyOrderAdapter(val context: Context) : RecyclerView.Adapter<MyOrderAdapter.RecyclerHolder>(){
    var list: List<CarOrder> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {

        return RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDate(list: List<CarOrder>){
        this.list=list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val order = list.get(position)
        val formatter  = SimpleDateFormat("yyyy-MM-dd HH:mm")
        holder.itemView.tv_name.text="预约服务：${order.cleanType}"
        holder.itemView.tv_time.text="预约时间：${formatter.format(order.date)}"
        holder.itemView.tv_type.text="预约价格:${order.price}元"
        holder.itemView.tv_car.text="预约车辆:${order.carName}"
        holder.itemView.btn_delete.setOnClickListener { onItemClickListener?.goDetailPage(order) }

    }

    class RecyclerHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun goDetailPage(order: CarOrder)
    }
}