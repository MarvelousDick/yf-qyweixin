#sql("getAll")
		select #(columns) from qywx_authorize  order by update_time desc
#end

#sql("getAuthorizeByAgentId")
	select * from qywx_authorize  
	where  status = #para(status)
	and agent_id = #para(agent_id)
	order by update_time desc
#end