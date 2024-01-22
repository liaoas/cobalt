package com.cobalt.service;

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
    void searchBookChapterDataBQG(String link);


    /**
     * 加载手动导入的章节信息
     */
    void importChapterData();


    void rabbitFootChapterData(String link);

}
