package com.cobalt.parser.chapter;

import com.cobalt.framework.persistence.SpiderAction;
import com.cobalt.framework.persistence.proxy.ReadingProgressProxy;
import com.cobalt.framework.persistence.proxy.SpiderActionProxy;

import java.util.List;

/**
 * {@link ChapterParser}实现的抽象基类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public abstract class AbstractChapterParser implements ChapterParser {

    public final ReadingProgressProxy readingProgress;

    public final SpiderActionProxy spiderAction;

    public AbstractChapterParser() {
        readingProgress = new ReadingProgressProxy();
        spiderAction = new SpiderActionProxy();
    }

    public void chaptersClear() {
        readingProgress.chaptersClear();
    }

    public void chaptersAdd(List<Chapter> capture) {
        readingProgress.setChapters(capture);
    }

}
