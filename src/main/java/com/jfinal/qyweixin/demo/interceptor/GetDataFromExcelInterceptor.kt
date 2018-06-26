package com.jfinal.qyweixin.demo.interceptor

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.TypeReference
import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation
import com.jfinal.qyweixin.demo.model.CrystalStickStatistics
import org.omg.CORBA.Object

class GetDataFromExcelInterceptor : Interceptor {
    override fun intercept(inv: Invocation?) {

        inv?.invoke()

        val controller = inv!!.controller
        val excelTableJson = JSON.parse(controller.getPara("excelFileJsonArray")) as JSONArray

        var excelRecordObj: Map<String, Object>

        var allInserted: Boolean = true

        try {
            for (excelRecord in excelTableJson) {
//            println(excelRecord)
                excelRecordObj = JSON.parseObject(excelRecord.toString(), object : TypeReference<Map<String, Object>>() {})
                var upload = CrystalStickStatistics()._setAttrs(excelRecordObj).save()
                if (!upload){
                    allInserted = false
                }
            }
        }catch (e: Exception){
            print(e)
        }finally {
            print(allInserted)
        }

    }
}