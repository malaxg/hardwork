package com.malaxg.hardwork;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-17 21:14
 */
public class WinCondition implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		MergedAnnotations annotations = metadata.getAnnotations();
		annotations.forEach(a -> System.out.println(annotations));
		//获取当前是什么操作系统
		String osName = System.getProperty("os.name").toLowerCase();
		return osName.contains("win");
	}
}
