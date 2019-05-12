package com.paopao.carClean.bean

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp

data class Car(val id: Int, val name: String,val date: Timestamp,val type:String,val userId: Int):Serializable

data class CarOrder(val id: Int, var cleanType: String,var price: String, val date: Timestamp, val carId: Int,val userId: Int, var carName:String)

data class CleanType(val id: Int, val name: String, val price: String, val describe: String,val pic: String)

data class News(val id: Int, val date: Timestamp, val content: String, val title: String, val pic:String):Serializable
data class Comment(val id: Int, val content: String, val date: Timestamp, val user: User, val userId: Int, val newsId: Int)

data class User @JsonCreator constructor(
        @param:JsonProperty("id")
        val id:Int,
        @param:JsonProperty("account")
        val account:String,
        @param:JsonProperty("name")
        val name:String,
        @param:JsonProperty("pwd")
        val pwd:String
)