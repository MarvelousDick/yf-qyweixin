package com.jfinal.qyweixin.demo.interceptor

import com.jfinal.aop.Interceptor
import com.jfinal.aop.Invocation
import com.jfinal.qyweixin.demo.model.UserRight

class UserRightInterceptor : Interceptor {
    override fun intercept(inv: Invocation?) {
        inv?.invoke()
        val controller = inv!!.controller
        val userId = controller.getPara("user_id")

        //After production
//        val userRight = UserRight.dao.findFirst(
//                "select * from " +
//                        "qywx_user_right " +
//                        "where user_id = '${userId}'"
//        )

        //only for test
        val userRight = UserRight.dao.findFirst(
                "select * from " +
                        "qywx_user_right " +
                        "where user_id = '2'"
        )


//        print(userRight.toJson().toString())
        controller.setAttr("user_right", userRight.toJson().toString())
    }
}