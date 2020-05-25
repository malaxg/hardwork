package com.malaxg.hardwork.filter;

/**
 * FilterType
 * @version  [版本号, 2019年2月20日]
 * @see      [相关类/方法]
 * @since    [产品/模块版本]
 */
public enum FilterType
{
    /**
     * IN
     */
    IN,
    /**
     * IN
     */
    NOTIN,
    /**
     * LIKE
     */
    LIKE,
    /**
     * LIKEANY
     */
    LIKEANY,
    /**
     * LIKEANY special append <br/>
     */
    LIKEANYBR,
    /**
     * NOT LIKE
     */
    NOTLIKE,
    /**
     * IS,=
     */
    IS,
    /**
     * IS NOT,!=
     */
    ISNOT,
    /**
     * GREATOR THAN OR EQUAL
     */
    GE,
    /**
     * LESS THAN OR EQUAL
     */
    LE,
    /**
     * LESS THAN
     */
    LT,
    /**
     * GREATER THAN
     */
    GT
}
