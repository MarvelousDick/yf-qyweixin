package com.jfinal.qyweixin.demo.interceptor

import com.alibaba.fastjson.JSON.parseObject
import com.alibaba.fastjson.TypeReference
import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation
import com.jfinal.qyweixin.demo.model.CrystalStickStatistics
import org.omg.CORBA.Object


class ProductDetailUpdateInterceptor : Interceptor {
    override fun intercept(inv: Invocation?) {

        inv?.invoke()
        val controller = inv!!.controller
        val updatedInfo = controller.getPara("update_info")
        val updatedInfoJson = parseObject(updatedInfo, object : TypeReference<Map<String, Object>>() {})

        var update = CrystalStickStatistics()._setAttrs(updatedInfoJson).update()

        controller.setAttr("updated", update)
        println(update)

    }
}

