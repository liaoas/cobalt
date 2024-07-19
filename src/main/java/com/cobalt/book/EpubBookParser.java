package com.cobalt.book;

import com.cobalt.chapter.Chapter;
import com.cobalt.common.constant.Constants;
import com.intellij.openapi.vfs.VirtualFile;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * 本地Epub 类型书籍解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class EpubBookParser extends FileBookParser {

    private final static Logger log = LoggerFactory.getLogger(EpubBookParser.class);

    @Override
    public boolean parser(Object object) {

        VirtualFile file = (VirtualFile) object;

        parse(file, bookMap, chapterList);

        instance.bookType = Constants.EPUB_STR_LOWERCASE;

        return !bookMap.isEmpty() && !chapterList.isEmpty();
    }

    public void parse(VirtualFile file, Map<String, String> bookMap, List<Chapter> chapterList) {

        try (InputStream inputStream = file.getInputStream()) {
            Book book = new EpubReader().readEpub(inputStream);

            BookMetadata.getInstance().setEpubBookBook(book);

            TableOfContents tableOfContents = book.getTableOfContents();
            List<TOCReference> tocReferences = tableOfContents.getTocReferences();
            for (int i = 0, size = tocReferences.size(); i < size; i++) {
                TOCReference reference = tableOfContents.getTocReferences().get(i);
                bookMap.put(reference.getTitle(), String.valueOf(i));
                chapterList.add(new Chapter(reference.getTitle(), reference.getTitle()));
            }
        } catch (Exception e) {
            log.error("书籍解析失败，filePath：{}", file, e);
        }

        BookMetadata.getInstance().setChapterList(chapterList);
        BookMetadata.getInstance().setBookMap(bookMap);
    }
}
