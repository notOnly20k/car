package com.paopao.carClean


import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by cz on 3/27/18.
 */
interface  AppComponent{
    val preference: Preference
    val objectMapper: ObjectMapper
}