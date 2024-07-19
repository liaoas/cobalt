package com.cobalt.book;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.chapter.Chapter;
import com.cobalt.common.parse.EpubContentParser;
import com.cobalt.common.parse.TxtContentParser;
import com.cobalt.common.utils.StringUtils;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.intellij.openapi.vfs.VirtualFile;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class BookParser {

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
    public List<Book> getBookNameData(String searchBookName) {
        if (readingProgressDao.searchType.equals(ModuleConstants.DEFAULT_DATA_SOURCE_NAME)) return null;
        ResolverFactory<Book> search = new ResolverFactory<>(spiderActionDao.spiderActionStr,
                readingProgressDao.searchType, ReptileType.SEARCH, searchBookName);
        return search.capture();
    }

    /**
     * 本地书籍导入
     *
     * @param file 书籍文件
     * @return 是否成功
     */
    public boolean importBook(@NotNull VirtualFile file) {
        // 获取扩展名
        String extension = file.getExtension();

        if (StringUtils.isEmpty(extension)) return false;

        String filePath = file.getPath();

        if (StringUtils.isEmpty(filePath)) return false;
        // 存储书籍信息
        Map<String, String> bookMap = new HashMap<>(16);
        // 存储目录信息
        List<Chapter> chapterList = new ArrayList<>(16);
        try {
            if (isText(extension)) {
                bookMap = TxtContentParser.parseTxt(filePath, chapterList);
            } else if (isEpub(extension)) {
                bookMap = EpubContentParser.parseEpubByEpubLib(filePath, chapterList);
            }
        } catch (Exception e) {
            return false;
        }
        return !bookMap.isEmpty() && !chapterList.isEmpty();
    }

    public boolean isText(String extension) {
        return extension.equals(Constants.TXT_STR_LOWERCASE) || extension.equals(Constants.TXT_STR_UPPERCASE);
    }


    public boolean isEpub(String extension) {
        return extension.equals(Constants.EPUB_STR_LOWERCASE) || extension.equals(Constants.EPUB_STR_UPPERCASE);
    }
}
