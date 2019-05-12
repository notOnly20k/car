package com.paopao.carClean.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paopao.carClean.R
import com.paopao.carClean.bean.Comment
import com.paopao.carClean.bean.News
import kotlinx.android.synthetic.main.item_comment.view.*
import java.text.SimpleDateFormat

class CommentAdapter(val context: Context) : RecyclerView.Adapter<CommentAdapter.RecyclerHolder>(){
    var list= mutableListOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {

        return RecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDate(list: List<Comment>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addDate(comment: Comment){
        list.add(comment)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val comment = list.get(position)
        val formatter  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        holder.itemView.tv_name.text=comment.user.name
        holder.itemView.tv_content.text=comment.content
        holder.itemView.tv_date.text=formatter.format(comment.date)

    }

    class RecyclerHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

}