package com.liao.book.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
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

    /**
     * 解析 epub 文件为 Map 格式，K 为章节名称，Value 为章节内容
     *
     * @param file epub 文件
     * @return <章节，章节内容>
     */
    public static Map<String, String> parseEpub(InputStream file) {
        Map<String, String> result = new LinkedHashMap<>();
        try (ZipInputStream zipStream = new ZipInputStream(file, StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".html") && entry.getName().contains("chapter")) {

                    String content = readEntry(zipStream);
                    Document doc = Jsoup.parse(content);
                    String title = getTitle(doc);
                    String chapterContent = getContent(doc);

                    result.put(title, chapterContent);
                }
                zipStream.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 读取 ZipInputStream 内容
     *
     * @param zipStream epub 单个文件
     * @return 内容
     */
    private static String readEntry(ZipInputStream zipStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream, StandardCharsets.UTF_8));
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sb.append(line);
        }

        return sb.toString();
    }

    /**
     * 解析章节标题
     *
     * @param doc 文档
     * @return 标题
     */
    private static String getTitle(Document doc) {
        return doc.getElementById("title").text();
    }

    /**
     * 解析文章内容
     *
     * @param doc 文档
     * @return 章节内容
     */
    private static String getContent(Document doc) {
        StringBuilder sb = new StringBuilder();
        Elements p = doc.getElementsByTag("p");

        for (Element element : p) {
            String text = element.text();
            sb.append(text);
            sb.append("\n");
        }

        return sb.toString();
    }
}
