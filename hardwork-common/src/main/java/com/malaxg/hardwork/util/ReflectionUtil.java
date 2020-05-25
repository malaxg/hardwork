package com.malaxg.hardwork.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

/**
 * 反射相关工具类
 *
 * @author wangrong
 * @version [版本号, March 25, 2020]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ReflectionUtil
{
	
	/**
	 * 获取对象某个属性的值
	 *
	 * @param f f
	 * @param t t
	 * @return Object
	 * @throws IllegalAccessException IllegalAccessException
	 * @author wangrong
	 */
	public static <T> Object getFieldValue(Field f, T t) throws IllegalAccessException {
		if (Objects.isNull(f) || Objects.isNull(t)) {
			return null;
		}
		
		f.setAccessible(true);
		f.get(t);
		return f.get(t);
	}
	
	/**
	 * 判断object是否为基本类型
	 *
	 * @param o Object
	 * @return boolean
	 */
	public static boolean isBaseType(Object o)
	{
		return o instanceof Integer ||
			o instanceof Byte ||
			o instanceof Long ||
			o instanceof Double ||
			o instanceof Float ||
			o instanceof Character ||
			o instanceof Short ||
			o instanceof Boolean;
	}
	
	/**
	 * 复制非空的属性值到target中，如果source中为空则不复制
	 *
	 * @param <D> d
	 * @param <S> s
	 * @param des des
	 * @param source source
	 * @return
	 */
	public static <D, S> boolean copyNotNullFileds(Collection<D> des, S source)
	{
		if (DataVerificationUtil.isEmpty(des) || source == null)
		{
			return false;
		}
		
		// set keyValue
		Map<String, Object> keyValue = new HashMap<>();
		
		AtomicBoolean isOk = new AtomicBoolean(true);
		
		Arrays.stream(BeanUtils.getPropertyDescriptors(source.getClass()))
			.forEach(o ->
			{
				Object value = null;
				try
				{
					value = o.getReadMethod().invoke(source);
				}
				catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e)
				{
					if (isOk.get())
					{
						isOk.set(false);
					}
				}
				if (value != null && !"class".equals(o.getName()))
				{
					keyValue.put(o.getName(), value);
				}
			});
		if (!isOk.get())
		{
			return false;
		}
		// 筛选对象d在map中存在的属性   field -> writeMethod
		D d = des.stream().findFirst().get();
		
		Map<String, Method> keyMethod = Arrays.stream(BeanUtils.getPropertyDescriptors(d.getClass()))
			.filter(o -> keyValue.keySet().contains(o.getName()))
			.collect(Collectors.toMap(PropertyDescriptor::getName, PropertyDescriptor::getWriteMethod));
		try
		{
			for (D s : des)
			{
				for (Map.Entry<String, Method> entry : keyMethod.entrySet())
				{
					entry.getValue().invoke(s, keyValue.get(entry.getKey()));
				}
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			return false;
		}
		return true;
	}
}
