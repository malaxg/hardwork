package com.malaxg.hardwork;

import lombok.Data;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-17 19:56
 */
@Data
public class Person {
	private String name;
	private int age;

	public Person() {
	}

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
}
