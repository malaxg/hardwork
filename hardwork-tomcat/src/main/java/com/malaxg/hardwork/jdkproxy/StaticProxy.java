package com.malaxg.hardwork.jdkproxy;

public class StaticProxy implements Operation {
	//被代理对象
	private Operation operation;

	public StaticProxy(Operation operation) {
		this.operation = operation;
	}

	@Override
	public int add(int i, int j) {
		System.out.println("开始代理add方法");
		int add = operation.add(i, j);
		System.out.println("结束代理add方法");
		return add;
	}

	@Override
	public int sub(int i, int j) {
		return 0;
	}
}
