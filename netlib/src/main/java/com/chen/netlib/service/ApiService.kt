package com.chen.netlib.service

import com.chen.netlib.netbase.BaseBeanData
import com.chen.netlib.netbean.MineShareBean
import com.chen.netlib.netbean.PhonePwdLoginBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * 网络请求接口
 */
interface ApiService {

    /**
     * 手机号，密码登陆。表单提交
     */
    @FormUrlEncoded
    @POST("abc/abc")
    fun toMobilePwdLogin(@FieldMap params: HashMap<String, String>): Observable<BaseBeanData<PhonePwdLoginBean>>

    /**
     * 手机号，密码登陆。非表单提交
     */
    @POST("abc/abc")
    fun toMobilePwdLogin2(@Body body: RequestBody): Observable<BaseBeanData<PhonePwdLoginBean>>

    /**
     * 获取 我的 界面分享信息
     */
    @GET("abc/abc")
    fun getMineShareInfo(): Observable<BaseBeanData<MineShareBean>>

    /**
     * 获取 我的 界面分享信息
     */
    @GET("abc/abc")
    fun getMineShareInfo2(
        @Query("page") page: String, @Query("pageSize") pageSize: String,@Query("ddd") ddd: String, @Query("pageA") pageA: String, @Query(
            "temp"
        ) temp: Int
    ): Observable<BaseBeanData<MineShareBean>>


}