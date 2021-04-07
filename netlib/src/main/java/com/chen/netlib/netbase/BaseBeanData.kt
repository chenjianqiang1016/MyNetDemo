package com.chen.netlib.netbase

/**
 * 服务器基础数据类
 */
data class BaseBeanData<T>(var msg: String, var code: String, var data: T)

