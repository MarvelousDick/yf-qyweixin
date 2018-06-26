package com.jfinal.qyweixin.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.demo.interceptor.ConfigInterceptor;
import com.jfinal.qyweixin.demo.User;
import com.jfinal.qyweixin.demo.model.Config;
import com.jfinal.qyweixin.sdk.api.*;
import com.jfinal.qyweixin.sdk.api.ChatApi.ChatUrl;
import com.jfinal.qyweixin.sdk.api.media.MediaApi;
import com.jfinal.qyweixin.sdk.api.media.MediaApi.MediaType;
import com.jfinal.qyweixin.sdk.api.media.MediaFile;
import com.jfinal.qyweixin.sdk.jfinal.ApiController;
import com.jfinal.qyweixin.sdk.menu.MenuManager;
import com.jfinal.qyweixin.sdk.msg.chat.ChatReceiver;
import com.jfinal.qyweixin.sdk.msg.chat.ChatReceiver.ChatType;
import com.jfinal.qyweixin.sdk.msg.chat.TextChat;
import com.jfinal.qyweixin.sdk.msg.chat.TextChatMsg;
import com.jfinal.qyweixin.sdk.msg.send.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Before(ConfigInterceptor.class)
public class QyWeixinApiController extends ApiController {
	private static final Log log=Log.getLog(QyWeixinApiController.class);

	/**
	 * 发送文本消息
	 */
	public void sendTextMssage() {
		Config config = (Config)getSessionAttr("config");
		QiYeTextMsg text=new QiYeTextMsg();
		text.setAgentid(config.getAgentId());
		text.setText(new Text("测试消息"));
		text.setSafe("1");
		text.setTouser("@all");
		ApiResult sendTextMsg = SendMessageApi.sendTextMsg(text);
		renderText(sendTextMsg.getJson());
	}
	/**
	 * 图文混排的消息
	 */
	public void sendNewsMessage(){
		Config config = (Config)getSessionAttr("config");
		QiYeNewsMsg qiYeNewsMsg=new QiYeNewsMsg();
		qiYeNewsMsg.setAgentid(config.getAgentId());
		qiYeNewsMsg.setArticleCount(1);
		qiYeNewsMsg.setSafe("0");
		qiYeNewsMsg.setTouser("@all");
		
		News news=new News();
		List<Article> articles=new ArrayList<Article>();
		Article article=new Article();
		article.setTitle("微信开发专栏");
		article.setDescription("微信开发专栏");
		article.setPicurl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497186578387&di=f4cbb821da6ecf3e071c12f617bda1ff&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2014%2Fmxy%2F02%2F11%2F4%2F4.jpg");
		article.setUrl("http://blog.csdn.net/column/details/14826.html");
		articles.add(article);
		
		article=new Article();
		article.setTitle("IJPay 让支付触手可及，实现微信、支付宝系列支付");
		article.setDescription("IJPay 让支付触手可及，实现微信、支付宝系列支付");
		article.setPicurl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497188779665&di=3dd9bb303371099861467ca6e2a67c34&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1308%2F17%2Fc6%2F24564406_1376704633091.jpg");
		article.setUrl("http://git.oschina.net/javen205/IJPay");
		articles.add(article);
		news.setArticles(articles);
		
		
		qiYeNewsMsg.setNews(news);
		
		ApiResult sendTextMsg = SendMessageApi.sendNewsMsg(qiYeNewsMsg);
		renderText(sendTextMsg.getJson());
	}
	/**
	 * 发送图片
	 */
	public void sendImage(){
		Config config = (Config)getSessionAttr("config");
		
		QiYeImageMsg image=new QiYeImageMsg();
		image.setAgentid(config.getAgentId());
		image.setSafe("0");
		image.setTouser("Javen");
		image.setMedia_id("1jwHxr9qU40IcyOMPoeQDtBHa9pvqJZGofzUhA1Yhd2AWxRCjrgyGZjNSKwZvKsqSDWeoWmNF3tkc05aBbXVmsg");
		ApiResult apiResult = SendMessageApi.sendImageMsg(image);
		renderText(apiResult.getJson());
	}
	/**
	 * 发送文件
	 */
	public void sendFile(){
		QiYeFileMsg file=new QiYeFileMsg();
		file.setAgentid("16");
		file.setMedia_id("1g45y7tvRx9dyk3jnaiMl5XR48dBcrPkl3SxfNJYC4mf3AYb6yLqs_dG1mK1mXVEzQ5zOprkWoF01x2uP290E2g");
		file.setSafe("0");
		file.setTouser("@all");
		ApiResult apiResult = SendMessageApi.sendFileMsg(file);
		renderText(apiResult.getJson());
	}
	
