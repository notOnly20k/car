package com.paopao.carClean.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.adapter.CommentAdapter
import com.paopao.carClean.bean.Comment
import com.paopao.carClean.bean.News
import com.paopao.carClean.bean.User
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_square_detail.*
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class SquareDetailActivity:BaseActivity() {

    var news: News?=null
    lateinit var commentAdapter:CommentAdapter
    override val contentViewResId: Int
        get() = R.layout.activity_square_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        news= intent.getSerializableExtra("news") as News?
        tv_pic.setImageURI(news?.pic)
        tv_content.text=news?.content
        commentAdapter= CommentAdapter(this)
        rec_comment.adapter=commentAdapter
        rec_comment.layoutManager= LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
        val formatter  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tv_date.text=formatter.format(news?.date)
        tv_title.text=news?.title
        sl.setOnRefreshListener { getData() }
        img_send.setOnClickListener {view->
            if (et_comment.text.isNotEmpty()){
                val user=appComponent.preference.currentUser
                val comment=Comment(0,et_comment.text.toString(), Timestamp(Date().time),user!!,user.id,news!!.id)
                appComponent.dbService.insComments(comment)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe {
                            if (it==1) {
                                commentAdapter.addDate(comment)
                                et_comment.text.clear()
                                hideSoftWindow(view)
                                Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show()
                            }
                            else
                                Toast.makeText(this,"评论失败",Toast.LENGTH_SHORT).show()
                        }
            }
            else
                Toast.makeText(this,"请输入评论内容",Toast.LENGTH_SHORT).show()
        }
        getData()
    }

    fun getData(){
        sl.isRefreshing=true
        appComponent.dbService.getComments(news?.id?:0)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    commentAdapter.setDate(it)
                    sl.isRefreshing=false
                }

    }
}