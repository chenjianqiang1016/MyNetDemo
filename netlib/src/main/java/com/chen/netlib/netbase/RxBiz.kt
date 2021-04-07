package com.chen.netlib.netbase


import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RxBiz private constructor() {

    init {
        throw IllegalStateException("Can't instance the RxBiz")
    }

    companion object {

        private val TAG = RxBiz::class.java.simpleName

        fun dispose(@Nullable disposable: Disposable) {
            if (isNotDisposed(disposable)) {
                disposable.dispose()
            }
        }

        fun isNotDisposed(@Nullable disposable: Disposable?): Boolean {
            return disposable != null && !disposable.isDisposed
        }

        /**
         * 实现io 线程和 主线程的变换
         *
         * @param <T>
         * @return
        </T> */
        fun <T> transformMain(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> singleTransformMain(): SingleTransformer<T, T> {
            return SingleTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> flowableTransformMain(): FlowableTransformer<T, T> {
            return FlowableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> maybeTransformMain(): MaybeTransformer<T, T> {
            return MaybeTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun completableTransformMain(): CompletableTransformer {
            return CompletableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }


}
