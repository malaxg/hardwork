package com.malaxg.hardwork;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author wangrong
 */
@EnableFeignClients
@SpringCloudApplication
public class MalaxgTomcatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MalaxgTomcatApplication.class, args);
	}

	@Bean
	Logger.Level feginLog() {
		return Logger.Level.BASIC;
	}
}
