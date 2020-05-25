package com.malaxg.hardwork;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-17 21:46
 */
public class TestImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
										BeanNameGenerator importBeanNameGenerator) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Person.class);
		rootBeanDefinition.setAttribute("name", "wangrong");
		rootBeanDefinition.setAttribute("age", 18);
		registry.registerBeanDefinition("person1", rootBeanDefinition);
		registerBeanDefinitions(importingClassMetadata, registry);
	}
}
