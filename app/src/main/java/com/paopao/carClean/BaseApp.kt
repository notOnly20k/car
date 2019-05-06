package com.paopao.carClean

import android.app.Application
import android.preference.PreferenceManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule
import org.slf4j.LoggerFactory


/**
 * Created by cz on 3/27/18.
 */
class BaseApp : Application(), AppComponent {
    override lateinit var objectMapper: ObjectMapper
    override lateinit var preference: Preference


    override fun onCreate() {
        super.onCreate()
        instance = this
        Fresco.initialize(this)
        preference =  AppPreference(this, PreferenceManager.getDefaultSharedPreferences(this), this)
        objectMapper = ObjectMapper().apply {
            registerModule(JsonOrgModule())
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        }

    }



    companion object {
        private val logger = LoggerFactory.getLogger("BaseApp")

        @JvmStatic lateinit var instance: BaseApp
            private set
    }
}
