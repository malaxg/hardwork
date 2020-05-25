package com.malaxg.hardwork.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogProxy implements InvocationHandler {

	private Object target;

	public LogProxy(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("日志记录" + System.currentTimeMillis());
		Object invoke = method.invoke(target, args);
		System.out.println("日志记录" + System.currentTimeMillis());
		return invoke;
	}
}
