package com.malaxg.hardwork;

import java.util.Arrays;

import com.malaxg.hardwork.model.User;
import com.malaxg.hardwork.util.ReflectionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TomcatApplication {

	public static void main(String[] args)
		throws IllegalAccessException
	{
		User user = new User();
		
		User user1 = new User();
		user1.setId("5445");
		user1.setAge(51);
		user1.setColor("blue");
		
		boolean b = ReflectionUtil.copyNotNullFileds(Arrays.asList(user), user1);
		
		
		SpringApplication.run(TomcatApplication.class, args);
	}
}
