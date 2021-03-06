package com.chen.netlib.netbase

import com.alibaba.fastjson.JSON
import com.chen.netlib.BuildConfig
import com.chen.netlib.utils.LogUtil
import com.chen.netlib.utils.SpUtil
import com.chen.netlib.utils.StringUtil
import com.google.gson.GsonBuilder
import com.safframework.http.interceptor.LoggingInterceptor
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

object NetApiFactory {

    @Volatile
    var sOkHttpClient: OkHttpClient? = null

    @Volatile
    var mOkHttpClient: OkHttpClient? = null

    @Volatile
    var sSharedSimpleClient: OkHttpClient? = null

    @Volatile
    private var sGsonFactory: GsonConverterFactory? = null

    private val sInterceptors: List<Interceptor>? = null

    private var mMonitor: Interceptor? = null

    private var mDispatcherExecutorService: ExecutorService? = null

    private var mDispatcherMaxRequest: Int = 0

    private var mDispatcherMaxRequestPerHost: Int = 0

    val defaultHttpClient: OkHttpClient?
        get() {
            checkBaseClientNotNull()
            if (mOkHttpClient == null) {
                mOkHttpClient =
                    getCustomizedHttpClient(IServer.IServerParams(sInterceptors, 5, 5, 5))
            }
            return mOkHttpClient
        }

    fun setMonitor(monitor: Interceptor) {
        mMonitor = monitor
    }

    fun setDispatcher(
        executorService: ExecutorService,
        dispatcherMaxRequest: Int,
        dispatcherMaxRequestPerHost: Int
    ) {
        mDispatcherExecutorService = executorService
        mDispatcherMaxRequest = dispatcherMaxRequest
        mDispatcherMaxRequestPerHost = dispatcherMaxRequestPerHost
    }


    fun <T> getServerApi(clazz: Class<T>, url: String): T? {
        return getServerApi(clazz, url, null)
    }

    fun <T> getServerApi(clazz: Class<T>?, url: String?, params: IServer.IServerParams?): T? {
        return if (null == clazz || null == url || "" == url) {
            null
        } else createServerApi(clazz, url, params)
    }

