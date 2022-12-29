package com.liao.book.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Bean 工厂
 * </p>
 *
 * @author LiAo
 * @since 2021/11/5
 */
public class BeanFactory {

    // 存储Bean 实例
    private static final Map<String, Object> map = new HashMap<String, Object>();

    // 存储实例路径
    private static final Map<String, String> beanMap = new HashMap<>();

    /**
     * 获取Bean实例
     *
     * @param key key
     * @return 实例
     */
    public static Object getBean(String key) {
        return map.get(key);
    }

    static {

        beanMap.put("BookChapterServiceImpl", "com.liao.book.service.impl.BookChapterServiceImpl");
        beanMap.put("BookSearchServiceImpl", "com.liao.book.service.impl.BookSearchServiceImpl");
        beanMap.put("BookTextServiceImpl", "com.liao.book.service.impl.BookTextServiceImpl");

        try {
            for (String baneName : beanMap.keySet()) {
                String beanValue = beanMap.get(baneName);
                Object value = Class.forName(beanValue).getDeclaredConstructor().newInstance();
                map.put(baneName, value);
            }
        } catch (InstantiationException |
                IllegalAccessException |
                ClassNotFoundException |
                NoSuchMethodException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
