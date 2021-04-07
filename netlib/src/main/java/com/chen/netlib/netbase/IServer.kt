package com.chen.netlib.netbase
import okhttp3.Interceptor

interface IServer : IApi {

    class IServerParams constructor(
        val interceptors: List<Interceptor>?,
        val connectTimeOut: Long,
        val readTimeOut: Long,
        val writeTimeOut: Long
    )

}
