package com.chen.netlib.service

import com.chen.netlib.netbase.*
import com.chen.netlib.netbean.MineShareBean
import com.chen.netlib.netbean.PhonePwdLoginBean
import com.chen.netlib.url.Url
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


/**
 * API 的实现
 */
class ApiServiceImpl {

    companion object {

        //这个不能删
        val apiZmService = NetApiFactory.getServerApi(ApiService::class.java, Url.Baseurl)!!

        /********************以下，是接口请求操作。用的时候，以下皆可删除，换成自己的********************************/

        /**
         * 手机号、密码登陆。表单提交
         *
         * 注意：这里没有单独拿出来 PhonePwdLoginBean，而是把返回的数据，全部返回了。是因为登录那里，要用code值来判断登录是否成功
         */
        fun toMobilePwdLogin(
            paramsMap: HashMap<String, String>,
            observer: ApiBaseObserver<BaseBeanData<PhonePwdLoginBean>>
        ) {

            apiZmService.toMobilePwdLogin(paramsMap)
                .compose(RxBiz.transformMain())
                .subscribe(observer)
        }

        /**
         * 手机号、密码登陆。非表单提交
         *
         */
        fun toMobilePwdLogin2(
            phone:String,
            pwd:String,
            observer: ApiBaseObserver<BaseBeanData<PhonePwdLoginBean>>
        ) {

            val jsonObject = JSONObject().apply {
                put("mobile", phone)
                put("pwd", pwd)

                //为了看参数排序，额外加的
                put("higk", "higk")
                put("nop", "nop")
            }
            val toRequestBody = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


            apiZmService.toMobilePwdLogin2(toRequestBody)
                .compose(RxBiz.transformMain())
                .subscribe(observer)
        }


        //获取分享数据
        fun getMineShareInfo(
            observer: ApiBaseObserver<MineShareBean>
        ) {
            apiZmService.getMineShareInfo()
                .compose(RxBiz.transformMain())
                .map(ResultFunc<MineShareBean>())
                .onErrorReturnItem(MineShareBean())
                .subscribe(observer)
        }

        //获取分享数据，带参数
        fun getMineShareInfo2(
            page: String,
            pageSize: String,
            ddd: String,
            pageA: String,
            temp: Int,
            observer: ApiBaseObserver<MineShareBean>
        ) {
            apiZmService.getMineShareInfo2(page,pageSize,ddd,pageA,temp)
                .compose(RxBiz.transformMain())
                .map(ResultFunc<MineShareBean>())
                .onErrorReturnItem(MineShareBean())
                .subscribe(observer)
        }


    }


}