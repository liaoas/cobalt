package com.cobalt.parser.chapter;

import com.rabbit.foot.enums.ReptileType;
import com.rabbit.foot.factory.ResolverFactory;

import java.util.List;

/**
 * 网络章节解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class NetworkChapterParser extends AbstractChapterParser {

    @Override
    public boolean parser(Object bookName) {
        chaptersClear();
        String link = String.valueOf(bookName);
        ResolverFactory<Chapter> search;
        search = new ResolverFactory<>(
                spiderAction.getSpiderActionStr(),
                readingProgress.getSearchType(),
                ReptileType.CHAPTER, link);
        List<Chapter> capture = search.capture();
        chaptersAdd(capture);
        return !capture.isEmpty();
    }
}
