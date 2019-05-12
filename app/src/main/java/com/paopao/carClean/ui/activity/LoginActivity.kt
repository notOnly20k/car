package com.paopao.carClean.ui.activity

import android.os.Bundle
import android.widget.Toast
import com.paopao.carClean.R
import com.paopao.carClean.ext.appComponent
import com.paopao.carClean.ui.base.BaseActivity
import com.paopao.carClean.util.ActivityUtil
import com.whynuttalk.foreignteacher.ext.e
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:BaseActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.e { appComponent.preference.currentUser }
        if (appComponent.preference.currentUser!=null){
            ActivityUtil.create(this).go(MainActivity::class.java).start()
            finish()
        }
        super.onCreate(savedInstanceState)
        btn_login.setOnClickListener {
            if (et_account.text.isNotEmpty()&&et_pwd.text.isNotEmpty()){
                appComponent.dbService.getUser(Pair(et_account.text.toString(),et_pwd.text.toString()))
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe {
                            if (it.isPresent){
                                appComponent.preference.currentUser=it.get()
                                ActivityUtil.create(this).go(MainActivity::class.java).start()
                                finish()
                            }else{
                                Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT).show()
                            }
                        }
            }else{
                Toast.makeText(this,"请填写账号和密码",Toast.LENGTH_SHORT).show()
            }
        }

        btn_reg.setOnClickListener {
            ActivityUtil.create(this).go(RegisterActivity::class.java).start()
        }
    }
}