package com.cobalt.common.parse;

import com.cobalt.common.model.Chapter;
import com.cobalt.common.model.ImportBookData;
import com.github.weisj.jsvg.C;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