    private fun <T> createServerApi(
        clazz: Class<T>,
        url: String,
        params: IServer.IServerParams?
    ): T {
        if (sGsonFactory == null) {
            sGsonFactory = GsonConverterFactory.create(GsonBuilder().create())
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(if (params == null) defaultHttpClient else getCustomizedHttpClient(params))
            .addConverterFactory(sGsonFactory!!)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(clazz)
    }

    private fun getCustomizedHttpClient(params: IServer.IServerParams): OkHttpClient {

        LogUtil.e("getCustomizedHttpClient ??????")

        checkBaseClientNotNull()
        /**
         * Customize a shared OkHttpClient instance with newBuilder()
         * which will build clients that share same connection pools, thread pools and configuration.
         */
        val builder = sOkHttpClient!!.newBuilder()

        if (params.connectTimeOut > 0) {
            builder.connectTimeout(params.connectTimeOut, TimeUnit.SECONDS)
        }
        if (params.readTimeOut > 0) {
            builder.readTimeout(params.readTimeOut, TimeUnit.SECONDS)
        }
        if (params.writeTimeOut > 0) {
            builder.writeTimeout(params.writeTimeOut, TimeUnit.SECONDS)
        }
        if (null != params.interceptors && params.interceptors.size > 0) {
            for (interceptor in params.interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        //??????header
        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val original: Request = chain.request()

                val request: Request = original.newBuilder()
                    .addHeader("UserID", SpUtil.getStringValue("UserID"))
                    .addHeader("UserName", SpUtil.getStringValue("UserName"))
                    .addHeader("VersionName", SpUtil.getStringValue("VersionName"))
                    .build()

                return chain.proceed(request)

            }

        })

        /**
         * ???????????????????????????????????????????????? ??????header?????????????????????????????????????????????????????????
         *
         * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         *
         * ???????????????????????????????????????????????????????????????
         *
         * ?????????????????????????????????
         *
         * ?????????????????????key????????????????????????????????????????????????????????????
         * ?????????abc=abc&mobile=123123&nnnqqq=nnnqqq&pwd=a1234567
         */
        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val request: Request = chain.request()
                val method: String = request.method
                val httpUrl: HttpUrl = request.url
                val mbu: Request.Builder = request.newBuilder()

                /**
                 * ???????????????
                 * method = GET ; httpUrl = https://......
                 * method = POST ; httpUrl = https://......
                 */
                LogUtil.e("method = $method ; httpUrl = $httpUrl")

                //???????????????????????????????????????????????????
                var jiaoYanStr: String = ""

                if (httpUrl.toString().endsWith("abc")) {
                    //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    jiaoYanStr = ""
                } else {
                    //?????????????????????

                    try {

                        if ("GET".equals(method)) {
                            //get??????

                            val paramNames: Set<String> = httpUrl.queryParameterNames
                            val paramMap: HashMap<String, String> = hashMapOf()

                            if (paramNames.isNotEmpty()) {
                                paramNames.forEach {
                                    if (it.isNotEmpty()) {
                                        val value: String? = httpUrl.queryParameter(it)
                                        if (value.isNullOrEmpty().not()) {
                                            //????????????????????????????????????????????????
                                            paramMap.put(it, value!!);
                                        }
                                    }
                                }
                            }

                            if (paramMap.isNotEmpty()) {

                                val mapSort: List<Map.Entry<String, String>> =
                                    paramMap.entries.sortedBy { it.key }

                                val sb: StringBuilder = StringBuilder()
                                mapSort.forEach {
                                    if (StringUtil.checkStringIsNull(it.value).not()) {
                                        sb.append(it.key).append("=").append(it.value).append("&");
                                    }
                                }

                                val tempStr = sb.toString()
                                jiaoYanStr = tempStr.substring(0, tempStr.length - 1);

                            }

                            LogUtil.e("GET ?????? jiaoYanStr = $jiaoYanStr")

                        } else if ("POST".equals(method)) {
                            //post??????

                            val requestBody: RequestBody? = request.body

                            if (requestBody != null) {
                                //??????????????????

                                if (requestBody is FormBody) {
                                    //????????????

                                    val formBody: FormBody = requestBody as FormBody

                                    val size = formBody.size

                                    var postParamMap: HashMap<String, String> = hashMapOf()

                                    for (i in 0 until size) {
                                        postParamMap.put(formBody.name(i), formBody.value(i))
                                    }

                                    if (postParamMap.isNotEmpty()) {

                                        val mapSort: List<Map.Entry<String, String>> =
                                            postParamMap.entries.sortedBy { it.key }

                                        val sb: StringBuilder = StringBuilder()
                                        mapSort.forEach {
                                            if (StringUtil.checkStringIsNull(it.value).not()) {
                                                sb.append(it.key).append("=").append(it.value)
                                                    .append("&");
                                            }
                                        }

                                        val tempStr = sb.toString()
                                        jiaoYanStr = tempStr.substring(0, tempStr.length - 1);

                                    }

                                    LogUtil.e("post ???????????? jiaoYanStr = $jiaoYanStr")


                                } else {
                                    //??????????????????post ???Json ??????

                                    try {
                                        val buffer: Buffer = Buffer()
                                        requestBody.writeTo(buffer)

                                        val oldParams: String = buffer.readUtf8() //?????? ????????? json ?????????

                                        if (oldParams.isNotEmpty()) {

                                            val jsonObject: JSONObject = JSONObject(oldParams)

                                            //JSON????????????????????? implementation 'com.alibaba:fastjson:1.2.58'
                                            /**
                                             * JSON????????????????????? implementation 'com.alibaba:fastjson:1.2.58'
                                             *
                                             * ?????????????????????????????? as HashMap???????????????
                                             * java.lang.ClassCastException: com.alibaba.fastjson.JSONObject cannot be cast to java.util.HashMap
                                             */
                                            val pMap =
                                                JSON.parse(oldParams) as Map<String, String>

                                            if (pMap.isNotEmpty()) {

                                                val mapSort: List<Map.Entry<String, String>> =
                                                    pMap.entries.sortedBy { it.key }

                                                val sb: StringBuilder = StringBuilder()
                                                mapSort.forEach {
                                                    if (StringUtil.checkStringIsNull(it.value).not()) {
                                                        sb.append(it.key).append("=")
                                                            .append(it.value)
                                                            .append("&");
                                                    }
                                                }

                                                val tempStr = sb.toString()
                                                jiaoYanStr =
                                                    tempStr.substring(0, tempStr.length - 1);

                                            }

                                        } else {
                                            jiaoYanStr = ""
                                        }

                                        LogUtil.e("post ??????????????? jiaoYanStr = $jiaoYanStr")

                                    } catch (e: Exception) {
                                        LogUtil.e("e = $e")
                                        jiaoYanStr = ""
                                    }

                                }

                            } else {
                                jiaoYanStr = ""
                            }

                        } else {
                            //????????????????????????
                            jiaoYanStr = ""
                        }

                    } catch (e: Exception) {
                        //??????????????????????????????
                        jiaoYanStr = ""
                    }

                }

                //?????????abc=abc&mobile=123123&nnnqqq=nnnqqq&pwd=a1234567
                LogUtil.e("jiaoYanStr = $jiaoYanStr")
                if (jiaoYanStr.isNotEmpty()) {
                    mbu.addHeader("JiaoYanStr", jiaoYanStr)
                }

                val req = mbu.build()

                return chain.proceed(req)

            }

        })

        /**
         * ??????????????????????????????????????????
         *
         * api 'com.safframework.log:saf-logginginterceptor:1.4.2'
         */
        val loggingInterceptor = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
            .build()
        builder.addInterceptor(loggingInterceptor)

        if (mMonitor != null) {
            builder.addNetworkInterceptor(mMonitor!!)
        }
        if (mDispatcherExecutorService != null) {
            val dispatcher = Dispatcher(mDispatcherExecutorService!!)
            if (mDispatcherMaxRequest > 0) {
                dispatcher.maxRequests = mDispatcherMaxRequest
            }
            if (mDispatcherMaxRequestPerHost > 0) {
                dispatcher.maxRequestsPerHost = 5
            }

            builder.dispatcher(dispatcher)
        }
        return builder.build()
    }


    private fun checkBaseClientNotNull() {
        if (sOkHttpClient == null) {
            sOkHttpClient = OkHttpClient()
        }
    }

}
