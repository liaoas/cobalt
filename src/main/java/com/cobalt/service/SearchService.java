package com.cobalt.service;

import com.cobalt.common.domain.BookData;

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

}
