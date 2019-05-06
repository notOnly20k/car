package com.paopao.carClean.bean

import java.sql.Date

data class Car(val id: Int, val name: String)

data class CarOrder(val id: Int, val cleanType: String, val date: Date, val carId: Int, val car: Car)

data class CleanType(val id: Int, val name: String, val price: String, val describe: String)

data class News(val id: Int, val date: Date, val content: String, val title: String)

data class Comment(val id: Int, val content: String, val date: Date, val user: User, val userId: Int, val newsId: Int)

data class User @JvmOverloads constructor(
        val id:Int,
        val name:String,
        val pwd:String
)