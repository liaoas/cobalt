package com.cobalt.common.parse;

import com.cobalt.common.constant.Constants;
import com.cobalt.chapter.Chapter;
import com.cobalt.book.BookMetadata;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Epub 文件解析
 * </p>
 *
 * @author LiAo
 * @since 2023-04-13
 */
public class EpubContentParser {

    private final static Logger log = LoggerFactory.getLogger(EpubContentParser.class);

    // 阅读状态
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    public static Map<String, String> parseEpubByEpubLib(String file, List<Chapter> chapterList) {

        // 存储小说
        Map<String, String> result = new LinkedHashMap<>();
        try (FileInputStream in = new FileInputStream(file)) {
            EpubReader reader = new EpubReader();
            Book book = reader.readEpub(in);
            BookMetadata.getInstance().setEpubBookBook(book);

            TableOfContents tableOfContents = book.getTableOfContents();
            List<TOCReference> tocReferences = tableOfContents.getTocReferences();
            for (int i = 0, size = tocReferences.size(); i < size; i++) {
                TOCReference reference = tableOfContents.getTocReferences().get(i);
                result.put(reference.getTitle(), String.valueOf(i));
                chapterList.add(new Chapter(reference.getTitle(), reference.getTitle()));
            }
        } catch (Exception e) {
            log.error("书籍解析失败，filePath：{}", file, e);
        }
        BookMetadata.getInstance().setChapterList(chapterList);
        BookMetadata.getInstance().setBookMap(result);
        instance.bookType = Constants.EPUB_STR_LOWERCASE;

        return result;
    }
}
