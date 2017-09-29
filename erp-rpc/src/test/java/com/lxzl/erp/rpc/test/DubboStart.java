package com.lxzl.erp.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboStart {
	 private static String configPath = "classpath*:spring-config.xml";
	    public static void main(String[] args) {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { configPath });
	        context.start();
	        System.out.println("=======================dubbo 服务启动完毕======================");
	        try {
	            System.in.read(); // 按任意键退出
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
