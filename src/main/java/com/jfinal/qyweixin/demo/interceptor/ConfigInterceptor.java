/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.qyweixin.demo.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.qyweixin.demo.model.Config;
import com.jfinal.qyweixin.demo.service.ConfigService;

/**
 * @author Javen
 * 2017年6月11日
 */
public class ConfigInterceptor implements Interceptor {
	static ConfigService srv = ConfigService.me;
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		String rmid = controller.getPara("rmid");
		if (StrKit.isBlank(rmid)) {
			throw new IllegalArgumentException("rmid 值不能为空"); 
		}
		Config config = srv.getConfigByRmid(rmid);
		controller.setSessionAttr("config", config);
		if (null == config) {
			throw new IllegalArgumentException("rmid："+rmid+" 值不能用，请联系管理员"); 
		}
		srv.getApiConfig(config);
		inv.invoke();
	}

}
