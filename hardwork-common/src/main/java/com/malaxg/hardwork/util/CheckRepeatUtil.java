package com.malaxg.hardwork.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import com.malaxg.hardwork.annotation.NonRepeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查集合中是否有存在相同属性值的对象
 *
 * @author wangrong
 * @version [版本号, March 25, 2020]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CheckRepeatUtil
{
    /** 日志记录 */
    private static final Logger logger = LoggerFactory.getLogger(CheckRepeatUtil.class);
    
    /**
     * 检查对象中的集合对象是否有存在相同属性值的对象(需被@NoRepeat标注)，支持去重
     * 注意：该方法只支持2级校验，也就是如果是对象的话，会去看对象中的属性是否存在集合，如果是集合就会对它判重
     *
     * @param c Object
     * @return true:重复 false:不重复
     * @throws IllegalAccessException IllegalAccessException
     */
    public static DateVerificationResult existRepeatFiledValue(Object c)
        throws IllegalAccessException
    {
        if (c == null)
        {
            return DateVerificationResult.ok();
        }
    
        if (c instanceof List)
        {
            return existRepeatFiledValue((List)c);
        }
    
        Field[] fields = c.getClass().getDeclaredFields();
        for (Field f : fields)
        {
            f.setAccessible(true);
            Object o = f.get(c);
            if (o == null || ReflectionUtil.isBaseType(o) || o instanceof String)
            {
                continue;
            }
            
            if (o instanceof List)
            {
                return existRepeatFiledValue((List)o);
            }
        }
        return DateVerificationResult.ok();
    }
    
    /**
     * 检查集合中是否有存在相同属性值的对象;  需要注意基本数据类型属性默认值，最好的办法是将基本数据类型改为保证类型
     * Examples: User("malaxg",15); User("malaxg",16),User("malaxg",16)
     *
     * @param <T> t
     * @param list list
     * @param filed 仅限基本数据类型值
     * @return true:重复 false:不重复
     */
    public static <T> boolean existRepeatFiledValue(List<T> list, String filed)
        throws IllegalAccessException
    {
        return existRepeatFiledValue(list, filed, true);
    }
    
    /**
     * existRepeatFiledValue
     *
     * @param <T> t
     * @param list list
     * @param filed 仅限基本数据类型值
     * @param excludeNullValueCompare excludeNullValueCompare
     * @return true:重复 false:不重复
     */
    public static <T> boolean existRepeatFiledValue(List<T> list, String filed, boolean excludeNullValueCompare)
        throws IllegalAccessException
    {
        Objects.requireNonNull(list);
        Objects.requireNonNull(filed);
        
        int nullValueNum = 0;
        Set<Object> set = new HashSet<>();
        for (T t : list)
        {
            Object value;
            value = getObjectFiledValue(t, filed);
            
            if (excludeNullValueCompare)
            {
                if (Objects.nonNull(value))
                {
                    set.add(value);
                }
                else
                {
                    nullValueNum++;
                }
            }
            else
            {
                set.add(value);
            }
        }
        return list.size() - nullValueNum != set.size();
    }
    
    /**
     * 属性上加了@NonRepeat注解可以使用该方法验证重复
     *
     * @param <T> t
     * @param list list
     * @return boolean
     */
    public static <T> DateVerificationResult existRepeatFiledValue(List<T> list)
        throws IllegalAccessException
    {
        Objects.requireNonNull(list);
        
        List<StringBuilder> mapList = new ArrayList<>();
        for (T t : list)
        {
            mapList.add(getObjectNonRepeatFiledValue(t));
        }
        Set<String> set = new HashSet<>();
    
        mapList.forEach(o -> set.add(o.toString()));
    
        if (set.size() != mapList.size())
        {
            List<String> collect = Arrays.stream(list.get(0).getClass().getDeclaredFields())
                .filter(o -> o.getAnnotation(NonRepeat.class) != null)
                .map(o -> o.getAnnotation(NonRepeat.class).message()).collect(Collectors.toList());
            if (!DataVerificationUtil.isEmpty(collect))
            {
                return DateVerificationResult.fail(collect.get(0));
            }
        }
        
        return DateVerificationResult.ok();
    }
    
    /**
     * 获取对象的某个属性的值
     *
     * @param <T> t
     * @param t t
     * @param filed filed
     * @return Object
     */
    public static <T> Object getObjectFiledValue(T t, String filed)
        throws IllegalAccessException
    {
        Objects.requireNonNull(t);
        Objects.requireNonNull(filed);
        
        Field field;
        try
        {
            field = t.getClass().getDeclaredField(filed);
        }
        catch (NoSuchFieldException e)
        {
            logger.error("没有该属性:" + filed);
            return null;
        }
        return ReflectionUtil.getFieldValue(field, t);
    }
    
    /**
     * 通过使用@NonRepeat注解的方式获取对象被标注@NonRepeat注解属性的值
     *
     * @param <T> t
     * @param t t
     * @return StringBuilder
     */
    public static <T> StringBuilder getObjectNonRepeatFiledValue(T t)
        throws IllegalAccessException
    {
        Objects.requireNonNull(t);
        Field[] fields = t.getClass().getDeclaredFields();
        
        StringBuilder sb = new StringBuilder();
        for (Field field : fields)
        {
            Annotation a = field.getAnnotation(NonRepeat.class);
            if (Objects.nonNull(a))
            {
                Object o = ReflectionUtil.getFieldValue(field, t);
                
                if (Objects.nonNull(o))
                {
                    sb.append(o).append(o.hashCode());
                }
            }
        }
        return sb;
    }
}
