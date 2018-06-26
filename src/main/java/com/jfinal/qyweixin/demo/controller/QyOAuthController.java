/**
 * Copyright (c) 2011-2015, Javen  (javenlife@126.com).
 *
 * Licensed under the Apache License, Version 1.0 (the "License");
 */
package com.jfinal.qyweixin.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.demo.interceptor.ConfigInterceptor;
import com.jfinal.qyweixin.demo.model.Authorize;
import com.jfinal.qyweixin.demo.model.Config;
import com.jfinal.qyweixin.demo.service.AuthorizeService;
import com.jfinal.qyweixin.demo.service.ConfigService;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;

/**
 * @author Javen 2015年12月27日
 */

public class QyOAuthController extends Controller {
	private Log logger = Log.getLog(QyOAuthController.class);
	AuthorizeService as = AuthorizeService.me;
	static ConfigService srv = ConfigService.me;
	
	// 跳转到授权页面
	@Before(ConfigInterceptor.class)
	public void toOauth() {
		String rmid = getPara("rmid");// 再通过rmid 查询APPId,这里为了方便直接将其存入session
		Config config = (Config) getSession().getAttribute("config");
		if (null != config) {
			String agentId = config.getAgentId();
			// 通过appId查询配置的授权参数
			Authorize authorize = as.getByAgentId(agentId);
			if (null != authorize) {
				boolean isUserInfo = false;
				if (authorize.getSnsapiBase() == 0 ) {
					isUserInfo = true;
				}
				StringBuffer sbf = new StringBuffer();
				sbf.append(authorize.getDomain()).append("/qyoauth").append("/").append(rmid);
				String url = OAuthApi.getAuthorizeURL(sbf.toString(), config.getCorpId(),
						authorize.getState(), agentId, isUserInfo);
				System.out.println("url>"+url);
				redirect(url);
			} else {
				renderText("authorize is error.");
			}
		} else {
			renderText("config is error.");
		}
	}

	public void custom() {
		setAttr("userId", getSession().getAttribute("userId"));
		setAttr("openId", getSession().getAttribute("openId"));
		render("custom.html");
	}

	public void index() {
		String userId = null;
		String deviceId = null;
		String openid = null;
		String state = null;
		String user_ticket = null;
		String rmid = getPara(0);
		//通过rmid查询 公众号相关的配置
		Config config = srv.getConfigByRmid(rmid);
		if (null != config) {
			String agentId = config.getAgentId();
			Authorize authorize = as.getByAgentId(agentId);
			if (authorize !=null) {
				if (!isParaBlank("code")) {
					String code = getPara("code");
					logger.info("code:" + code);
					if (!isParaBlank("state")) {
						state = getPara("state");
						logger.info(" state:" + state);
					}
					ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
					if (userInfoApiResult.isSucceed()) {
						String userInfoJson = userInfoApiResult.getJson();
						JSONObject object = JSON.parseObject(userInfoJson);
						deviceId = object.getString("DeviceId");
						try {
							userId = object.getString("UserId");
							user_ticket = object.getString("UserId");
							if (StrKit.notBlank(userId)) {
								System.out.println("userId:" + userId);
								// 如果获取userId为空 说明没有关注
								ApiResult toOpenIdApiResult = OAuthApi.ToOpenId(userId, agentId);
								System.out.println("toOpenIdApiResult:" + toOpenIdApiResult.getJson());
								if (toOpenIdApiResult.isSucceed()) {
									openid = JSON.parseObject(toOpenIdApiResult.getJson()).getString("openid");
								}
								if (authorize.getSnsapiBase() == 0) {
									ApiResult apiResult = OAuthApi.getUserInfo(user_ticket);
									logger.info(" getUserInfo:" + apiResult.getJson());
								}
							}else {
								System.out.println("非企业成员授权");
							}
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					System.out.println(userId+" "+openid+" "+deviceId);
					setSessionAttr("userId", userId);
					setSessionAttr("openId", openid);
					setSessionAttr("deviceId", deviceId);
					if (state.equals(authorize.getState())) {
						redirect(authorize.getRedirectUri());
					}else {
						redirect("/");
					}
				}else {
					renderText("code is  null");
				}
			}
		}
	}
}
