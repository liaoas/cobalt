package com.cobalt.service.impl;

import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.common.domain.BookData;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.cobalt.service.SearchService;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;

import java.util.List;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class SearchServiceImpl implements SearchService {

    // 重试次数
    public static int index = 2;

    // 爬虫资源
    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();
    // 阅读进度持久化
    static ReadingProgressPersistent readingProgressDao = ReadingProgressPersistent.getInstance();

    /**
     * 判断数据源
     *
     * @param searchBookName 书名
     * @return 结果
     */
    @Override
    public List<BookData> getBookNameData(String searchBookName) {
        if (readingProgressDao.searchType.equals(ModuleConstants.DEFAULT_DATA_SOURCE_NAME)) return null;
        ResolverFactory<BookData> search = new ResolverFactory<>(spiderActionDao.spiderActionStr,
                readingProgressDao.searchType, ReptileType.SEARCH, searchBookName);
        return search.capture();
    }
}
