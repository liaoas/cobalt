package com.liao.book.parse;

import com.intellij.openapi.vfs.VirtualFile;
import com.liao.book.common.Constants;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedHashMap;
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

    // Default Pattern
    private static final Pattern CHAPTER_PATTERN = Pattern.compile(Constants.DEFAULT_CHAPTER_REGULAR);

    /**
     * 解析本地 txt 文件为 Map 格式，K 为章节名称，Value 为章节内容
     *
     * @param file txt 文件
     * @return <章节，章节内容>
     */
    public static Map<String, String> parseTxt(InputStream file) {
        Map<String, String> chapterMap = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file, "GBK"))) {
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return chapterMap;
    }
}
