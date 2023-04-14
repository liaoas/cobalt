package com.liao.book.service;

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


    void searchBookChapterData(String url);


    /**
     * 笔趣阁
     *
     * @param element 当前章节信息
     * @return 结果
     */
    String textFormat(Element element);

    /**
     * 妙笔阁
     *
     * @param element 章节内容
     * @return 数据
     */
    String textFormat_miao(Element element);
}
