package com.cobalt.book;

import com.cobalt.common.constant.Constants;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;

import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link BookParser}实现的抽象基类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public abstract class AbstractBookParser implements BookParser {

    // 爬虫资源
    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();
    // 阅读进度持久化
    static ReadingProgressPersistent readingProgressDao = ReadingProgressPersistent.getInstance();
    // 阅读状态
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();
    // Default Pattern
    static final Pattern CHAPTER_PATTERN = Pattern.compile(Constants.DEFAULT_CHAPTER_REGULAR);


    /**
     * 判断是否为 text 类型书籍
     *
     * @param extension 后缀
     * @return 是否为 text
     */
    public boolean isText(String extension) {
        return extension.equals(Constants.TXT_STR_LOWERCASE) || extension.equals(Constants.TXT_STR_UPPERCASE);
    }

    /**
     * 判断是否为 epub 类型书籍
     *
     * @param extension 后缀
     * @return 是否为 epub
     */
    public boolean isEpub(String extension) {
        return extension.equals(Constants.EPUB_STR_LOWERCASE) || extension.equals(Constants.EPUB_STR_UPPERCASE);
    }

    /**
     * 存储books到共享单例对象
     *
     * @param books 搜索的书籍列表
     * @return 是否成功
     */
    public boolean setBooks(List<Book> books) {
        if (books.isEmpty()) {
            BookMetadata.getInstance().setBooks(null);
            return false;
        }
        BookMetadata.getInstance().setBooks(books);
        return true;
    }
}
