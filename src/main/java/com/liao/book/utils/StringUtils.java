package com.liao.book.utils;

/**
 * <p>
 * String 字符串工具类
 * </p>
 *
 * @author LiAo
 * @since 2022-12-28
 */
public class StringUtils {

    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str);
    }

    /**
     * 判断字符串是否为非空
     *
     * @param str String
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
