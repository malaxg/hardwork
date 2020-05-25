package com.malaxg.hardwork.controller;

import com.malaxg.hardwork.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-17 20:49
 */
@Controller
public class TestController {
	@Autowired
	@Qualifier("testServiceImpl1")
	private TestService testService;
}
