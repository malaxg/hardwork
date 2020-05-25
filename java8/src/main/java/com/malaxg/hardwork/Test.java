package com.malaxg.hardwork;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.malaxg.hardwork.model.User;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-31 20:18
 */
public class Test {
	public static void main(String[] args) {
		User user1 = new User(1, "wangrong1", 18);
		User user2 = new User(1, "wangrong2", 25);
		User user3 = new User(1, "wangrong2", 90);

		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		//需求1：筛选出年龄>18的所有用户  输入age 输出list
		Test test = new Test();
		System.out.println(test.getAgeRantherThan(users, (User a) -> a.getAge() > 18));
		//需求2：筛选出name = wangrong2所有用户  输入name 输出list
		//其实我们都在操作某一个数据，只不过条件不一样，这个适合可以使用谓词，在数学上常常用来表示函数的一个东西
		//对某个集合进行过滤返回新的集合，可以写一个公共方法，过滤条件是什么？？？ 代码

		users.stream().filter(o -> o.getAge() > 19).collect(Collectors.toList()).toString();

		//可以将lambda表达式赋值给变量
	}

	private List getAgeRantherThan(List<User> list, Predicate<User> predicate) {
		List<User> users = new ArrayList<>();
		for (User user : list) {
			if (predicate.test(user)) {
				users.add(user);
			}
		}
		return users;
	}
}
