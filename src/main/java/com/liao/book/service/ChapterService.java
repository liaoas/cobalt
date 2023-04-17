package com.liao.book.service;

/**
 * <p>
 * 爬取章节
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public interface ChapterService {

    /**
     * 爬取章节信息
     *
     * @param link 链接
     */
    void getBookChapterByType(String link);

    /**
     * 香书小说
     *
     * @param link 链接
     */
    void searchBookChapterData(String link);


    /**
     * 笔趣阁
     *
     * @param link 链接
     */
    void searchBookChapterData_bqg2(String link);

}
