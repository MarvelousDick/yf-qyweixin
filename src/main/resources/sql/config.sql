#sql("getAllConfig")
		select #(columns) from qywx_config where status = #para(status) order by create_time desc
#end

#sql("getConfigByRmid")
	select * from qywx_config  where rmid = #para(rmid) and status = #para(status)
#end

#sql("getConfigByAgentId")
	select * from qywx_config  where  agent_id = #para(agent_id) and status = #para(status)
#end

#sql("paginate")
	select * from qywx_config  order by update_time desc
#end