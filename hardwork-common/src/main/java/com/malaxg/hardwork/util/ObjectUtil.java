package com.malaxg.hardwork.util;

import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.malaxg.hardwork.web.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * PrivateUtils.java
 *
 * @author wWX530788
 * @version [版本号, 2018年11月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ObjectUtil
{
    private static final DateFormat DATE_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final DateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtil.class);
    
    private static final String COMMA = ",";
    
    private static LocalDateTime start;
    
    /**
     * 默认分隔为1000
     *
     * @param ts [参数说明]
     * @param size [参数说明]
     * @param <Z> [参数说明]
     * @return 输出的list [参数说明]
     */
    public static <Z> List<List<Z>> splitor(List<Z> ts, Integer size)
    {
        List<List<Z>> tss = new ArrayList<>();
        if (ts == null || ts.isEmpty())
        {
            return tss;
        }
        int length = Constants.ONE_THOUSAND;
        if (size != null && size > 0)
        {
            length = size;
        }
        int p = (ts.size() - 1) / length + 1;
        for (int i = 0; i < p - 1; i++)
        {
            List<Z> tmp = ts.subList(i * length, (i + 1) * length);
            tss.add(tmp);
        }
        tss.add(ts.subList(length * (p - 1), ts.size()));
        return tss;
    }
    
    /**
     * 反射存储数据
     *
     * @param context [参数说明]
     * @param data [参数说明]
     * @param repositoryName [参数说明]
     * @param entityQunifiedName [参数说明]
     * @return boolean [参数说明]
     */
    public static boolean saveData(ApplicationContext context, List data, String repositoryName,
        String entityQunifiedName)
    {
        if (context == null || data == null || repositoryName == null)
        {
            return false;
        }
        if (repositoryName.length() == 0 || entityQunifiedName == null || entityQunifiedName.length() == 0)
        {
            return false;
        }
        
        Object rep = null;
        try
        {
            rep = context.getBean(repositoryName);
        }
        catch (BeansException be)
        {
            return false;
        }
        
        Class entityClass = null;
        try
        {
            entityClass = Class.forName(entityQunifiedName);
        }
        catch (ClassNotFoundException e1)
        {
            return false;
        }
        
        try
        {
            
            /*
             * Class.forName(
             * "com.huawei.code.dao.isource.IsourceCommitSync2012Respository0")
             * .getMethod("save",Iterable.class).invoke(obj, args)
             */
            Class z = rep.getClass();
            Method m = z.getMethod("save", Iterable.class);
            
            // JSON.parseObject(JSON.toJSONString(data.iterator().next()),
            // rep.getClass().getTypeParameters()[0].getClass()) ;
            
            List<Object> aa = new ArrayList<>();
            for (Object obj : data)
            {
                aa.add(JSON.parseObject(JSON.toJSONString(obj), entityClass));
            }
            m.invoke(rep, aa);
            m = z.getMethod("flush");
            m.invoke(rep);
            return true;
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InvocationTargetException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (NoSuchMethodException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (SecurityException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * 反射存储数据
     *
     * @param context [参数说明]
     * @param data [参数说明]
     * @param repositoryName [参数说明]
     * @param entityQunifiedName [参数说明]
     * @return boolean [参数说明]
     */
    @SuppressWarnings("unused")
    public static boolean saveDataPer1000(ApplicationContext context, List data, String repositoryName,
        String entityQunifiedName)
    {
        List<List> temps = splitor(data, Constants.ONE_THOUSAND);
        boolean ifok = true;
        for (List temp : temps)
        {
            if (!saveData(context, data, repositoryName, entityQunifiedName))
            {
                ifok = false;
                break;
            }
        }
        return ifok;
    }
    
    /**
     * 反射查找数据
     *
     * @param context [参数说明]
     * @param repositoryName [参数说明]
     * @param findMethodName [参数说明]
     * @param oneThing [参数说明]
     * @param returnType [参数说明]
     * @param <S> [参数说明]
     * @param <T> [参数说明]
     * @return List<S, T> [参数说明]
     */
    @Deprecated
    public static <S, T> List<S> findDataByOnething(ApplicationContext context, String repositoryName,
        String findMethodName, T oneThing, Class<S> returnType)
    {
        if (context == null || repositoryName == null || repositoryName.length() == 0)
        {
            return Collections.emptyList();
        }
        if (findMethodName == null || findMethodName.length() == 0 || oneThing == null || returnType == null)
        {
            return Collections.emptyList();
        }
        
        Object rep = null;
        try
        {
            rep = context.getBean(repositoryName);
        }
        catch (BeansException be)
        {
            return new ArrayList<>();
        }
        
        try
        {
            /*
             * Class.forName(
             * "com.huawei.code.dao.isource.IsourceCommitSync2012Respository0")
             * .getMethod("save",Iterable.class).invoke(obj, args)
             */
            Class z = rep.getClass();
            Method m = z.getDeclaredMethod(findMethodName, oneThing.getClass());
            // z.getDeclaredMethod("findAll").invoke(rep);
            List result = (List<Object[]>)m.invoke(rep, oneThing);// TODO
            List<S> resultN = JSON.parseArray(JSON.toJSONString(result), returnType);
            return resultN;
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InvocationTargetException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (NoSuchMethodException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (SecurityException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * 反射查找数据
     *
     * @param context [参数说明]
     * @param repositoryName [参数说明]
     * @param findMethodName [参数说明]
     * @param returnType [参数说明]
     * @param something [参数说明]
     * @param <S> [参数说明]
     * @return List<S> [参数说明]
     * @throws Exception [参数说明]
     */
    public static <S> List<S> findDataBySomething(ApplicationContext context, String repositoryName,
        String findMethodName, Class<S> returnType, Object... something)
        throws Exception
    {
        if (context == null || repositoryName == null || repositoryName.length() == 0)
        {
            return Collections.emptyList();
        }
        if (findMethodName == null || findMethodName.length() == 0 || something == null || returnType == null)
        {
            return Collections.emptyList();
        }
        
        Object rep = null;
        try
        {
            rep = context.getBean(repositoryName);
        }
        catch (BeansException be)
        {
            return new ArrayList<>();
        }
        
        try
        {
            /*
             * Class.forName(
             * "com.huawei.code.dao.isource.IsourceCommitSync2012Respository0")
             * .getMethod("save",Iterable.class).invoke(obj, args)
             */
            Class z = rep.getClass();
            
            Class[] mtypes = new Class[something.length];
            for (int i = 0; i < mtypes.length; i++)
            {
                if (something[i] == null)
                {
                    throw new Exception("type error");
                }
                mtypes[i] = something[i].getClass();
            }
            
            Method m = z.getDeclaredMethod(findMethodName, mtypes);
            // z.getDeclaredMethod("findAll").invoke(rep);
            List result = (List<Object[]>)m.invoke(rep, something);// TODO
            List<S> resultN = JSON.parseArray(JSON.toJSONString(result), returnType);
            return resultN;
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
    
    /**
     * convertNameLetter
     *
     * @param name [参数说明]
     * @param firstLetterToUpper [参数说明]
     * @param elseLetterToLower [参数说明]
     * @return String [参数说明]
     */
    public static String convertNameLetter(String name, boolean firstLetterToUpper, boolean elseLetterToLower)
    {
        if (name == null || name.length() == 0)
        {
            return name;
        }
        else if (name.length() == 1)
        {
            if (firstLetterToUpper)
            {
                return name.toUpperCase();
            }
            else
            {
                return name;
            }
        }
        else
        {
            return (firstLetterToUpper ? name.substring(0, 1).toUpperCase() : name.substring(0, 1))
                + (elseLetterToLower ? name.substring(1).toLowerCase() : name.substring(1));
        }
    }
    
    /**
     * getIpAddress
     *
     * @return String
     */
    public static String getIpAddress()
    {
        InetAddress ip;
        try
        {
            ip = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            return null;
        }
        return ip.getHostAddress();
    }
    
    /**
     * formatToTime
     *
     * @param time time
     * @return String
     */
    public static String formatToTime(Date time)
    {
        if (time == null)
        {
            return null;
        }
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat1.format(time);
    }
    
    /**
     * parseToDate
     *
     * @param time time
     * @return Date Date
     */
    public static Date parseToDate(String time)
    {
        if (StringUtils.isEmpty(time))
        {
            return null;
        }
        time = time.replace("/", "-");
        if (time.length() <= Constants.TEN)
        {
            try
            {
                synchronized (DATE_FORMAT1)
                {
                    return DATE_FORMAT1.parse(time);
                }
            }
            catch (ParseException e)
            {
                return null;
            }
        }
        else
        {
            try
            {
                synchronized (DATE_FORMAT2)
                {
                    return DATE_FORMAT2.parse(time);
                }
            }
            catch (ParseException e)
            {
                return null;
            }
        }
    }
    
    /**
     * parseToDateFromNumber
     *
     * @param number [参数说明]
     * @param isSecond [参数说明]
     * @return Date [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static Date parseToDateFromNumber(String number, boolean isSecond)
    {
        if (StringUtils.isBlank(number))
        {
            return null;
        }
        long timeLong = 0;
        try
        {
            timeLong = Long.parseLong(number);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        if (isSecond)
        {
            timeLong *= Constants.ONE_THOUSAND;
        }
        return new Date(timeLong);
    }
    
    /**
     * formatToDate
     *
     * @param time [参数说明]
     * @return String [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static String formatToDate(Date time)
    {
        if (time == null)
        {
            return null;
        }
        synchronized (DATE_FORMAT1)
        {
            return DATE_FORMAT1.format(time);
        }
    }
    
    /**
     * parseInteger
     *
     * @param text [参数说明]
     * @return Integer [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static Integer parseInteger(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return null;
        }
        try
        {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
    
    /**
     * getString
     *
     * @param obj [参数说明]
     * @return String [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getString(Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        if (obj instanceof Date)
        {
            return ObjectUtil.formatToTime((Date)obj);
        }
        return String.valueOf(obj);
    }
    
    /**
     * start
     *
     * @see [类、类#方法、类#成员]
     */
    public static void start()
    {
        start = LocalDateTime.now();
    }
    
    /**
     * tag
     *
     * @param message [参数说明]
     * @see [类、类#方法、类#成员]
     */
    public static void tag(String message)
    {
        LOGGER.info(String.format("% 10d   %s", Duration.between(start, LocalDateTime.now()).toMillis(), message));
    }
    
    /**
     * 获取不为null的fields
     *
     * @param object 对象
     * @return fieldName
     */
    public static Map<String, Object> getNotNullFieldToValue(Object object)
    {
        return getFieldToValue(object, (o) -> o != null);
    }
    
    /**
     * 获取不为null的fields
     *
     * @param object 对象
     * @return fieldName
     */
    public static Map<String, Object> getAllFieldToValue(Object object)
    {
        return getFieldToValue(object, (o) -> true);
    }
    
    /**
     * 获取不为null的fields
     *
     * @param object 对象
     * @param predicate 测试fieldValue
     * @return fieldName
     */
    private static Map<String, Object> getFieldToValue(Object object, Predicate<Object> predicate)
    {
        Map<String, Object> result = new HashMap<>();
        
        Arrays.stream(BeanUtils.getPropertyDescriptors(object.getClass()))
            .forEach(o ->
            {
                try
                {
                    Object obj = o.getReadMethod().invoke(object);
                    if (!"class".equals(o.getName()) && predicate.test(obj))
                    {
                        result.put(o.getName(), obj);
                    }
                }
                catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e)
                {
                    LOGGER.error(e.getMessage(), e);
                }
            });
        return result;
    }
    
    /**
     * 获取所有的fields
     *
     * @param clazz 类
     * @return fieldName
     */
    public static Map<String, Method> getAllFieldToWriteMethod(Class<?> clazz)
    {
        return clazz == null ? null
            : Arrays.stream(BeanUtils.getPropertyDescriptors(clazz)).filter(o -> !"class".equals(o.getName()))
            .collect(Collectors.toMap(PropertyDescriptor::getName, PropertyDescriptor::getWriteMethod));
    }
    
    /**
     * 获取指定类型所有的fields
     *
     * @param clazz 类
     * @param targeType 指定类型
     * @return fieldName
     */
    public static Map<String, Method> getFieldOfTargetTypeToValue(Class<?> clazz, Class<?> targeType)
    {
        return clazz == null ? null
            : Arrays.stream(BeanUtils.getPropertyDescriptors(clazz))
            .filter(o -> !"class".equals(o.getName()) && o.getPropertyType().equals(targeType))
            .collect(Collectors.toMap(PropertyDescriptor::getName, PropertyDescriptor::getWriteMethod));
    }
    
    /**
     * 获取所有的fields
     *
     * @param clazz 类
     * @return fieldName
     */
    public static Map<String, Method> getAllFieldToReadMethod(Class<?> clazz)
    {
        return clazz == null ? null
            : Arrays.stream(BeanUtils.getPropertyDescriptors(clazz)).filter(o -> !"class".equals(o.getName()))
            .collect(Collectors.toMap(PropertyDescriptor::getName, PropertyDescriptor::getReadMethod));
    }
    
    /**
     * 获取所有的非javax.persistence.Transient的fields
     *
     * @param clazz 类
     * @return fieldName
     */
    public static Set<String> getFieldNotTransient(Class<?> clazz)
    {
        return getFieldNotTransiteToReadMethod(clazz, null);
    }
    
    private static Set<String> getFieldNotTransiteToReadMethod(Class<?> clazz, Set<String> result)
    {
        if (result == null)
        {
            result = new HashSet<>();
        }
        result
            .addAll(Arrays
                .stream(
                    clazz.getDeclaredFields())
                .filter(o -> o.getAnnotation(Transient.class) == null && !Modifier.isStatic(o.getModifiers())
                    && BeanUtils.getPropertyDescriptor(clazz, o.getName()) != null
                    && BeanUtils.getPropertyDescriptor(clazz, o.getName()).getReadMethod() != null
                    && BeanUtils.getPropertyDescriptor(clazz, o.getName()).getReadMethod()
                    .getAnnotation(Transient.class) == null)
                .map(Field::getName).collect(Collectors.toSet()));
        if (!clazz.getSuperclass().equals(Object.class))
        {
            getFieldNotTransiteToReadMethod(clazz.getSuperclass(), result);
        }
        return result;
    }
    
    /**
     * 复制非null的fields
     *
     * @param des 到
     * @param fieldNameToValue 从
     * @param fieldNameToWriteMethod fieldName -> WriteMethod of des
     * @return 是否成功
     */
    public static boolean copyNotNullFileds(Object des, Map<String, Object> fieldNameToValue,
        Map<String, Method> fieldNameToWriteMethod)
    {
        if (des == null || fieldNameToValue == null || fieldNameToWriteMethod == null)
        {
            return false;
        }
        // set keyValue
        try
        {
            for (Map.Entry<String, Object> entry : fieldNameToValue.entrySet())
            {
                Method method = fieldNameToWriteMethod.get(entry.getKey());
                if (method == null)
                {
                    continue;
                }
                method.invoke(des, entry.getValue());
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
    
    /**
     * getLogInfo get log info
     *
     * @param objOld old object
     * @param objNew new object
     * @param fieldToTitleMap field map to title
     * @param <T> [参数说明]
     * @param compareAllField if compared all field or compared only not null in objNew.
     * @return String[], String[0] is title,String[1] is detail.
     */
    public static <T> String[] getLogInfo(T objOld, T objNew, Map<String, String> fieldToTitleMap,
        boolean compareAllField)
    {
        Map<String, Object> fieldToValue = compareAllField ? getAllFieldToValue(objNew)
            : getNotNullFieldToValue(objNew);
        Map<String, Method> allReadMethod = ObjectUtil.getAllFieldToReadMethod(objOld.getClass());
        StringBuilder sb = new StringBuilder();
        List<String> changedFields = new ArrayList<>();
        for (Map.Entry<String, Object> entry : fieldToValue.entrySet())
        {
            Method readMethod = allReadMethod.get(entry.getKey());
            if (readMethod == null)
            {
                continue;
            }
            String title = fieldToTitleMap.get(entry.getKey());
            if (title == null)
            {
                continue;
            }
            Object oldValue = null;
            try
            {
                oldValue = readMethod.invoke(objOld);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                return null;
            }
            if (oldValue == null && entry.getValue() == null)
            {
                continue;
            }
            if (oldValue != null && entry.getValue() != null && oldValue.equals(entry.getValue()))
            {
                continue;
            }
            changedFields.add(title);
            sb.append(String.format("%s从<%s>调整为<%s>;", title,
                oldValue == null ? "null" : oldValue.toString(),
                entry.getValue() == null ? "null" : entry.getValue().toString()));
        }
        return new String[] {String.join(COMMA, changedFields), sb.toString()};
    }
    
    /**
     * split txt, 保留相邻的分隔符，保留首尾空字符
     *
     * @param txt txt
     * @param splitor splitor
     * @return String[]
     */
    public static String[] split(String txt, String splitor)
    {
        if (StringUtils.isEmpty(txt))
        {
            return new String[] {""};
        }
        if (StringUtils.isEmpty(splitor))
        {
            return new String[] {txt};
        }
        List<String> r = new ArrayList<>();
        int start1 = 0;
        int end = 0;
        while ((end = txt.indexOf(splitor, start1)) >= 0)
        {
            r.add(txt.substring(start1, end));
            start1 = end + splitor.length();
        }
        r.add(txt.substring(start1, txt.length()));
        
        String[] rr = new String[r.size()];
        r.toArray(rr);
        return rr;
    }
    
    /**
     * copyProperties 把fieldNameToValue中的Value拷贝到data中对应于key的field中。
     *
     * @param data data
     * @param fieldNameToValue fieldNameToValue
     * @param <T> 参数
     * @return boolean boolean
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T> boolean copyProperties(List<T> data, JSONObject fieldNameToValue)
    {
        if (data == null || data.isEmpty())
        {
            return true;
        }
        Map<String, Method> allFieldToWriteMethod = getAllFieldToWriteMethod(data.get(0).getClass());
        Map<String, Method> fieldToWriteMethod = new HashMap<>();
        fieldNameToValue.keySet().forEach(o ->
        {
            Method method = allFieldToWriteMethod.get(o);
            if (method != null)
            {
                fieldToWriteMethod.put(o, method);
            }
        });
        Map<String, Class<?>> typeMap = fieldToWriteMethod.entrySet().stream()
            .collect(Collectors.toMap(o -> o.getKey(),
                o -> BeanUtils
                    .getPropertyDescriptor(data.get(0).getClass(), o.getKey())
                    .getPropertyType()));
        try
        {
            for (T t : data)
            {
                for (Map.Entry<String, Method> entry : fieldToWriteMethod.entrySet())
                {
                    entry.getValue().invoke(t,
                        valueOf(typeMap.get(entry.getKey()), fieldNameToValue.get(entry.getKey())));
                }
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            return false;
        }
        return true;
    }
    
    /**
     * add days
     *
     * @param date date
     * @param days days to add
     * @return Date
     */
    public static Date addDay(Date date, int days)
    {
        if (date == null)
        {
            return null;
        }
        return Date.from(date.toInstant().plus(days, ChronoUnit.DAYS));
    }
    
    /**
     * type convert
     *
     * @param toType convert to type
     * @param obj obj to be converted
     * @return converted obj of type toType
     */
    public static Object valueOf(Class<?> toType, Object obj)
    {
        if (toType == null || obj == null)
        {
            return null;
        }
        if (toType.equals(String.class))
        {
            return obj.toString();
        }
        if (toType.equals(Date.class))
        {
            return parseToDate(obj.toString());
        }
        try
        {
            Method writeMethod = toType.getMethod("valueOf", String.class);
            return writeMethod.invoke(null, obj.toString());
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e)
        {
            return null;
        }
    }
    
    /**
     * equals of two set
     *
     * @param s1 set1
     * @param s2 set2
     * @param <T> 参数
     * @return equals
     */
    public static <T> boolean equals(Set<T> s1, Set<T> s2)
    {
        if (s1 == null && s2 == null)
        {
            return true;
        }
        if (s1 == null || s2 == null)
        {
            return false;
        }
        return s1.size() == s2.size() && new ArrayList<>(s1).containsAll(s2);
    }
    
    /**
     * convert null to zero
     *
     * @param num num
     * @return num
     */
    public static Long convertNullToZero(Long num)
    {
        if (num == null)
        {
            return 0L;
        }
        else
        {
            return num;
        }
    }
    
    /**
     * test str if a date yyyy-MM-dd or yyyy/MM/dd format.
     *
     * @param str input
     * @return boolean
     */
    public static boolean isDateFormOrNull(String str)
    {
        return StringUtils.isEmpty(str) || parseToDate(str) != null;
    }
    
    /**
     * sort
     *
     * @param origin origin
     * @param sortInfos 排序内容
     * @param <T> <T>
     * @return List<T> List<T>
     */
    public static <T> List<T> sort(List<T> origin, List<SortInfo> sortInfos)
    {
        if (origin.size() == 0)
        {
            return Collections.emptyList();
        }
        origin = origin.stream().filter(o -> o != null).collect(Collectors.toList());
        if (origin.size() == 1 || sortInfos == null)
        {
            return origin;
        }
        PropertyDescriptor[] pd = BeanUtils.getPropertyDescriptors(origin.get(0).getClass());
        Map<String, Class<?>> dpm = Arrays.stream(pd)
            .collect(Collectors.toMap(PropertyDescriptor::getName, o -> o.getPropertyType()));
        Map<String, Method> allFieldToReadMethod = getAllFieldToReadMethod(origin.get(0).getClass());
        Set<String> allFields = allFieldToReadMethod.keySet();
        sortInfos = sortInfos.stream().filter(o -> allFields.contains(o.getSortField())).collect(Collectors.toList());
        if (sortInfos.isEmpty())
        {
            return origin;
        }
        Comparator comparator = null;
        for (SortInfo sortInfo : sortInfos)
        {
            if (sortInfo.isAsc == null)
            {
                sortInfo.isAsc = true;
            }
            if (sortInfo.nullsFirst == null)
            {
                sortInfo.nullsFirst = false;
            }
            Method readMethod = allFieldToReadMethod.get(sortInfo.getSortField());
            Class<?> type = dpm.get(sortInfo.sortField);
            Comparator temp = getComparator(type, sortInfo, readMethod);
            
            if (!sortInfo.isAsc)
            {
                temp = temp.reversed();
            }
            if (comparator == null)
            {
                comparator = temp;
            }
            else
            {
                comparator.thenComparing(temp);
            }
        }
        origin.sort(comparator);
        return origin;
    }
    
    /**
     * limit
     *
     * @param origin origin
     * @param page 页码
     * @param pageSize 页码数据大小
     * @param <T> <T>
     * @return PageContainer<T> PageContainer<T>
     */
    public static <T> PageContainer<T> limit(List<T> origin, int page, int pageSize)
    {
        PageContainer<T> data = new PageContainer<>();
        if (origin == null)
        {
            data.setPage(1);
            data.setTotal(0);
            data.setData(Collections.emptyList());
        }
        else if (page < 1 || pageSize < 1 || pageSize * (page - 1) >= origin.size())
        {
            data.setPage(page);
            data.setTotal(origin.size());
            data.setData(Collections.emptyList());
        }
        else
        {
            data.setPage(page);
            data.setTotal(origin.size());
            data.setData(origin.subList(pageSize * (page - 1), Integer.min(pageSize * page, origin.size())));
        }
        return data;
    }
    
    /**
     * Copyright Notice: Copyright 1998-2009, Huawei Technologies Co., Ltd. ALL
     * Rights Reserved.
     * <p>
     * Warning: This computer software sourcecode is protected by copyright law
     * and international treaties. Unauthorized reproduction or distribution of
     * this sourcecode, or any portion of it, may result in severe civil and
     * criminal penalties, and will be prosecuted to the maximum extent possible
     * under the law.
     * <p>
     * Created by zWX438747 on 2019-04-24.
     *
     * @author zWX438747
     */
    public static class SortInfo
    {
        private String sortField;
        private Boolean isAsc;
        private Boolean nullsFirst;
        
        public String getSortField()
        {
            return sortField;
        }
        
        public void setSortField(String sortField)
        {
            this.sortField = sortField;
        }
        
        public Boolean getIsAsc()
        {
            return isAsc;
        }
        
        public void setIsAsc(Boolean isAsc)
        {
            this.isAsc = isAsc;
        }
        
        public Boolean getNullsFirst()
        {
            return nullsFirst;
        }
        
        public void setNullsFirst(Boolean nullsFirst)
        {
            this.nullsFirst = nullsFirst;
        }
    }
    
    /**
     * 封装page，传给前台
     * Copyright Notice:
     * Copyright 1998-2009, Huawei Technologies Co., Ltd. ALL Rights Reserved.
     * <p>
     * Warning: This computer software sourcecode is protected by copyright law
     * and international treaties. Unauthorized reproduction or distribution
     * of this sourcecode, or any portion of it, may result in severe civil and
     * criminal penalties, and will be prosecuted to the maximum extent
     * possible under the law.
     * <p>
     * Created by zWX438747 on 2019-05-13.
     *
     * @param <T> 存储的数据
     * @author zWX438747
     */
    public static class PageContainer<T>
    {
        private List<T> data = null;
        private long total;
        private int page;
        
        public List<T> getData()
        {
            return data;
        }
        
        public void setData(List<T> data)
        {
            this.data = data;
        }
        
        public long getTotal()
        {
            return total;
        }
        
        public void setTotal(long total)
        {
            this.total = total;
        }
        
        public int getPage()
        {
            return page;
        }
        
        public void setPage(int page)
        {
            this.page = page;
        }
        
    }
    
    private static Comparator getComparator(Class<?> type, SortInfo sortInfo, Method readMethod)
    {
        Comparator temp = null;
        if (type.equals(String.class))
        {
            temp = Comparator.comparing(o ->
            {
                Object value = null;
                try
                {
                    value = readMethod.invoke(o);
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                    LOGGER.error(e.getMessage(), e);
                }
                return (String)value;
            }, sortInfo.nullsFirst ^ !sortInfo.isAsc ? Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)
                : Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
        }
        else
        {
            temp = Comparator.comparing(o ->
            {
                Object value = null;
                try
                {
                    value = readMethod.invoke(o);
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                    LOGGER.error(e.getMessage(), e);
                }
                return value;
            }, sortInfo.nullsFirst ^ !sortInfo.isAsc
                ? Comparator.nullsFirst((o1, o2) -> ((Comparable)o1).compareTo(o2))
                : Comparator.nullsLast((o1, o2) -> ((Comparable)o1).compareTo(o2)));
        }
        return temp;
    }
    
    /**
     * 获取集合中重复的元素
     *
     * @param list [参数说明]
     * @param <E> [参数说明]
     * @return List<E> [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static <E> List<E> getDuplicateElements(List<E> list)
    {
        if (DataVerificationUtil.isEmpty(list))
        {
            return null;
        }
        return list.stream()
            .collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(entry -> entry.getKey())
            .collect(Collectors.toList());
    }
}
