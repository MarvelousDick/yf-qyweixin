package com.jfinal.qyweixin.demo.parse;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.qyweixin.demo.model.Config;
import com.jfinal.qyweixin.demo.service.ConfigService;
import com.jfinal.qyweixin.sdk.jfinal.AgentIdParser;

public class CustomAgentIdParser implements AgentIdParser {
    private static final String DEFAULT_AGENT_ID_KEY = "agentId";
    
    private final String agentIdKey;
    
    static ConfigService cs = ConfigService.me;
    
    public CustomAgentIdParser() {
        this.agentIdKey = DEFAULT_AGENT_ID_KEY;
    }

    public CustomAgentIdParser(String agentIdKey) {
        this.agentIdKey = agentIdKey;
    }
    
	@Override
	public String getAgentId(Invocation inv) {
		return getAgentId(inv.getController());
	}

	@Override
	public String getAgentId(Controller ctl) {
		String  agentId = ctl.getAttr(agentIdKey);
		System.out.println("CustomAgentIdParser agentId:"+agentId);
		//从数据库中查询配置
		Config config = cs.getConfigByRmid(agentId);
		System.out.println(JsonKit.toJson(config));
		return config.getAgentId();
	}

}
