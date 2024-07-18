package com.cobalt.common.parse;

import com.cobalt.entity.Chapter;
import com.cobalt.entity.ImportBookData;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

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

    public static Map<String, String> parseEpubByEpubLib(String file, List<Chapter> chapterList, ImportBookData instance) {

        // 存储小说
        Map<String, String> result = new LinkedHashMap<>();
        try (FileInputStream in = new FileInputStream(file)) {
            EpubReader reader = new EpubReader();
            Book book = reader.readEpub(in);
            instance.setEpubBookBook(book);

            TableOfContents tableOfContents = book.getTableOfContents();
            List<TOCReference> tocReferences = tableOfContents.getTocReferences();
            for (int i = 0, size = tocReferences.size(); i < size; i++) {
                TOCReference reference = tableOfContents.getTocReferences().get(i);
                result.put(reference.getTitle(), String.valueOf(i));
                chapterList.add(new Chapter(reference.getTitle(), reference.getTitle()));
            }
        } catch (Exception ignored) {
        }

        instance.setChapterList(chapterList);
        instance.setBookMap(result);

        return result;
    }
}
