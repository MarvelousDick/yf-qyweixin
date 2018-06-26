package com;
/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */


import com.jfinal.core.JFinal;

/**
 * 
 * @author Javen
 * 2017年7月8日
 */
public class AppRun {

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8080, "/", 5);// 启动配置项
	}

}
