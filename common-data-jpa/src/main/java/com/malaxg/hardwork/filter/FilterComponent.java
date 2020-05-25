package com.malaxg.hardwork.filter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.malaxg.hardwork.util.ObjectUtil;
import com.malaxg.hardwork.util.ReflectionUtil;
import com.malaxg.hardwork.web.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

/**
 * FilterComponent
 *
 * @param <T> T
 * @author wangrong
 * @version [版本号, 2019年3月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class FilterComponent<T>
{
    /**
     * 常量
     */
    public static final String COMMA = ",";
    /**
     * 常量
     */
    public static final String PERCENT = "%";
    /**
     * 常量
     */
    public static final String BR = "<br/>";

    /**
     * filter
     * 
     * @param filter
     *            filterObj
     * @param filterCondition
     *            filterCondition.
     *            <em> FilterType.IN and NOTIN can't be used to filed not String </em>
     * @param filterTypeMap
     *            filterTypeMap
     * @param executor
     *            executor
     * @return Page<T> Page<T>
     */
    public Page<T> filter(T filter, FilterCondition filterCondition, Map<String, FilterType> filterTypeMap,
            JpaSpecificationExecutor<T> executor)
    {
    
        Map<String, Object> filterValue = ObjectUtil.getNotNullFieldToValue(filter);
        List<FilterCriteria> filterCriterias = filterValue.entrySet().stream()
            .map(o ->
            {
                FilterType filterType = filterTypeMap.get(o.getKey());
                Object value = o.getValue();
                if (filterType != null
                    && (filterType.equals(FilterType.IN) || filterType.equals(FilterType.NOTIN)))
                {
                    value = value.toString().split(COMMA);
                }
                    return new FilterCriteria(o.getKey(), filterTypeMap.get(o.getKey()), value);
                })
                .collect(Collectors.toList());

        return filter(filterCriterias, filterCondition, Collections.emptyMap(), executor, null);
    }
    
    /**
     * more powerfull and complex 根据用户选择的查询条件，动态的组装sql语句，生成where order by 等条件子句
     *
     * @param filterCriterias
     *            filterCriterias
     * @param filterCondition
     *            filterCondition
     * @param filterSpecialDataTypeMapForLike
     *            用于like匹配的特殊数据类型
     * @param executor
     *            executor
     * @param patternFieldsExcluded
     *            搜索pattern时被排除的字段，即配的字段将不进行模糊搜索
     * @return Page
     */
    public Page<T> filter(Collection<FilterCriteria> filterCriterias, FilterCondition filterCondition,
            Map<String, FilterSpecialDataType> filterSpecialDataTypeMapForLike,
            JpaSpecificationExecutor<T> executor, List<String> patternFieldsExcluded)
    {
        FilterCondition filterConditionTemp = new FilterCondition();
        if (filterCondition != null)
        {
            ReflectionUtil.copyNotNullFileds(Arrays.asList(filterConditionTemp), filterCondition);
        }
        //1、利用Specification接口构造一个where子句
        //root:代表查询的实体类
        //cb: 用來设置查询条件
        Specification<T> spec = (Root<T> root, CriteriaQuery< ? > query, CriteriaBuilder cb) ->
        {
            Predicate predicate = toPredicate(filterCriterias, filterConditionTemp.getPattern(), patternFieldsExcluded,
                filterSpecialDataTypeMapForLike, root, cb);
            return predicate;
        };
    
        Pageable pageable = null;
        if (!StringUtils.isEmpty(filterConditionTemp.getSortField()))
        {
            Sort sort = Sort.by(filterConditionTemp.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC,
                filterConditionTemp.getSortField());
            /*Sort sort = new Sort(filterConditionTemp.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC,
                filterConditionTemp.getSortField());*/
            pageable = PageRequest.of(filterConditionTemp.getPage() - 1, filterConditionTemp.getPageSize(), sort);
            //pageable = new PageRequest(filterConditionTemp.getPage() - 1, filterConditionTemp.getPageSize(), sort);
        }
        else
        {
            pageable = PageRequest.of(filterConditionTemp.getPage() - 1, filterConditionTemp.getPageSize());
        }
        return executor.findAll(spec, pageable);
    }
    
    private Predicate toPredicate(Collection<FilterCriteria> filterCriterias, String pattern,
            List<String> patternFieldsExcluded,
            Map<String, FilterSpecialDataType> filterSpecialDataTypeMapForLike, Root<T> root,
            CriteriaBuilder cb)
    {
        List<Predicate> predicates = new ArrayList<>();
        if (filterCriterias != null)
        {
            for (FilterCriteria filterCriteria : filterCriterias)
            {
                addFilterCriteria(filterSpecialDataTypeMapForLike, root, cb, predicates, filterCriteria);
            }
        }
        if (!StringUtils.isEmpty(pattern))
        {
            addPattern(pattern, root, filterSpecialDataTypeMapForLike, predicates, cb, patternFieldsExcluded);
        }
        Predicate[] p = new Predicate[predicates.size()];
        predicates.toArray(p);
        return cb.and(p);
    }

    /**
     * 校验fieldName并转化In的数据类型
     * 
     * @param filterUserInput
     *            filterUserInput
     * @param clazz
     *            被校验的对象类型
     * @param defaultSortField
     *            默认排序字段
     * @return 是否正确
     */
    public boolean verifyFieldNameAndConvertType(FilterUserInput filterUserInput,
            Class<T> clazz, String defaultSortField)
    {
        // 校验非空
        if (filterUserInput == null)
        {
            return false;
        }
        // 设置排序
        setDefaultSortFiled(filterUserInput, defaultSortField);
        // 获取非Transient特定注释字段
        Set<String> allFields = ObjectUtil.getFieldNotTransient(clazz);
        if (filterUserInput.getFilterCondition() != null)
        {
            String sortField = filterUserInput.getFilterCondition().getSortField();
            if (sortField != null && !allFields.contains(sortField))
            {
                return false;
            }
            if (filterUserInput.getFilterCondition().getPage() == null
                    || filterUserInput.getFilterCondition().getPage() < 1)
            {
                return false;
            }
            if (filterUserInput.getFilterCondition().getPageSize() == null
                    || filterUserInput.getFilterCondition().getPageSize() < 1)
            {
                return false;
            }
        }
        if (filterUserInput.getFilterCriteria() != null)
        {
            for (FilterCriteria filterCriteria : filterUserInput.getFilterCriteria())
            {
                if (filterCriteria.getFieldName() == null || !allFields.contains(filterCriteria.getFieldName()))
                {
                    return false;
                }

                // in的转换类型
                if (!convertTypeOfIn(filterCriteria, clazz))
                {
                    return false;
                }

                if (FilterType.LIKEANYBR.equals(filterCriteria.getFilterType()))
                {
                    filterCriteria.setFilterType(FilterType.LIKEANY);
                    filterCriteria.setValue(String.join(COMMA, Arrays
                            .stream(((String) filterCriteria.getValue()).split(COMMA)).map(o -> BR + o + BR)
                            .collect(Collectors.toList())));
                }
            }
        }

        return true;
    }

    private void setDefaultSortFiled(FilterUserInput filterUserInput, String defaultSortField)
    {
        // 设置按默认字段排序
        if (filterUserInput.getFilterCondition() == null)
        {
            filterUserInput.setFilterCondition(new FilterCondition());
        }
        // 初始排序为空，按传入字段排序
        if (StringUtils.isEmpty(filterUserInput.getFilterCondition().getSortField()))
        {
            filterUserInput.getFilterCondition().setSortField(defaultSortField);
        }
    }

    private boolean convertTypeOfIn(FilterCriteria filterCriteria, Class<T> clazz)
    {
        Class clazzz = BeanUtils.getPropertyDescriptor(clazz, filterCriteria.getFieldName()).getPropertyType();
        // 非包含和非全包含
        if (!FilterType.IN.equals(filterCriteria.getFilterType())
                && !FilterType.NOTIN.equals(filterCriteria.getFilterType()))
        {
            // 时间类型是很特殊的类型，在反射的时候
            if (Date.class.equals(clazzz))
            {
                Date date = ObjectUtil.parseToDate((String)filterCriteria.getValue());
                if (date == null)
                {
                    return false;
                }
                filterCriteria.setValue(date);
            }
            return true;
        }

        //  包含或者全非包含
        List<Object> data = new ArrayList<>();
        // 判断为string类型
        if (String.class.equals(clazzz))
        {
            for (String str : ((String) filterCriteria.getValue()).split(COMMA))
            {
                data.add(str);
            }
            filterCriteria.setValue(data);
            return true;
        }
        // 时间类型
        if (Date.class.equals(clazzz))
        {
            for (String str : ((String) filterCriteria.getValue()).split(COMMA))
            {
                Date date = ObjectUtil.parseToDate(str);
                if (date == null)
                {
                    return false;
                }
                data.add(str);
            }
            filterCriteria.setValue(data);
            return true;
        }
        // 或者其他
        Method method = null;
        try
        {
            method = clazzz.getMethod("valueOf", String.class);
        }
        catch (NoSuchMethodException | SecurityException e)
        {
            e.printStackTrace();
            return false;
        }
        for (String str : ((String) filterCriteria.getValue()).split(COMMA))
        {
            try
            {
                data.add(method.invoke(null, str));
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        filterCriteria.setValue(data);
        return true;
    }

    private void addFilterCriteria(Map<String, FilterSpecialDataType> filterSpecialDataTypeMapForLike, Root<T> root,
            CriteriaBuilder cb, List<Predicate> predicates, FilterCriteria filterCriteria)
    {
        String fieldName = filterCriteria.getFieldName();
        FilterType filterType = filterCriteria.getFilterType();
        if (filterType == null)
        {
            filterType = FilterType.LIKE;
        }
        FilterSpecialDataType filterSpecialDataType = filterSpecialDataTypeMapForLike.get(fieldName);
        switch (filterType)
        {
            case LIKE:
                dealLike(root, cb, predicates, filterCriteria, filterSpecialDataType, fieldName);
                break;
            case NOTLIKE:
                dealNotLike(root, cb, predicates, filterCriteria, filterSpecialDataType, fieldName);
                break;
            case LIKEANY:
                likeAny(root, cb, predicates, filterCriteria, filterSpecialDataType);
                break;
            case IS:
                predicates.add(cb.equal(root.get(fieldName), filterCriteria.getValue()));
                break;
            case ISNOT:
                predicates.add(cb.notEqual(root.get(fieldName), filterCriteria.getValue()));
                break;
            case IN:
                predicates.add(cb.in(root.get(fieldName)).value(filterCriteria.getValue()));
                break;
            case NOTIN:
                predicates.add(cb.in(root.get(fieldName)).value(filterCriteria.getValue()).not());
                break;
            case GT:
                predicates.add(cb.greaterThan(root.get(fieldName),
                        (Comparable) filterCriteria.getValue()));
                break;
            case GE:
                predicates
                        .add(cb.greaterThanOrEqualTo(root.get(fieldName),
                                (Comparable) filterCriteria.getValue()));
                break;
            case LT:
                predicates.add(cb.lessThan(root.get(fieldName),
                        (Comparable) filterCriteria.getValue()));
                break;
            case LE:
                predicates.add(cb.lessThanOrEqualTo(root.get(fieldName),
                        (Comparable) filterCriteria.getValue()));
                break;
            default:
                throw new FilterTypeUnImplementException("FilterType " + filterType + " is not implemented.");
        }
    }

    private void dealNotLike(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates,
            FilterCriteria filterCriteria,
            FilterSpecialDataType filterSpecialDataType, String fieldName)
    {
        if (FilterSpecialDataType.UNIXTIME.equals(filterSpecialDataType))
        {
            predicates.add(cb.notLike(cb.function("FROM_UNIXTIME", String.class, root.get(fieldName),
                    cb.literal("%Y-%m-%d %H:%i:%s")),
                    PERCENT + filterCriteria.getValue().toString() + PERCENT));
        }
        else if (FilterSpecialDataType.DATE.equals(filterSpecialDataType))
        {
            predicates.add(cb.notLike(cb.function("DATE_FORMAT", String.class, root.get(fieldName),
                    cb.literal("%Y-%m-%d %H:%i:%s")),
                    PERCENT + filterCriteria.getValue().toString() + PERCENT));
        }
        else
        {
            predicates.add(cb.notLike(root.get(fieldName).as(String.class),
                    PERCENT + filterCriteria.getValue().toString() + PERCENT));
        }
    }

    private void dealLike(Root<T> root, CriteriaBuilder cb, List<Predicate> predicates, FilterCriteria filterCriteria,
            FilterSpecialDataType filterSpecialDataType, String fieldName)
    {
        if (FilterSpecialDataType.UNIXTIME.equals(filterSpecialDataType))
        {
            predicates.add(cb.like(cb.function("FROM_UNIXTIME", String.class, root.get(fieldName),
                    cb.literal("%Y-%m-%d %H:%i:%s")),
                    PERCENT + filterCriteria.getValue().toString() + PERCENT));
        }
        else if (FilterSpecialDataType.DATE.equals(filterSpecialDataType))
        {
            predicates.add(cb.like(cb.function("DATE_FORMAT", String.class, root.get(fieldName),
                    cb.literal("%Y-%m-%d %H:%i:%s")),
                    PERCENT + filterCriteria.getValue().toString() + PERCENT));
        }
        else
        {
            predicates.add(cb.like(root.get(fieldName).as(String.class),
                    PERCENT + filterCriteria.getValue().toString() + PERCENT));
        }
    }

    private void likeAny(Root<T> root,
            CriteriaBuilder cb, List<Predicate> predicates, FilterCriteria filterCriteria,
            FilterSpecialDataType filterSpecialDataType)
    {
        String[] values = filterCriteria.getValue().toString().split(COMMA);
        List<Predicate> predicatesTemp = new ArrayList<>();
        String fieldName = filterCriteria.getFieldName();
        for (String valuee : values)
        {
            if (FilterSpecialDataType.UNIXTIME.equals(filterSpecialDataType))
            {
                predicatesTemp.add(cb.like(cb.function("FROM_UNIXTIME", String.class, root.get(fieldName),
                        cb.literal("%Y-%m-%d %H:%i:%s")),
                        PERCENT + valuee + PERCENT));
            }
            else if (FilterSpecialDataType.DATE.equals(filterSpecialDataType))
            {
                predicatesTemp.add(cb.like(cb.function("DATE_FORMAT", String.class, root.get(fieldName),
                        cb.literal("%Y-%m-%d %H:%i:%s")),
                        PERCENT + valuee + PERCENT));
            }
            else
            {
                predicatesTemp.add(cb.like(root.get(fieldName).as(String.class),
                        PERCENT + valuee + PERCENT));
            }
        }
        Predicate[] predicates2a = new Predicate[predicatesTemp.size()];
        predicatesTemp.toArray(predicates2a);
        predicates.add(cb.or(predicates2a));
    }

    private void addPattern(String pattern, Root<T> root,
            Map<String, FilterSpecialDataType> filterSpecialDataTypeMapForLike,
            List<Predicate> predicates, CriteriaBuilder cb, List<String> patternFieldsExcluded)
    {
        pattern = pattern.replace("%", "\\%").replace("_", "\\_");
        pattern = PERCENT + pattern + PERCENT;
        Set<String> allFieldNameTemp = ObjectUtil.getFieldNotTransient(root.getJavaType());
        Set<String> allFieldName = new HashSet<>();
        if (patternFieldsExcluded != null)
        {
            Set<String> temp = allFieldNameTemp.stream().filter(o -> !patternFieldsExcluded.contains(o))
                    .collect(Collectors.toSet());
            allFieldName.addAll(temp);
        }
        else
        {
            allFieldName.addAll(allFieldNameTemp);
        }
        List<Predicate> predicatesTemp = new ArrayList<>();
        for (String fieldName : allFieldName)
        {
            FilterSpecialDataType filterSpecialDataType = filterSpecialDataTypeMapForLike.get(fieldName);
            if (FilterSpecialDataType.UNIXTIME.equals(filterSpecialDataType))
            {
                predicatesTemp.add(cb.like(cb.function("FROM_UNIXTIME", String.class, root.get(fieldName),
                        cb.literal("%Y-%m-%d %H:%i:%s")),
                        pattern));
            }
            else if (FilterSpecialDataType.DATE.equals(filterSpecialDataType))
            {
                predicatesTemp.add(cb.like(cb.function("DATE_FORMAT", String.class, root.get(fieldName),
                        cb.literal("%Y-%m-%d %H:%i:%s")),
                        pattern));
            }
            else
            {
                predicatesTemp.add(cb.like(root.get(fieldName).as(String.class), pattern));
            }
        }
        if (!predicatesTemp.isEmpty())
        {
            Predicate[] predicateTemp = new Predicate[predicatesTemp.size()];
            predicatesTemp.toArray(predicateTemp);
            predicates.add(cb.or(predicateTemp));
        }
    }

    /**
     * trim &ltbr/&gt
     * 
     * @param txt
     *            to be trimed
     * @return trimed txt
     */
    public String trimBr(String txt)
    {
        if (StringUtils.isEmpty(txt))
        {
            return txt;
        }
        if (txt.startsWith(BR))
        {
            txt = txt.substring(Constants.FIVE);
        }
        if (txt.endsWith(BR))
        {
            txt = txt.substring(0, txt.length() - Constants.FIVE);
        }
        return txt;
    }

    /**
     * 数据封装，将datas中所有的数据中的fieldMap.value().getKey()的field中的 数据格式<br/>
     * a<br/>
     * c<br/>
     * 封装为fieldMap.key（fieldName）中的的jsonArray数据，如[{"filedName":"a"},{"filedName"
     * :"c"}]。
     * fieldMap.value().getValue()做新的field。
     * 
     * 
     * @param datas
     *            datas
     * @param fieldMap
     *            fieldMap {toJavaFiled:{fromJavaFiled:toJsonField}}
     * @return not encapsulated data.
     * @throws IllegalAccessException
     *             IllegalAccessException
     * @throws IllegalArgumentException
     *             IllegalArgumentException
     * @throws InvocationTargetException
     *             InvocationTargetException
     */
    public List<T> encapsulateData(List<T> datas, Map<String, JSONObject> fieldMap)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        List<T> notEncapsulatedData = new ArrayList<>();
        if (datas == null || datas.isEmpty() || fieldMap == null || fieldMap.isEmpty())
        {
            return notEncapsulatedData;
        }
        T data = datas.get(0);
        for (Map.Entry<String, JSONObject> entry : fieldMap.entrySet())
        {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(data.getClass(), entry.getKey());
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod == null)
            {
                continue;
            }
            Map<String, Method> readMethods = new HashMap<>();
            for (Map.Entry<String, Object> entryTemp : entry.getValue().entrySet())
            {
                PropertyDescriptor propertyDescriptorTemp = BeanUtils.getPropertyDescriptor(data.getClass(),
                        entryTemp.getKey());
                Method readMethod = propertyDescriptorTemp.getReadMethod();
                if (readMethod != null)
                {
                    readMethods.put(entryTemp.getKey(), readMethod);
                }
            }
            if (readMethods.isEmpty())
            {
                continue;
            }
            for (T t : datas)
            {
                if (!encapsulateData(t, readMethods, writeMethod, entry))
                {
                    notEncapsulatedData.add(t);
                }
            }
        }
        return notEncapsulatedData;
    }

    // 封装单条数据
    private boolean encapsulateData(T t, Map<String, Method> readMethods, Method writeMethod,
            Map.Entry<String, JSONObject> entry)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Map<String, String[]> map = new HashMap<>();
        for (Map.Entry<String, Method> entryTemp : readMethods.entrySet())
        {
            Object value = entryTemp.getValue().invoke(t);
            if (value == null)
            {
                continue;
            }
            map.put(entryTemp.getKey(), ObjectUtil.split(value.toString(), BR));
        }
        JSONArray datan = new JSONArray();
        int size = 0;
        for (String[] strings : map.values())
        {
            if (size == 0)
            {
                size = strings.length;
            }
            else
            {
                if (size != strings.length)
                {
                    return false;
                }
            }
        }
        for (int i = 0; i < size; i++)
        {
            boolean allEmp = true;
            for (String[] s : map.values())
            {
                if (!StringUtils.isEmpty(s[i]))
                {
                    allEmp = false;
                    break;
                }
            }
            if (allEmp)
            {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, String[]> entryTempp : map.entrySet())
            {
                jsonObject.put(entry.getValue().getString(entryTempp.getKey()), entryTempp.getValue()[i]);
            }
            datan.add(jsonObject);
        }
        writeMethod.invoke(t, datan);
        return true;
    }
}
