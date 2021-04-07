package com.chen.netlib.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chen.netlib.ui.BaseApplication

val SP_NAME = "my_net_demo"

/**
 * SharedPreferences工具类
 */
object SpUtil {

    private val context = BaseApplication.getInstance()

    fun put(key: String, value: Any) {
        context.put(key, value)
    }

    fun spRemove(key: String) {
        context.spRemove(key)
    }

    fun spClear() {
        context.spClear()
    }

    fun getStringValue(key: String,default:String? = ""): String = context.getStringValue(key,default).orEmpty()

    fun getBooleanValue(key: String,default: Boolean = false): Boolean = context.getBooleanValue(key,default)

    fun getIntValue(key: String,default:Int = 0): Int = context.getIntValue(key,default)

    fun getLongValue(key: String,default: Long = 0L): Long = context.getLongValue(key,default)

    fun getFloatValue(key: String,default:Float = 0f): Float = context.getFloatValue(key,default)
}

fun Context.getSharePreference(): SharedPreferences {
    return this.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
}

/**
 * 存储sp信息，int .long.float.string.boolean
 */
fun Context.put(key: String, value: Any) {
    getSharePreference().edit {
        when (value) {
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is Boolean -> putBoolean(key, value)
        }
    }
}

/**
 * 获取sp中key对应字符串
 */
fun Context.getStringValue(key: String, default: String? = ""): String? = getSharePreference().getString(key, default)

/**
 * 获取sp中key对应布尔值
 */
fun Context.getBooleanValue(key: String, default: Boolean = false): Boolean =
    getSharePreference().getBoolean(key, default)

/**
 * 获取sp中key对应int值
 */
fun Context.getIntValue(key: String, default: Int = 0): Int = getSharePreference().getInt(key, default)

/**
 * 获取sp中key对应long类型值
 */
fun Context.getLongValue(key: String, default: Long = 0L): Long = getSharePreference().getLong(key, default)

/**
 * 获取sp中key对应浮点类型值
 */
fun Context.getFloatValue(key: String, default: Float = 0f): Float = getSharePreference().getFloat(key, default)

/**
 * 清除sp中某个值
 */
fun Context.spRemove(key: String) {
    getSharePreference().edit {
        remove(key)
    }
}

/**
 * 清空sp
 */
fun Context.spClear() {
    getSharePreference().edit {
        clear()
    }
}
