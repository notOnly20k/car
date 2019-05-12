package com.paopao.carClean.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
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


    protected fun hideSoftWindow(v: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
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