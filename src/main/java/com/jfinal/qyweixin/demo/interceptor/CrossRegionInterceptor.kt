package com.jfinal.qyweixin.demo.interceptor

import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation

class CrossRegionInterceptor : Interceptor {
    override fun intercept(inv: Invocation?) {
        inv?.invoke()
        val controller = inv!!.controller
        controller.response.addHeader("Access-Control-Allow-Origin", "*")
    }

}