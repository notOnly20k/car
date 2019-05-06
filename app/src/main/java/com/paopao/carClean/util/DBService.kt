package com.paopao.carClean.util

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class DBService private constructor() {
    private val conn: Connection? = null //打开数据库对象
    private val ps: PreparedStatement? = null//操作整合sql语句的对象
    private val rs: ResultSet? = null//查询结果的集合

    companion object {

        //DBService 对象
        var dbService: DBService? = null
        get() {
            if (field==null){
                field=DBService()
            }

            return field
        }

        @Synchronized
        fun get(): DBService{
            return dbService!!
        }
    }


}
