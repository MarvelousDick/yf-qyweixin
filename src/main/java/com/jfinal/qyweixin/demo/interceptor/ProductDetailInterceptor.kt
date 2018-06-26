package com.jfinal.qyweixin.demo.interceptor

import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation
import com.jfinal.qyweixin.demo.model.CrystalStickStatistics

class ProductDetailInterceptor : Interceptor {
    override fun intercept(inv: Invocation?) {
        inv?.invoke()
        val controller = inv!!.controller
        val productId = controller.getPara("id")

        val product = CrystalStickStatistics.dao.findFirst(
                "select * from " +
                    "qywx_crystal_stick_statistics " +
                    "where id = '${productId}'"
        )

        controller.setAttr("product", product.toJson().toString())

    }
}