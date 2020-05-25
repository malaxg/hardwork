package com.malaxg.hardwork;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description:
 * @author: malaxg
 * @date: 2020-03-17 21:38
 */
public class TestImportSelector implements ImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		String[] strings = {"com.malaxg.hardwork.Person", "com.malaxg.hardwork.Teacher"};
		return strings;
	}
}
