package com.paopao.carClean.bean

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


data class User @JvmOverloads constructor(
         val id:Int,
         val name:String,
         val sex:String
)
