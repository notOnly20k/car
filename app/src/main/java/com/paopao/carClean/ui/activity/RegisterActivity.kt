package com.paopao.carClean.ui.activity

import android.os.Bundle
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.bean.User
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getIntExtra("edit",0)==1){
            val user=appComponent.preference.currentUser
            et_account.setText(user?.account)
            et_name.setText(user?.name)
            et_pwd.setText(user?.pwd)
            et_account.isEnabled=false
            btn_save.text="保存"
        }
        btn_save.setOnClickListener {
            if (et_account.text.toString().isNotEmpty() && et_name.text.toString().isNotEmpty() &&
                    et_pwd.text.toString().isNotEmpty()) {

                if (intent.getIntExtra("edit",0)==1) {
                    val user = User(0, et_account.text.toString(),
                            et_name.text.toString(), et_pwd.text.toString())
                    appComponent.dbService.updateUser(user)
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .subscribe {
                                when (it) {
                                    -1 -> Toast.makeText(this, "编辑失败", Toast.LENGTH_SHORT).show()
                                    -3 -> Toast.makeText(this, "编辑失败,账号已被注册", Toast.LENGTH_SHORT).show()
                                    else -> {
                                        appComponent.preference.currentUser = user
                                        Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                            }
                }
                else
                appComponent.dbService.getUserByAccount(et_account.text.toString())
                        .flatMap {
                            if (!it.isPresent){
                                appComponent.dbService.insUser(User(0,et_account.text.toString(),
                                        et_name.text.toString(),et_pwd.text.toString()))
                            }else
                                Observable.just(-3)
                        }
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe {
                            when (it) {
                                -1 -> Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show()
                                -3 -> Toast.makeText(this,"注册失败,账号已被注册",Toast.LENGTH_SHORT).show()
                                else -> {
                                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        }
            }
        }
    }
}