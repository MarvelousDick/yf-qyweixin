package com.jfinal.qyweixin.demo.interceptor

import com.alibaba.fastjson.JSON
import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation
import com.jfinal.qyweixin.demo.model.CrystalStickStatistics

class GetQueryResultInterceptor : Interceptor {
    override fun intercept(inv: Invocation?) {
        inv?.invoke()
        val controller = inv!!.controller

        val queryListString = controller.getPara("queryList")
        val queryNumberStrnig = controller.getPara("queryNumber")

        print(queryListString)

        val productList = CrystalStickStatistics.dao.find(queryListString)

        val productNumber = CrystalStickStatistics.dao.find(queryNumberStrnig)
        val listArray = ArrayList<Any>()
        for (product in productList){
            listArray.add(JSON.parse(product.toJson()))
        }

        print(listArray)
        controller.setAttr("productList", listArray)
        controller.setAttr("productNumber", productNumber)
    }

}