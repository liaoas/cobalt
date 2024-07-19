package com.cobalt.chapter;

import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;

import java.util.List;

/**
 * {@link ChapterParser}实现的抽象基类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public abstract class AbstractChapterParser implements ChapterParser {

    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();

    public void chaptersClear() {
        instance.chapters.clear();
    }

    public void chaptersAdd(List<Chapter> capture) {
        instance.chapters.addAll(capture);
    }

}
