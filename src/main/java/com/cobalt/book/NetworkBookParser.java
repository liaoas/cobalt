package com.cobalt.book;

import com.cobalt.common.constant.ModuleConstants;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;

import java.util.List;

/**
 * 网络书籍解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class NetworkBookParser extends AbstractBookParser {

    @Override
    public boolean parser(Object bookName) {
        if (readingProgressDao.searchType.equals(ModuleConstants.DEFAULT_DATA_SOURCE_NAME)) return false;

        String searchBookName = String.valueOf(bookName);
        ResolverFactory<Book> search = new ResolverFactory<>(spiderActionDao.spiderActionStr,
                readingProgressDao.searchType, ReptileType.SEARCH, searchBookName);
        List<Book> books = search.capture();
        return setBooks(books);
    }
}
