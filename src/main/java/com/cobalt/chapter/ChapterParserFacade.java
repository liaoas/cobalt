package com.cobalt.chapter;

import com.cobalt.book.FileBookParser;
import com.cobalt.book.NetworkBookParser;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.book.BookMetadata;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.intellij.openapi.vfs.VirtualFile;
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
public class ChapterParserFacade {

    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    public boolean initChapter(Object chapter) {
        if (chapter == null) return false;

        if (instance.searchType.equals(ModuleConstants.IMPORT)) {
            return new FileChapterParser().parser(chapter);
        }
        return new NetworkChapterParser().parser(chapter);
    }
}
