package com.liao.book.factory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jaxen.dom4j.Dom4jXPath;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
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
                Object value = Class.forName(beanValue).newInstance();
                map.put(baneName, value);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        /*SAXReader reader = new SAXReader();
        try {
            // 使用阅读器读取xml文档
            *//*Document document = reader.read
                    (Dom4jXPath.class.getClassLoader().getResourceAsStream("bean.xml"));*//*

            Document document = reader.read(new File("bean.xml"));
            // 获取root节点
            Element element = document.getRootElement();
            // 获取root节点的子节点
            Iterator<Element> iterator = element.elementIterator();
            // 遍历迭代器
            while (iterator.hasNext()) {
                Element e = iterator.next();
                String key = e.attributeValue("id");
                Object value = Class.forName(e.attributeValue("class")).newInstance();
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
