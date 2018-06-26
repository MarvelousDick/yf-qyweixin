package com.jfinal.qyweixin.demo.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.qyweixin.demo.interceptor.*;


public class YfProductDetailController extends Controller {

    @Before(ProductDetailInterceptor.class)
    public void getprodetail() {
        renderJson();
    }

    @Before(UserRightInterceptor.class)
    public void getuserright() {
        renderJson();
    }

    @Before(ProductDetailUpdateInterceptor.class)
    public void updateprod() {
        renderJson();
    }

    @Before(GetDataFromExcelInterceptor.class)
    public void savedatafromexcel() {
        renderJson();
    }

    @Before(GetDataDesktopInterceptor.class)
    public void getdatadesktop() {
        renderJson();
    }

    @Before(GetQueryResultInterceptor.class)
    public void getresult() {
        renderJson();
    }
}