package com.paopao.carClean.util


import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

object DBOpenHelper {
    private val user = "root"//用户名
    private val password = "000000"//密码
    val ip = "10.0.2.2"
    val dbName = "car_maintenance"

    val conn: Connection?
        get() {
            var conn: Connection? = null
            try {
                Class.forName("com.mysql.jdbc.Driver")
                conn = DriverManager.getConnection(
                        "jdbc:mysql://$ip:3306/$dbName?serverTimezone=UTC&useUnicode=yes&characterEncoding=utf-8&useSSL=false",
                        user, password)
            } catch (ex: SQLException) {
                ex.printStackTrace()
            } catch (ex: ClassNotFoundException) {
                ex.printStackTrace()
            }

            return conn
        }

    //关闭数据库


    fun closeAll(conn: Connection?, ps: PreparedStatement?) {
        if (conn != null) {
            try {
                conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
        if (ps != null) {
            try {
                ps.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }

    //关闭数据库

    fun closeAll(conn: Connection?, ps: PreparedStatement?, rs: ResultSet?) {
        if (conn != null) {
            try {
                conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
        if (ps != null) {
            try {
                ps.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
        if (rs != null) {
            try {
                rs.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }
}

