package com.paopao.carClean.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.paopao.carClean.views.MyDialogProgress
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class BaseActivity : AppCompatActivity() {
    protected val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }
    protected lateinit var mCompositeDisposable: CompositeDisposable
    protected lateinit var progress: MyDialogProgress
    protected abstract val contentViewResId: Int



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewResId)
        mCompositeDisposable = CompositeDisposable()
        progress = MyDialogProgress(this)
    }




    override fun onDestroy() {
        super.onDestroy()
        if (progress!=null) {
            progress.dismiss()
        }
        if (mCompositeDisposable != null) { //取消订阅
            mCompositeDisposable.clear()
        }
    }
}