package com.chen.netlib.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.chen.netlib.listener.OnAvoidDoubleClickListener

/**
 * Toast 扩展方法
 *
 * @context
 * @msg
 */
fun Context.toast(msg: String) {
    this?.let {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

/**
 * 点击事件扩展方法
 *
 * 使用时，只需：
 * view.click{
 *  ...
 * }
 *
 * 点击一次后，默认600毫秒内不可以再次点击
 */
fun View.click(block: (View) -> Unit) {
    this.setOnClickListener(object : OnAvoidDoubleClickListener() {
        override fun onNoDoubleClick(v: View?) {
            v?.apply {
                block(v)
            }
        }
    })
}
