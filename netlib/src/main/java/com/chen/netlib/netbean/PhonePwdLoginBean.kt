package com.chen.netlib.netbean

data class PhonePwdLoginBean(
    var info: Info?,
    var mobile_status: String? = "",
    var token: String? = ""
) {

    data class Info(
        var avater: String? = "",
        var mobile: String? = "",
        var nickname: String? = "",
        var uid: String? = ""
    )

}