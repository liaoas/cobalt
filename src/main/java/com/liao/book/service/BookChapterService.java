package com.liao.book.service;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * <p>
 * 爬取章节
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public interface BookChapterService {

    void getBookChapterByType(String link);

    /**
     * 笔趣阁书籍爬取
     *
     * @param link 链接
     */
    void searchBookChapterData(String link);

    /**
     * 妙笔阁书籍爬取
     *
     * @param link 链接
     */
    void searchBookChapterData_miao(String link);


    /**
     * 全本小说网书籍爬取
     *
     * @param link 链接
     */
    void searchBookChapterData_tai(String link);

    /**
     * 笔趣阁书籍爬取
     *
     * @param link 链接
     */
    void searchBookChapterData_bqg2(String link);

    /**
     * 69书吧书籍爬取
     *
     * @param link 链接
     */
    void searchBookChapterData_69shu(String link);

    /**
     * 58小说书籍爬取
     *
     * @param link 链接
     */
    void searchBookChapterData_58(String link);

    /**
     * 定点小说
     *
     * @param link 链接
     */
    void searchBookChapterData_top(String link);
}
