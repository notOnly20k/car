package com.paopao.carClean.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.facebook.drawee.backends.pipeline.Fresco
import com.paopao.carClean.views.MyDialogProgress

import java.lang.reflect.Field

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * fragment ，单独在activity使用请设置setUserVisibleHint(true),结合viewpager使用不需要
 */
abstract class BaseFragment : Fragment() {

    private var dialog: MyDialogProgress? = null
    protected abstract val contentViewLayoutID: Int
    protected val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private var isFirstResume = true
    private var isFirstVisible = true
    private var isFirstInvisible = true
    private var isPrepared: Boolean = false
    private var compositeSubscription: CompositeDisposable? = null
    protected abstract fun onFirstVisibleToUser()
    protected abstract fun onVisibleToUser()
    protected abstract fun onInvisibleToUser()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Fresco.initialize(context!!)
        compositeSubscription = CompositeDisposable()
        initPrepare()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (contentViewLayoutID != 0) {
            inflater.inflate(contentViewLayoutID, null)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun addDisposable(disposable: Disposable) {
        compositeSubscription!!.add(disposable)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
        if (userVisibleHint) {
            onVisibleToUser()
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint) {
            onInvisibleToUser()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false
                initPrepare()
            } else {
                onVisibleToUser()
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false
                //                onFirstUserInvisible();
            } else {
                onInvisibleToUser()
            }
        }
    }


    override fun onDetach() {
        super.onDetach()

        if (compositeSubscription != null && !compositeSubscription!!.isDisposed) {
            compositeSubscription!!.dispose()
        }
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed

        try {
            val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)

        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }

    @Synchronized
    private fun initPrepare() {
        if (isPrepared) {
            onFirstVisibleToUser()
        } else {
            isPrepared = true
        }
    }

    /**
     * 查找	View
     * @param paramInt
     * @return
     */
    fun <T : View> findViewById(paramInt: Int): T? {
        return if (view == null) {
            null
        } else view!!.findViewById<View>(paramInt) as T

    }

    protected fun isShowDialog(show: Boolean?) {
        if (show!!) {
            showDialog("")
        } else {
            dismissDialog()
        }
    }

    /**
     * 显示加载提示窗
     * @param msg 提示文字
     * @param canCancel 是否可手动取消
     */
    @JvmOverloads
    protected fun showDialog(msg: CharSequence = "", canCancel: Boolean = false) {
        if (context == null) {
            return
        }
        if (dialog == null) {
            dialog = MyDialogProgress(getContext()!!)
        }
        dialog!!.setCanceledOnTouchOutside(canCancel)
        if (!dialog!!.isShowing) {
            dialog!!.show()
        }
    }

    /**
     * 关闭加载窗
     */
    fun dismissDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    /**
     * 显示Toast
     * @param msg 显示文字
     */
    fun showToast(msg: String) {
        if (context == null) {
            return
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show()
    }
}
/**
 * 显示加载提示窗
 */
/**
 * 显示加载提示窗
 * @param msg 提示文字
 */
