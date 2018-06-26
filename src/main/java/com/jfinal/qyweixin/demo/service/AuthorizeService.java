/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.qyweixin.demo.service;

import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.qyweixin.demo.model.Authorize;

/**
 * 授权配置信息
 * @author Javen
 * 2017年7月8日
 */
public class AuthorizeService {
	static Log log = Log.getLog(AuthorizeService.class);
	public static final AuthorizeService me = new AuthorizeService();
	private final Authorize dao = new Authorize().dao();
	
	/**
	 * 根据appId 查询授权配置信息
	 * @param appId
	 * @return
	 */
	public Authorize getByAgentId(String agentId){
		Kv para = Kv.by("agent_id",agentId).set("status",1);
		SqlPara sqlPara = dao.getSqlPara("authorize.getAuthorizeByAgentId", para);
		return dao.findFirst(sqlPara);
	}
	
}
