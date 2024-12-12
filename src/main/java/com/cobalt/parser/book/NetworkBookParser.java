package com.cobalt.parser.book;

import com.cobalt.common.constant.UIConstants;
import com.rabbit.foot.enums.ReptileType;
import com.rabbit.foot.factory.ResolverFactory;

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
        if (readingProgress.getSearchType().equals(UIConstants.DEFAULT_DATA_SOURCE_NAME)) return false;

        String searchBookName = String.valueOf(bookName);
        ResolverFactory<Book> search = new ResolverFactory<>(spiderAction.getSpiderActionStr(),
                readingProgress.getSearchType(), ReptileType.SEARCH, searchBookName);
        List<Book> books = search.capture();
        return setBooks(books);
    }
}
