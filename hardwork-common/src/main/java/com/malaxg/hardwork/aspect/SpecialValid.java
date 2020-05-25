package com.malaxg.hardwork.aspect;

import java.util.Arrays;

import com.malaxg.hardwork.util.CheckRepeatUtil;
import com.malaxg.hardwork.util.DateVerificationResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * @author wangrong
 */
@Aspect
@Component
public class SpecialValid
{
	
	@Pointcut("execution(* com.malaxg.*.controller.*.*(.., @javax.validation.Valid (*), ..))")
	public void logPointCut()
	{
	}
	
	@Before("logPointCut()")
	public void before(JoinPoint joinPoint)
		throws IllegalAccessException
	{
		//检查是否存在相同属性值
		checkRepeat(joinPoint);
	}
	
	private void checkRepeat(JoinPoint joinPoint)
		throws IllegalAccessException
	{
		BindingResult bindingResult;
		
		Object[] args = joinPoint.getArgs();
		boolean exist = false;
		DateVerificationResult result = null;
		bindingResult = (BindingResult)Arrays.stream(args).filter(o -> o instanceof BindingResult).findFirst().get();
		
		for (Object arg : args)
		{
			result = CheckRepeatUtil.existRepeatFiledValue(arg);
			if (result.isFail())
			{
				exist = true;
				break;
			}
		}
		
		if (exist)
		{
			bindingResult.reject("repeat", result.getMessage());
		}
	}
}
