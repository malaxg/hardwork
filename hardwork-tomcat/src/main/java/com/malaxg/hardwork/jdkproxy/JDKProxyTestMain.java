package com.malaxg.hardwork.jdkproxy;

import java.lang.reflect.Proxy;

public class JDKProxyTestMain {
	public static void main(String[] args) {
		// 此时系统中只有被代理和它实现的接口，并没有代理类，代理类通过Proxy自动生成
		// 计算类需要被代理？ 被代理做什么？ 添加日志 其实这里就是InvocationHandler
		Jisuan jisuan = new Jisuan();
		LogProxy logProxy = new LogProxy(jisuan);

		//获取被代理类的类的类加载器及它所实现的接口，来动态的生成一个类
		ClassLoader classLoader = jisuan.getClass().getClassLoader();
		Class<?>[] interfaces = jisuan.getClass().getInterfaces();

		//使用Proxy动态的来生成一个代理类\
		Operation o = (Operation) Proxy.newProxyInstance(classLoader, interfaces, logProxy);
		//动态生成的代理类的名字是什么？
		System.out.println(o.add(1, 9));
		System.out.println(o.getClass().getName());

	}
}
