package com.jfinal.qyweixin.demo.interceptor

import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation
import com.jfinal.kit.HashKit
import com.jfinal.qyweixin.sdk.api.ApiConfigKit
import com.jfinal.qyweixin.sdk.api.JsTicketApi

import java.util.*


class QrScanInterceptor : Interceptor {

    override fun intercept(inv: Invocation?) {
        inv?.invoke()
        val controller = inv?.controller
        val jsApiTicket = JsTicketApi.getTicket(JsTicketApi.JsApiType.jsapi)
        val jsapi_ticket = jsApiTicket.ticket
        val nonce_str = create_nonce_str()
        // 注意 URL 一定要动态获取，不能 hardcode.
//        var url = ("http://" + controller!!.request.serverName // 服务器地址
//
//                // + ":"
//                // + getRequest().getServerPort() //端口号
//                + controller.request.contextPath // 项目名称
//                + controller.request.servletPath)// 请求页面或其他地址

        var url = "http://${controller!!.request.serverName}${controller.request.contextPath}${controller.request.servletPath}"

        val qs = controller.request.queryString // 参数
        if (qs != null) {
            url = url + "?" + controller.request.queryString
        }

        url = controller.getPara("url")

        println("url>>>>" + url)
        val timestamp = create_timestamp()
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        //注意这里参数名必须全部小写，且必须有序
//        val str = ("jsapi_ticket=" + jsapi_ticket +
//                "&noncestr=" + nonce_str +
//                "&timestamp=" + timestamp +
//                "&url=" + url)

        val str = "jsapi_ticket=${jsapi_ticket}&noncestr=${nonce_str}&timestamp=${timestamp}&url=${url}"


        val signature = HashKit.sha1(str)

        System.out.println(("appId " + ApiConfigKit.getApiConfig().corpId
                + "  nonceStr " + nonce_str + " timestamp " + timestamp))
        println("url " + url + " signature " + signature)
        println("nonceStr " + nonce_str + " timestamp " + timestamp)
        println(" jsapi_ticket " + jsapi_ticket)
        println("nonce_str  " + nonce_str)

        controller.setAttr("appId", ApiConfigKit.getApiConfig().corpId)
        controller.setAttr("nonceStr", nonce_str)
        controller.setAttr("timestamp", timestamp)
        controller.setAttr("url", url)
        controller.setAttr("signature", signature)
        controller.setAttr("jsapi_ticket", jsapi_ticket)

    }


    private fun create_timestamp(): String {
        return java.lang.Long.toString(System.currentTimeMillis() / 1000)
    }

    private fun create_nonce_str(): String {
        return UUID.randomUUID().toString()
    }

}