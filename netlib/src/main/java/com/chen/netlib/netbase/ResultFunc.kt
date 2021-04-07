package com.chen.netlib.netbase

import io.reactivex.functions.Function

/**
 * 通过map 变换 拿出响应体中的 实体数据
 */
class ResultFunc<T> : Function<BaseBeanData<T>, T> {
    override fun apply(qxServerBean: BaseBeanData<T>): T {
        return qxServerBean.data
    }
}
