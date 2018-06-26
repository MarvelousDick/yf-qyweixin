/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.qyweixin.demo.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.qyweixin.demo.model.Config;
import com.jfinal.qyweixin.sdk.api.ApiConfig;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;

/**
 * 
 * @author Javen
 * 2017年7月8日
 */
public class ConfigService {
	public static final ConfigService me = new ConfigService();
	private final Config dao = new Config().dao();
	/**
	 * 根据随机数查询微信配置信息
	 * @param rmid
	 * @return
	 */
	public Config getConfigByRmid(String rmid){
		Kv para = Kv.by("rmid", rmid).set("status", 1);
		SqlPara sqlPara = dao.getSqlPara("config.getConfigByRmid",para);
		return dao.findFirst(sqlPara);
	}
	
	/**
	 * @param config
	 */
	public void getApiConfig(Config config) {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(config.getToken());
		ac.setAgentId(config.getAgentId());
		ac.setCorpSecret(config.getSecret());
		ac.setCorpId(config.getCorpId());
		ac.setEncryptMessage(true);
		ac.setEncodingAesKey(config.getEncodingaeskey());
		
		ApiConfigKit.setThreadLocalAgentId(config.getAgentId());
		ApiConfigKit.putApiConfig(ac);
	}
}
