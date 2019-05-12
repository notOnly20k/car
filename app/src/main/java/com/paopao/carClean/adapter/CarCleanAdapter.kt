package com.paopao.carClean.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.bean.CleanType
import com.paopao.carClean.bean.News
import kotlinx.android.synthetic.main.item_carclean.view.*
import java.text.SimpleDateFormat

class CarCleanAdapter(val context: Context) : RecyclerView.Adapter<CarCleanAdapter.RecyclerHolder>(){
    var list: List<CleanType> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {

        return RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_carclean, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDate(list: List<CleanType>){
        this.list=list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val cleanType = list.get(position)
        val formatter  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        holder.itemView.tv_title.text=cleanType.name
        holder.itemView.tv_content.text=cleanType.describe
        holder.itemView.img_pic.setImageURI(cleanType.pic)
        holder.itemView.tv_price.text="${cleanType.price}元/次"
        holder.itemView.setOnClickListener { onItemClickListener?.goDetailPage(cleanType) }

    }

    class RecyclerHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun goDetailPage(cleanType:CleanType)
    }
}