package com.cobalt.common.parse;

import com.cobalt.common.constant.Constants;
import com.cobalt.chapter.Chapter;
import com.cobalt.book.BookMetadata;
import com.cobalt.common.utils.LocalCharsetUtil;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * txt 文件解析
 * </p>
 *
 * @author LiAo
 * @since 2023-04-13
 */
public class TxtContentParser {

    private final static Logger log = LoggerFactory.getLogger(EpubContentParser.class);

    // Default Pattern
    private static final Pattern CHAPTER_PATTERN = Pattern.compile(Constants.DEFAULT_CHAPTER_REGULAR);

    // 阅读状态
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    /**
     * 解析本地 txt 文件为 Map 格式，K 为章节名称，Value 为章节内容
     *
     * @param filePath txt 文件路径
     * @return <章节，章节内容>
     */
    public static Map<String, String> parseTxt(String filePath, List<Chapter> chapterList) {

        Map<String, String> chapterMap = new LinkedHashMap<>();

        String fileCharset = LocalCharsetUtil.getFileCharset(filePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), fileCharset))) {
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
                        chapterMap.put(title, contentBuilder.toString());
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
                chapterMap.put(title, contentBuilder.toString());
                chapterList.add(new Chapter(title, title));
            }
        } catch (IOException e) {
            log.error("书籍解析失败，filePath：{}", filePath, e);
        }

        BookMetadata.getInstance().setChapterList(chapterList);
        BookMetadata.getInstance().setBookMap(chapterMap);
        instance.bookType = Constants.TXT_STR_LOWERCASE;

        return chapterMap;
    }
}
