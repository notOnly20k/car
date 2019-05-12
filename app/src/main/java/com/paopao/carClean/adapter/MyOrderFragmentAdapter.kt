package com.paopao.carClean.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.bean.Car
import kotlinx.android.synthetic.main.item_car.view.*

import java.text.SimpleDateFormat

class MyOrderFragmentAdapter(val context: Context) : RecyclerView.Adapter<MyOrderFragmentAdapter.RecyclerHolder>(){
    var list: List<Car> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {

        return RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_car, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDate(list: List<Car>){
        this.list=list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val car = list.get(position)
        val formatter  = SimpleDateFormat("yyyy-MM-dd")
        holder.itemView.tv_name.text=car.name
        holder.itemView.tv_time.text=formatter.format(car.date)
        holder.itemView.tv_type.text=car.type
        holder.itemView.setOnClickListener { onItemClickListener?.goDetailPage(car) }

    }

    class RecyclerHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun goDetailPage(car:Car)
    }
}