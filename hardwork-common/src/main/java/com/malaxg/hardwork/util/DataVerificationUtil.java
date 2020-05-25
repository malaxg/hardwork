package com.malaxg.hardwork.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;


/**
 * @author wangrong
 */
public class DataVerificationUtil
{
    
    /**
     * 判断对象String/StringBuffer/StringBuilder是否为null、""、"  "、集合是否为空
     *
     * @param o [参数类型]
     * @return boolean [返回类型说明]
     */
    public static boolean isEmpty(Object o)
    {
        if (Objects.isNull(o))
        {
            return true;
        }
    
        if (o instanceof String)
        {
            return StringUtils.isBlank((String)o);
        }
        else if (o instanceof StringBuffer)
        {
            return StringUtils.isBlank(((StringBuffer)o).toString());
        }
        else if (o instanceof StringBuilder)
        {
            return StringUtils.isBlank(((StringBuilder)o).toString());
        } else if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        } else if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }

        return false;
    }

    /**
     * 消除String StringBuffer StringBuilder中的首尾空格
     *
     * @param o [参数类型]
     * @return boolean [返回类型说明]
     * @author wangrong
     */
    public static Object trimStr(Object o) {
        if(Objects.isNull(o)){
            return null;
        }
        
        if (o instanceof String) {
            return ((String) o).trim();
        } else if (o instanceof StringBuffer) {
            return new StringBuffer(((StringBuffer) o).toString().trim());
        } else if (o instanceof StringBuilder) {
            return new StringBuilder(((StringBuilder) o).toString().trim());
        }

        return o;
    }

    /**
     * 消除类c中所有String StringBuffer StringBuilder中的首尾空格
     *
     * @param t [参数类型]
     * @return boolean [返回类型说明]
     * @author wangrong
     */
    public static <T> T trimObject(T t)
            throws IllegalAccessException {
        if(Objects.isNull(t)){
            return null;
        }
        
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object oldValue = ReflectionUtil.getFieldValue(field, t);
            Object newValue = trimStr(oldValue);

            if (oldValue != newValue) {
                field.setAccessible(true);
                field.set(t, newValue);
            }
        }
        return t;
    }
}