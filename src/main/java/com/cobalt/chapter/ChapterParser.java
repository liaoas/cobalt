package com.cobalt.chapter;

import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.book.BookMetadata;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;

import java.util.List;

/**
 * <p>
 * 爬取数据章节列表
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class ChapterParser {
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();

    /**
     * 爬取章节信息
     *
     * @param link 链接
     */
    public void getBookChapterByType(String link) {
        if (instance.searchType.equals(ModuleConstants.IMPORT)) {
            importChapterData();
            return;
        }
        rabbitFootChapterData(link);
    }

    /**
     * 远程爬取书籍处理
     * @param link 链接
     */
    public void rabbitFootChapterData(String link) {
        instance.chapters.clear();
        ResolverFactory<Chapter> search = new ResolverFactory<>(spiderActionDao.spiderActionStr, instance.searchType, ReptileType.CHAPTER, link);
        List<Chapter> capture = search.capture();
        instance.chapters.addAll(capture);
    }

    /**
     * 加载手动导入的章节信息
     */
    public void importChapterData() {
        instance.chapters.clear();
        BookMetadata importBookData = BookMetadata.getInstance();
        List<Chapter> chapterList = importBookData.getChapterList();
        instance.chapters.addAll(chapterList);
    }
}
