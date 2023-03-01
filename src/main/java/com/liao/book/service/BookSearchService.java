package com.liao.book.service;

import com.liao.book.entity.BookData;

import java.util.List;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public interface BookSearchService {

    /**
     * 判断数据源
     *
     * @param searchBookName 书名
     * @return 结果
     */
    List<BookData> getBookNameData(String searchBookName);


    /**
     * 笔趣阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData(String searchBookName);

    /**
     * 妙笔阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_miao(String searchBookName);

    /**
     * 全本小说网
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_tai(String searchBookName);

    /**
     * 笔趣阁www.biduoxs.com
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_bqg2(String searchBookName);

    /**
     * 69书吧www.69shuba.cc
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_69shu(String searchBookName);

    /**
     * 58小说www.wbxsw.com
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_58(String searchBookName);

    /**
     * 顶点小说www.maxreader.net
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_top(String searchBookName);

    /**
     * 千千小说网 www.qqxsw.co
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    List<BookData> searchBookNameData_qian(String searchBookName);

}
