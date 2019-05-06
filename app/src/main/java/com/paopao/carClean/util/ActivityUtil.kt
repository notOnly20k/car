package com.paopao.carClean.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment

import java.io.Serializable
import java.util.ArrayList

/**
 *
 * Created by liaoxiang on 16/8/8.
 */
class ActivityUtil {
    private lateinit var fromActivity: Context
    private lateinit var fragment: Fragment
    private var goActivity: Class<*>? = null
    private var bundle: Bundle? = null

    private constructor(context: Context) {
        fromActivity = context
        //        this.goActivity = goActivity;
        bundle = Bundle()
    }

    private constructor(fragment: Fragment) {
        this.fragment = fragment
        //        this.goActivity = goActivity;
        bundle = Bundle()
    }

    fun go(goActivity: Class<*>): ActivityUtil {
        this.goActivity = goActivity
        return this
    }

    /**
     * 传递int参数
     * @param key 键
     * @param intValue 值
     * @return this
     */
    fun put(key: String, intValue: Int): ActivityUtil {
        bundle!!.putInt(key, intValue)
        return this
    }

    /**
     * 传递Parcelable参数
     * @param key 键
     * @param p 值
     * @return this
     */
    fun put(key: String, p: Parcelable): ActivityUtil {
        bundle!!.putParcelable(key, p)
        return this
    }

    /**
     * 传递String参数
     * @param key 键
     * @param s 值
     * @return this
     */
    fun put(key: String, s: String): ActivityUtil {
        bundle!!.putString(key, s)
        return this
    }

    /**
     * 传递float参数
     * @param key 键
     * @param f 值
     * @return this
     */
    fun put(key: String, f: Float): ActivityUtil {
        bundle!!.putFloat(key, f)
        return this
    }

    /**
     * 传递double参数
     * @param key 键
     * @param d 值
     * @return this
     */
    fun put(key: String, d: Double): ActivityUtil {
        bundle!!.putDouble(key, d)
        return this
    }

    /**
     * 传递Serializable参数
     * @param key 键
     * @param s 值
     * @return this
     */
    fun put(key: String, s: Serializable): ActivityUtil {
        bundle!!.putSerializable(key, s)
        return this
    }

    /**
     * 传递StringList参数
     * @param key 键
     * @param a 值
     * @return this
     */
    fun putStringList(key: String, a: ArrayList<String>): ActivityUtil {
        bundle!!.putStringArrayList(key, a)
        return this
    }

    /**
     * 传递ParcelableList参数
     * @param key 键
     * @param a 值
     * @return this
     */
    fun putParcelableList(key: String, a: ArrayList<out Parcelable>): ActivityUtil {
        bundle!!.putParcelableArrayList(key, a)
        return this
    }

    /**
     * 跳转activity
     */
    fun start() {
        if (goActivity == null) {
            return
        }
        if (fromActivity != null) {
            val intent = Intent(fromActivity, goActivity)
            if (bundle != null && !bundle!!.isEmpty) {
                intent.putExtras(bundle!!)
            }
            fromActivity.startActivity(intent)
        } else if (fragment != null) {
            val intent = Intent(fragment.context, goActivity)
            if (bundle != null && !bundle!!.isEmpty) {
                intent.putExtras(bundle!!)
            }
            fragment.startActivity(intent)
        }
    }

    /**
     * 跳转activity 带requestCode
     * @param requestCode 请求code
     */
    fun startForResult(requestCode: Int) {
        if (goActivity == null) {
            return
        }
        if (fromActivity != null) {
            val intent = Intent(fromActivity, goActivity)
            if (bundle != null && !bundle!!.isEmpty) {
                intent.putExtras(bundle!!)
            }
            if (fromActivity is Activity) {
                (fromActivity as Activity).startActivityForResult(intent, requestCode)
            }
        } else if (fragment != null && !fragment.isDetached) {
            val intent = Intent(fragment.context, goActivity)
            if (bundle != null && !bundle!!.isEmpty) {
                intent.putExtras(bundle!!)
            }
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    companion object {

        fun create(context: Context): ActivityUtil {
            return ActivityUtil(context)
        }

        fun create(fragment: Fragment): ActivityUtil {
            return ActivityUtil(fragment)
        }
    }

}
