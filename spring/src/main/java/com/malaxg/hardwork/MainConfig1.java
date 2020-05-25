package com.malaxg.hardwork;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description:注解类，用于取代xml配置
 * @author: malaxg
 * @date: 2020-03-17 19:55
 */
@Configuration
@ComponentScan("com.malaxg.hardwork")
@Import(TestImportBeanDefinitionRegistrar.class)
public class MainConfig1 {
}
