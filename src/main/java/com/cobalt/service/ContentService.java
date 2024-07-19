package com.cobalt.service;

import org.jsoup.nodes.Element;

/**
 * <p>
 * 爬取当前章节信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public interface ContentService {


    /**
     * 获取章节内容
     *
     * @param url 链接
     */
    void searchBookChapterData(String url);

    /**
     * 获取手动导入的章节内容
     *
     * @param url 链接/map key
     */
    void getImportBook(String url);
}