	/**
	 * 获取菜单
	 */
	public void getMenuApi(){
		renderText(MenuApi.getMenu("22").getJson());
	}
	
	/**
	 * 删除菜单
	 */
	public void deleteMenuApi(){
		renderText(MenuApi.deleteMenu("22").getJson());
	}
	/**
	 * 创建菜单
	 */
	public void creatMenuApi(){
		String json=JsonKit.toJson(MenuManager.getMenu()).toString();
		log.error(json);
		System.out.println(json);
		renderText(MenuApi.createMenu(json,"22").getJson());
	}
	
	/**
	 * 获取指定部分列表
	 * {"errcode":0,"errmsg":"ok","department":[{"id":1,"name":"企业号体验34797078","parentid":0,"order":200},{"id":2,"name":"研发","parentid":1,"order":200}]}
	 */
	public void getDepartment(){
		ApiResult apiResult=ConDepartmentApi.getDepartment("1");
		renderText(apiResult.getJson());
	}
	
	/**
	 * 创建部门
	 */
	public void createDepartment(){
		String data="{\"name\": \"XX研发中心\",\""
	   +"parentid\": \"1\",\""
	   +"order\": \"2\",\""
	   +"id\": \"3\"}";
		ApiResult apiResult=ConDepartmentApi.createDepartment(data);
		
		log.error(data);
		renderText(apiResult.getJson());
	}
	
