package com.malaxg.codegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MalaxgGeneratorApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MalaxgGeneratorApplication.class, args);
    }
}
