package com.cobalt.parser.book;

import com.cobalt.parser.chapter.Chapter;
import com.cobalt.common.constant.Constants;
import com.intellij.openapi.vfs.VirtualFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author LiAo
 * @since 2024/7/19
 */
public class TextFileBookParser extends FileBookParser {

    private final static Logger log = LoggerFactory.getLogger(TextFileBookParser.class);

    @Override
    public boolean parser(Object object) {
        VirtualFile file = (VirtualFile) object;

        parse(file, bookMap, chapterList);

        instance.bookType = Constants.TXT_STR_LOWERCASE;

        return !bookMap.isEmpty() && !chapterList.isEmpty();
    }

    public void parse(VirtualFile file, Map<String, String> bookMap, List<Chapter> chapterList) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            StringBuilder contentBuilder = new StringBuilder();
            String title = null;

            while (true) {
                try {
                    if ((line = reader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Matcher matcher = CHAPTER_PATTERN.matcher(line);
                if (matcher.find()) {
                    if (title != null) {
                        bookMap.put(title, contentBuilder.toString());
                        chapterList.add(new Chapter(title, title));
                        contentBuilder.setLength(0);
                    }
                    title = line;
                } else {
                    contentBuilder.append(line);
                    contentBuilder.append(System.lineSeparator());
                }
            }
            if (title != null) {
                bookMap.put(title, contentBuilder.toString());
                chapterList.add(new Chapter(title, title));
            }
        } catch (IOException e) {
            log.error("书籍解析失败，filePath：{}", file.getPath(), e);
        }

        BookMetadata.getInstance().setChapterList(chapterList);
        BookMetadata.getInstance().setBookMap(bookMap);
    }
}
