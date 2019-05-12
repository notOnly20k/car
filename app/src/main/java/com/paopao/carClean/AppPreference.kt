package com.paopao.carClean

import android.content.Context
import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import com.paopao.carClean.bean.User
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Logger
import kotlin.reflect.KProperty


class AppPreference(appContext: Context,
                    private val pref: SharedPreferences,
                    private val appComponent: AppComponent) : Preference {
    override var currentUser: User? by sharePreference(KEY_CURRENT_USER)

    private val objectMapper: ObjectMapper
        get() = appComponent.objectMapper

    private inline fun <reified T> sharePreference(key: String): SharedPreferenceDelegate<T> {
        return SharedPreferenceDelegate(key, pref, T::class.java, objectMapper)
    }
    private class SharedPreferenceDelegate<T>(val key: String,
                                              val pref: SharedPreferences,
                                              val clazz: Class<T>,
                                              val objectMapper: ObjectMapper) {
        private var cacheValue: AtomicReference<T?>? = null

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            if (cacheValue == null) {
                cacheValue = AtomicReference(pref.getString(key, null)?.let { objectMapper.readValue(it, clazz) })
            }

            return cacheValue!!.get()
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            val editor = pref.edit()
            if (value == null) {
                editor.remove(key)
            } else {
                editor.putString(key, objectMapper.writeValueAsString(value))
            }

            editor.apply()
            cacheValue?.set(value)
        }
    }

    override fun clear() {
        pref.edit()
                .remove(KEY_CURRENT_USER)
                .apply()
    }
    companion object {
        const val KEY_CURRENT_USER = "current_user"
    }
}