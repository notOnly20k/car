package com.paopao.carClean


import com.paopao.carClean.bean.User
import java.util.*

/**
 * 提供程序选项数据的永久存储
 */
interface Preference {
    var currentUser: User?

}
