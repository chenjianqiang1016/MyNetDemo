package com.chen.netlib.utils

import android.text.TextUtils
import java.util.*

object StringUtil {

    /**
     * 检查字符串，是否为空，或者，为"null"、"NULL"、"Null"
     *
     * true：表示为空、null 或者 "null"、"NULL"、"Null" 中的某一个
     *
     * 注：如果项目中，"null"也算有效结果，则此方法不适用。
     * 例如：要判断用户ID是否为空，可以用这个办法判断。因为用户ID是 "null"，也是无效的。
     */
    fun checkStringIsNull(str:String?):Boolean{

        if (str.isNullOrEmpty()) {
            return true
        }

        return TextUtils.equals("null", str.toLowerCase(Locale.ROOT))

    }


}