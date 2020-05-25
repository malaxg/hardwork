import com.malaxg.hardwork.MainConfig1;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * @Description: 学习Spring源码用的测试主类
 * @author: malaxg
 * @date: 2020-03-17 19:51
 */
public class SpringTest {

	/**
	 * 使用AnnotationConfigApplicationContext读取注解配置
	 * 注解配置实际上是取代了xml配置，使得配置更加的简单
	 */
	@Test
	public void testAnnotationConfigApplicationContext() {
		//Spring容器
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig1.class);
		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		Arrays.asList(beanDefinitionNames).forEach(name -> System.out.println(name));
	}

}
