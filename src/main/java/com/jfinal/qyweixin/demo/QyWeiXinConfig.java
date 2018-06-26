package com.jfinal.qyweixin.demo;

import java.io.File;
import java.sql.Connection;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.qyweixin.demo.controller.*;
import com.jfinal.qyweixin.demo.interceptor.CrossRegionInterceptor;
import com.jfinal.qyweixin.demo.model._MappingKit;
import com.jfinal.qyweixin.sdk.api.ApiConfig;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;


public class QyWeiXinConfig extends JFinalConfig {
	private static Prop p = loadProp();

	public static Prop loadProp() {
		try {
			return PropKit.use("a_little_config_pro.txt");
		} catch (Exception e) {
			return PropKit.use("a_little_config.txt");
		}
	}

	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();

		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setCorpId(PropKit.get("corpId"));
		ac.setCorpSecret(PropKit.get("secret"));
		ac.setAgentId(PropKit.get("agentId"));

		/**
		 * 是否对消息进行加密，对应于微信平台的消息加解密方式： 1：true进行加密且必须配置 encodingAesKey
		 * 2：false采用明文模式，同时也支持混合模式
		 * 
		 * 目前企业号只支持加密且必须配置
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", true));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		me.setDevMode(p.getBoolean("devMode", false));
		me.setEncoding("utf-8");
		me.setEncoding("utf-8");
		me.setViewType(ViewType.JSP);
		// 设置上传文件保存的路径
		me.setBaseUploadPath(PathKit.getWebRootPath() + File.separator + "myupload");
		me.setError404View("/WEB-INF/error/404.html");
		me.setError500View("/WEB-INF/error/500.html");
		// ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
		ApiConfigKit.setDevMode(me.getDevMode());
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", QyIndexController.class); // 第三个参数为该Controller的视图存放路径 /表示绝对路径
		me.add("/yfoauth", YfOAuthController.class, "/");
		me.add("/yfjssdk", YfJssdkController.class, "jsp");
		me.add("/yfprod", YfProductDetailController.class, "/");
		me.add("/qymsg", QyWeixinMsgController.class);
		me.add("/qyapi", QyWeixinApiController.class);
		me.add("/qyoauth", QyOAuthController.class,"/");
		me.add("/qyjssdk", QyJssdkController.class);
	}

	/**
	 * 抽取成独立的方法，例于 _Generator 中重用该方法，减少代码冗余
	 */
	public static DruidPlugin getDruidPlugin() {
		String jdbcUrl = p.get("jdbcUrl");
		String user = p.get("user");
		String password = p.get("password").trim();
		System.out.println("load data source: " + jdbcUrl + " " + user + " " + password);
		return new DruidPlugin(jdbcUrl, user, password);
	}

	public static DruidPlugin createDruidPlugin() {

		// 配置druid数据连接池插件
		DruidPlugin dp = getDruidPlugin();
		// 配置druid监控
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType(JdbcUtils.MYSQL);
		dp.addFilter(wall);
		return dp;
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置ActiveRecord插件
		DruidPlugin druidPlugin = createDruidPlugin();
		me.add(druidPlugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 方言
		arp.setDialect(new MysqlDialect());
		// 事务级别
		arp.setTransactionLevel(Connection.TRANSACTION_REPEATABLE_READ);
		// 统一全部默认小写
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		// 是否显示SQL
		arp.setShowSql(p.getBoolean("devMode", false));

		arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
		arp.addSqlTemplate("all_sqls.sql");
		_MappingKit.mapping(arp);
		me.add(arp);
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new CrossRegionInterceptor());
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {

	}

	@Override
	public void afterJFinalStart() {
		// 单个应用可以直接在启动之后添加 添加应用配置
		ApiConfigKit.putApiConfig(getApiConfig());
		//设置无效
//		ApiInterceptor.setAgentIdParser(new CustomAgentIdParser("rmid"));
	}

	@Override
	public void configEngine(Engine me) {

	}

}
