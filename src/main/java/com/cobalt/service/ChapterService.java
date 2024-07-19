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
     * 远程爬取书籍处理
     *
     * @param link 链接
     */
    void rabbitFootChapterData(String link);

    /**
     * 本地导入书籍处理
     */
    void importChapterData();



}
