package com.chen.mynetdemo

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import com.chen.netlib.netbase.ApiBaseObserver
import com.chen.netlib.netbase.BaseBeanData
import com.chen.netlib.netbean.MineShareBean
import com.chen.netlib.netbean.PhonePwdLoginBean
import com.chen.netlib.service.ApiServiceImpl
import com.chen.netlib.url.Url.ApiSuccessCode
import com.chen.netlib.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //版本号
        SpUtil.put("VersionName", DeviceUtil.getVersionName(this))

        //这里调一次，是为了模拟APP启动后调用的广告接口（闪屏页、引导页）。
        toGetShareData()

        to_login?.click {
            //登录。post 表单提交

            val paramsMap = HashMap<String, String>()
            //手机号
            paramsMap["mobile"] = "123123"
            //密码
            paramsMap["pwd"] = "a1234567"

            //下面2个参数，是额外随便加的，就是单纯为了看参数排序的
            paramsMap["abc"] = "abc"
            paramsMap["nnnqqq"] = "nnnqqq"

            ApiServiceImpl.toMobilePwdLogin(
                paramsMap,
                object : ApiBaseObserver<BaseBeanData<PhonePwdLoginBean>>() {
                    override fun onNext(t: BaseBeanData<PhonePwdLoginBean>) {

                        this@MainActivity.toast(t.msg)

                        if (TextUtils.equals(ApiSuccessCode, t.code)) {
                            //登录成功，接口返回200成功值
                        } else {
                            //登录失败
                        }
                        LogUtil.e("$t")

                        //登录成功，存起来必要数据，给header添加
                        SpUtil.put("UserID", "1234567890")
                        SpUtil.put("UserName", "abcdefg")

                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        this@MainActivity.toast("登录失败")
                    }
                })


        }


        to_login_2?.click {
            //登录。post 非表单提交

            ApiServiceImpl.toMobilePwdLogin2(
                "123123", "a1234567",
                object : ApiBaseObserver<BaseBeanData<PhonePwdLoginBean>>() {
                    override fun onNext(t: BaseBeanData<PhonePwdLoginBean>) {

                        this@MainActivity.toast(t.msg)

                        if (TextUtils.equals(ApiSuccessCode, t.code)) {
                            //登录成功，接口返回200成功值
                        } else {
                            //登录失败
                        }
                        LogUtil.e("$t")

                        //登录成功，存起来必要数据，给header添加
                        SpUtil.put("UserID", "1234567890")
                        SpUtil.put("UserName", "abcdefg")

                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        this@MainActivity.toast("登录失败")
                    }
                })


        }


        get_share?.click {
            //分享

            //用于模拟带参数的get请求
            ApiServiceImpl.getMineShareInfo2(
                "page", "pageSize", "ddd", "pageA", 123,
                object : ApiBaseObserver<MineShareBean>() {
                    override fun onNext(t: MineShareBean) {
                        LogUtil.e("$t")
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)

                    }
                })

        }

    }


    private fun toGetShareData() {

        ApiServiceImpl.getMineShareInfo(
            object : ApiBaseObserver<MineShareBean>() {
                override fun onNext(t: MineShareBean) {
                    LogUtil.e("$t")
                }

                override fun onError(throwable: Throwable) {
                    super.onError(throwable)

                }
            })


    }


}
