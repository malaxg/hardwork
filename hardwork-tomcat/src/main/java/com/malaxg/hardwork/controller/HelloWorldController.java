package com.malaxg.hardwork.controller;

import java.util.Map;

import javax.validation.Valid;

import com.malaxg.hardwork.annotation.NonRepeat;
import com.malaxg.hardwork.fegin.HardWorkGeneratorService;
import com.malaxg.hardwork.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-20 22:52
 */
@RestController
public class HelloWorldController {

	@Autowired
	private HardWorkGeneratorService hardWorkGeneratorService;


	@PostMapping("/generateCodeTemplate")
	public void index(@RequestBody Map<String, String> generator) {
		hardWorkGeneratorService.generateCodeTemplete(generator);
	}

	@RequestMapping("/user")
	public User user(String name) {
		User user = new User();
		user.setName(name);
		user.setAge(19);
		return user;
	}
	
	@PostMapping("/testRepeat")
	public String createUser(@RequestBody @NonRepeat @Valid User users, BindingResult bindingResult)
		throws IllegalAccessException
	{
		if (bindingResult.hasErrors())
		{
			bindingResult.rejectValue("userName", "repeat", "该用户名已存在");
			return bindingResult.getFieldError().getDefaultMessage();
		}
		return "success";
	}

}