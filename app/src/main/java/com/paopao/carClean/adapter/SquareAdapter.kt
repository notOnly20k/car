package com.paopao.carClean.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.bean.News
import kotlinx.android.synthetic.main.item_news.view.*
import java.text.SimpleDateFormat

class SquareAdapter(val context: Context) : RecyclerView.Adapter<SquareAdapter.RecyclerHolder>(){
    var list: List<News> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {

        return RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDate(list: List<News>){
        this.list=list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val news = list.get(position)
        val formatter  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        holder.itemView.tv_title.text=news.title
        holder.itemView.img_pic.setImageURI(news.pic)
        holder.itemView.tv_date.text=formatter.format(news.date)
        holder.itemView.setOnClickListener { onItemClickListener?.goDetailPage(news) }

    }

    class RecyclerHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun goDetailPage(news:News)
    }
}