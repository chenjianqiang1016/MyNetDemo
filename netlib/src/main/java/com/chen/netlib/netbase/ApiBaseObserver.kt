package com.chen.netlib.netbase

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * RxJava observer 基类
 */
abstract class ApiBaseObserver<T> : Observer<T> {

    private val TAG = ApiBaseObserver::class.java

    override fun onSubscribe(d: Disposable) {

    }

    abstract override fun onNext(t: T)

    override fun onError(throwable: Throwable) {
        //TODO: 响应出错的 统一处理
    }

    override fun onComplete() {
    }
}