	/**
	 * 更新部门
	 */
	public void updateDepartment(){
		String data="{\"name\": \"北京研发中心\",\""
	   +"parentid\": \"1\",\""
	   +"order\": \"1\",\""
	   +"id\": \"5\"}";
		ApiResult apiResult=ConDepartmentApi.updateDepartment(data);
		
		log.error(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 删除部门
	 */
	public void deleteDepartment(){
		ApiResult apiResult=ConDepartmentApi.deleteDepartment("6");
		renderText(apiResult.getJson());
	}
	/**
	 * 获取成员
	 */
	public void getUser(){
		ApiResult apiResult = ConUserApi.getUser("Javen68");
		renderText(apiResult.getJson());
	}
	
	/**
	 * 创建成员
	 */
	public void createUser(){
		String json="{\"userid\": \"Javen205\","
   +"\"name\": \"Javen205\","
   +"\"department\": [3],"
   +"\"position\": \"产品经理\","
   +"\"mobile\": \"\","
   +"\"gender\": \"1\","
   +"\"email\": \"\","
   +"\"weixinid\": \"Javen205\","
  // +"\"avatar_mediaid\": \"2-G6nrLmr5EC3MNb_-zL1dDdzkd0p7cNliYu9V5w7o8K0\","
   +"\"extattr\": {\"attrs\":[{\"name\":\"爱好\",\"value\":\"旅游\"},{\"name\":\"卡号\",\"value\":\"1234567234\"}]}}";
		System.out.println(json);
		ApiResult apiResult = ConUserApi.createUser(json);
		renderText(apiResult.getJson());
	}
	
	/**
	 * 更新成员
	 */
	public void updateUser(){
		String json="{\"userid\": \"Javen205\","
   +"\"name\": \"Javen205_test\","
   +"\"department\": [3],"
   +"\"position\": \"产品经理\","
   +"\"mobile\": \"\","
   +"\"gender\": \"1\","
   +"\"email\": \"342796937@qq.com\","
   +"\"weixinid\": \"Javen205\","
  // +"\"avatar_mediaid\": \"2-G6nrLmr5EC3MNb_-zL1dDdzkd0p7cNliYu9V5w7o8K0\","
   +"\"extattr\": {\"attrs\":[{\"name\":\"爱好\",\"value\":\"旅游\"},{\"name\":\"卡号\",\"value\":\"1234567234\"}]}}";
		System.out.println(json);
		ApiResult apiResult = ConUserApi.updateUser(json);
		renderText(apiResult.getJson());
	}
	/**
	 * 删除成员
	 */
	public void deleteUser(){
		ApiResult apiResult = ConUserApi.deleteUser("lisi");
		renderText(apiResult.getJson());
	}
	/**
	 * 批量删除成员
	 */
	public void batchDeleteUser(){
		String data="{"+
   "\"useridlist\": [\"zhangsan\", \"lisi\"]\"}";
		ApiResult apiResult = ConUserApi.batchDeleteUser(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 获取部门成员
	 */
	public void getDepartmentUserSimpleList(){
		ApiResult apiResult = ConUserApi.getDepartmentUserSimpleList("1", "1", "0");
		renderText(apiResult.getJson());
	}
	/**
	 * 获取部门成员(详情)
	 */
	public void getDepartmentUserList(){
		ApiResult apiResult = ConUserApi.getDepartmentUserList("1", "1", "0");
		System.out.println(apiResult.getJson());
		if (apiResult.isSucceed()) {
			JSONObject object= JSON.parseObject(apiResult.getJson());
			JSONArray jsonArray = object.getJSONArray("userlist");
			if (null!=jsonArray && jsonArray.size()>0) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject userObject = jsonArray.getJSONObject(i);
					User user = JSON.toJavaObject(userObject, User.class);
					System.out.println(user.toString());
				}
			}
			
		}
		
		
		renderText(apiResult.getJson());
	}
	/**
	 * 邀请成员关注 测试貌似只能使用邮箱要求
	 */
	public void inviteUser(){
		String data="{\"userid\":\"Javen205\"}";
		System.out.println(data);
		ApiResult apiResult = ConUserApi.inviteUser(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 创建标签
	 */
	public void createTag(){
		String data="{\"tagname\": \"UI\",\"tagid\": id}";
		System.out.println(data);
		ApiResult apiResult = ConTagApi.createTag(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 更新标签名字
	 */
	public void updateTag(){
		String data="{\"tagname\": \"UI-test\",\"tagid\": 1}";
		System.out.println(data);
		ApiResult apiResult = ConTagApi.updateTag(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 删除标签
	 */
	public void deleteTag(){
		ApiResult apiResult = ConTagApi.deleteTag("1");
		renderText(apiResult.getJson());
	}
	/**
	 * 获取标签成员
	 */
	public void getTagUser(){
		ApiResult apiResult = ConTagApi.getTagUser("1");
		renderText(apiResult.getJson());
	}
	/**
	 * 增加标签成员
	 */
	public void addTagUsers(){
		String data="{\"tagid\": \"1\","+
					   "\"userlist\":[ \"Javen\",\"lisi\"],"+
					   "\"partylist\": []}";
		System.out.println(data);
		ApiResult apiResult = ConTagApi.addTagUsers(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 删除标签成员
	 */
	public void delTagUser(){
		String data="{\"tagid\": \"1\","+
				   "\"userlist\":[ \"Javen\",\"lisi\"],"+
				   "\"partylist\": []}";
		System.out.println(data);
		ApiResult apiResult = ConTagApi.deleteTagUsers(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 获取标签列表
	 */
	public void getTagList(){
		ApiResult apiResult = ConTagApi.getTagList();
		renderText(apiResult.getJson());
	}
	/**
	 * 邀请成员关注
	 */
	public void inviteUsers(){
		String data="{"
			+"\"touser\":\"lisi|Javen\","
			+"\"toparty\":\"3\","
			+"\"totag\":\"1\","
			+"\"callback\":"
			+"{"
			+" 	\"url\": \"http://javen.ngrok.natapp.cn/qymsg\","
			+" 	\"token\": \"Javen\","
			+" 	\"encodingaeskey\": \"sPqS4op3rKjOT7XbWJkDr5Kqq6v6oL3enZ8oY6hrK8b\""
			+"}"
			+"	}";
		System.out.println(data);
		ApiResult apiResult=ConBatchApi.inviteUsers(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 获取异步任务结果
	 */
	public void batchGetResult(){
		ApiResult apiResult=ConBatchApi.batchGetResult("DJuuczAtDK_5ryoQ0_e8YwzG-IosiijUjVYk3EjAuhQ");
		renderText(apiResult.getJson());
	}
	
	
	public void uploadFile(){
		ApiResult apiResult=MediaApi.uploadMedia(MediaType.FILE, new File("/Users/Javen/Documents/batch_user_sample.csv"));
		renderText(apiResult.getJson());
	}
	
	/**
	 * 1jwHxr9qU40IcyOMPoeQDtBHa9pvqJZGofzUhA1Yhd2AWxRCjrgyGZjNSKwZvKsqSDWeoWmNF3tkc05aBbXVmsg
	 */
	public void uploadImage(){
		ApiResult apiResult=MediaApi.uploadMedia(MediaType.IMAGE, new File("/Users/Javen/Downloads/网络微生活.jpg"));
		String json = apiResult.getJson();
		String mediaId=JSON.parseObject(json).getString("media_id");
		QiYeImageMsg image=new QiYeImageMsg(mediaId);
		image.setAgentid("22");
		image.setSafe("0");
		image.setTouser("Javen");
		ApiResult sendImageMsg = SendMessageApi.sendImageMsg(image);
		
		renderText(json+"  "+sendImageMsg.getJson());
	}
	
	
	public void getMediaDate()
	{
		try {
			MediaFile mediaFile = MediaApi.getMedia("1jwHxr9qU40IcyOMPoeQDtBHa9pvqJZGofzUhA1Yhd2AWxRCjrgyGZjNSKwZvKsqSDWeoWmNF3tkc05aBbXVmsg");
			renderText(mediaFile.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getMaterialCount(){
		ApiResult apiResult = MediaApi.getMaterialCount("22");
		renderText(apiResult.getJson());
	}
	public void batchGetMaterial(){
		ApiResult apiResult = MediaApi.batchGetMaterial(MediaType.IMAGE, 0, 20,22);
		renderText(apiResult.getJson());
	}
	public void updateSyncUser(){
		String data="{"
			+"\"media_id\":\"1g45y7tvRx9dyk3jnaiMl5XR48dBcrPkl3SxfNJYC4mf3AYb6yLqs_dG1mK1mXVEzQ5zOprkWoF01x2uP290E2g\","
			+"\"callback\":"
			+"{"
			+" 	\"url\": \"http://javen.ngrok.natapp.cn/qymsg\","
			+" 	\"token\": \"Javen\","
			+" 	\"encodingaeskey\": \"sPqS4op3rKjOT7XbWJkDr5Kqq6v6oL3enZ8oY6hrK8b\""
			+"}"
			+"	}";
		System.out.println(data);
		ApiResult apiResult=ConBatchApi.updateSyncUser(data);
		renderText(apiResult.getJson());
	}
	
	/**
	 * 获取企业号应用
	 */
	public void getAgent(){
		ApiResult apiResult=AgentApi.getAgent("22");
		renderText(apiResult.getJson());
	}
	/**
	 * 设置企业号应用
	 */
	public void setAgent(){
		String data="{"+
		   "\"agentid\": \"22\","+
		   "\"report_location_flag\": \"1\","+
		  // "\"logo_mediaid\": \"xxxxx\","+
		   "\"name\": \"智慧云端日记\","+
		   "\"description\": \"企业号测试应用\","+
		   "\"redirect_domain\": \"javen.ngrok.natapp.cn\","+
		   "\"isreportuser\":1,"+
		   "\"isreportenter\":1}";
			System.out.println(data);
		ApiResult apiResult=AgentApi.setAgent(data);
		renderText(apiResult.getJson());
	}
	/**
	 * 获取应用概况列表
	 */
	public void getListAgent(){
		ApiResult apiResult=AgentApi.getListAgent();
		renderText(apiResult.getJson());
	}
	/**
	 * 如果用户未关注将无法转化
	 * openid转换成userid接口
	 */
	public void toUserId(){
		String json="{\"openid\":\"oD3e5jpSC3C8Qq5uon_SEeRwc9AA\"}";
		System.out.println("json..."+json);
		ApiResult apiResult = OAuthApi.ToUserId(json);
		renderText(apiResult.getJson());
	}
	
	public void sendTextChat(){
		TextChat textChat=new TextChat();
		
		ChatReceiver receiver=new ChatReceiver();
		receiver.setType(ChatType.single);
		receiver.setId("Javen");
		
		textChat.setReceiver(receiver);
		textChat.setSender("Javen");
		textChat.setText(new TextChatMsg("企业会话消息测试....."));
		String data=JSON.toJSONString(textChat);
		System.out.println("data:"+data);
		ApiResult apiResult = ChatApi.Chat(ChatUrl.sendUrl, data);
		renderText(apiResult.getJson());
	}
}

