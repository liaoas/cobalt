package com.cobalt.parser.chapter;

import com.cobalt.parser.book.BookMetadata;

import java.util.List;

/**
 * 网络章节解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class FileChapterParser extends AbstractChapterParser {

    @Override
    public boolean parser(Object bookName) {
        chaptersClear();
        List<Chapter> capture = BookMetadata.getInstance().getChapterList();
        chaptersAdd(capture);
        return !capture.isEmpty();
    }
}
