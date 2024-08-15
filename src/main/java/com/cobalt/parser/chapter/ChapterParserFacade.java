package com.cobalt.parser.chapter;

import com.cobalt.common.constant.UIConstants;
import com.cobalt.framework.persistence.ReadingProgressPersistent;

/**
 * <p>
 * 爬取数据章节列表
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class ChapterParserFacade {

    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    public boolean initChapter(Object chapter) {
        if (instance.searchType.equals(UIConstants.IMPORT)) {
            return new FileChapterParser().parser(chapter);
        }

        if (chapter == null)
            return false;

        return new NetworkChapterParser().parser(chapter);
    }
}
