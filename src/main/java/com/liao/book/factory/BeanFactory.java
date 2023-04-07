package com.liao.book.factory;

import com.sun.istack.NotNull;

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

    // 存储Bean 实例的容器
    private static final Map<String, Object> map = new HashMap<>();

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

    /**
     * 往对象容器中塞入Bean实例
     *
     * @param name 名称
     * @param bean 实例对象
     */
    public static void setBean(String name, Object bean) {
        map.put(name, bean);
    }

    /**
     * 判断容器中是否已经存在相关key
     *
     * @param name beanName
     * @return 对象
     */
    public static boolean containsBeanName(String name) {
        return map.containsKey(name);
    }

    static {

        beanMap.put("BookChapterServiceImpl", "com.liao.book.service.impl.BookChapterServiceImpl");
        beanMap.put("BookSearchServiceImpl", "com.liao.book.service.impl.BookSearchServiceImpl");
        beanMap.put("BookTextServiceImpl", "com.liao.book.service.impl.BookTextServiceImpl");
        beanMap.put("ReadingProgressDao", "com.liao.book.dao.ReadingProgressDao");

        try {
            for (String baneName : beanMap.keySet()) {
                String beanValue = beanMap.get(baneName);
                Object value = Class.forName(beanValue).getDeclaredConstructor().newInstance();
                map.put(baneName, value);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
