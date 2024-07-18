package com.cobalt.service;

import com.cobalt.entity.BookData;

import java.util.List;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public interface SearchService {

    /**
     * 判断数据源
     *
     * @param searchBookName 书名
     * @return 结果
     */
    List<BookData> getBookNameData(String searchBookName);


    /**
     * 香书小说
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData(String searchBookName);

    /**
     * 笔趣阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_bqg2(String searchBookName);


}
