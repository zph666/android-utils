package com.sword.utils.spf

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 文件存储辅助
 */
abstract class SPFDelegate {

    val preferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(
            getSharedPreferencesName(),
            Context.MODE_PRIVATE
        )
    }


    fun clearAll() {
        preferences.edit().clear().apply()
    }


    fun getAll(): MutableMap<String, *>? {
        return preferences.all
    }

    fun int(defaultValue: Int = 0) = object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return preferences.getInt(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            preferences.edit().putInt(property.name, value).apply()
        }
    }

    fun string(defaultValue: String? = null) = object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return preferences.getString(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
            preferences.edit().putString(property.name, value).apply()
        }
    }

    fun long(defaultValue: Long = 0L) = object : ReadWriteProperty<Any, Long> {

        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return preferences.getLong(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            preferences.edit().putLong(property.name, value).apply()
        }
    }

    fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return preferences.getBoolean(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            preferences.edit().putBoolean(property.name, value).apply()
        }
    }

    fun float(defaultValue: Float = 0.0f) = object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return preferences.getFloat(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            preferences.edit().putFloat(property.name, value).apply()
        }
    }

    abstract fun getSharedPreferencesName(): String
}

// 全局 Context 持有者 - 放在这里最简单
val appContext: Context
    get() = SPFContext.application.applicationContext

object SPFContext {
    lateinit var application: Application
        private set

    fun init(app: Application) {
        application = app
    }
}