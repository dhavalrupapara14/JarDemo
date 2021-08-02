package com.jar.demo.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

const val PREFS_NAME: String = "jar_demo_shared_pref"
const val LAST_GLIDE_CACHE_CLEAR_TIME: String = "last_glide_cache_clear_time"

/**
 * Global Shared preference class.
 */
class AppSharedPref constructor(private val application: Application) {

    private fun getSharedPref(): SharedPreferences {
        return application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
    }

    private fun setLong(key: String, value: Long) {
        getSharedPref().edit().apply() {
            putLong(key, value)
            apply()
        }
    }

    private fun getLong(key: String, defValue: Long): Long {
        return getSharedPref().getLong(key, defValue)
    }

    fun getLastGlideCacheClearedTime(): Long {
        return getLong(LAST_GLIDE_CACHE_CLEAR_TIME, 0)
    }

    fun setLastGlideCacheClearedTime(time: Long) {
        setLong(LAST_GLIDE_CACHE_CLEAR_TIME, time)
    }
}