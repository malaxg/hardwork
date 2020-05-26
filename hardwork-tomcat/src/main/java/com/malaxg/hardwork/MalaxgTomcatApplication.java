package com.malaxg.hardwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wangrong
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MalaxgTomcatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MalaxgTomcatApplication.class, args);
	}

}
