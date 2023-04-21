package com.liao.book.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
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
    public static Map<String, String> parseEpub(String file, List<String> chapterList) {

        // 存储小说
        Map<String, String> result = new LinkedHashMap<>();

        // 存储文件名称对应的编码格式
        Map<String, Charset> charsetMap = getCharsetMap(file);

        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {

                if (entry.isDirectory() || !entry.getName().endsWith(".html")) {
                    continue;
                }

                if (entry.getName().contains("chapter")) {

                    Document doc = readEntry(zipStream, charsetMap.get(entry.getName()));

                    String title = getTitle(doc);
                    String chapterContent = getContent(doc);

                    result.put(title, chapterContent);
                } else if (entry.getName().contains("toc")) {
                    Document doc = readEntry(zipStream, charsetMap.get(entry.getName()));

                    getChapterToc(doc, chapterList);
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
    private static Document readEntry(ZipInputStream zipStream, Charset c) {
        StringBuilder sb = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream, c));

        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sb.append(line);
        }

        return Jsoup.parse(sb.toString());
    }

    /**
     * 获取 EPUB 中每一个文档的编码格式
     *
     * @param filePath 文件路径
     * @return Map<文件名称, 编码格式>
     */
    private static Map<String, Charset> getCharsetMap(String filePath) {
        Map<String, Charset> charsetMap = new HashMap<>(16);

        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(filePath))) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".html")) {

                    Charset charset = getCharset(zipStream);

                    charsetMap.put(entry.getName(), charset);
                }
                zipStream.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return charsetMap;
    }

    /**
     * 获取文档编码格式
     *
     * @param zipStream zip
     * @return 编码格式
     */
    private static Charset getCharset(ZipInputStream zipStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream));
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            sb.append(line);

            if (line.contains("<?xml")) break;
        }

        Document parse = Jsoup.parse(sb.toString());

        return parse.charset();
    }

    /**
     * 解析目录
     *
     * @param doc         文档
     * @param chapterList 存储目录
     */
    private static void getChapterToc(Document doc, List<String> chapterList) {
        Elements a = doc.getElementsByTag("a");
        for (Element tocANode : a) {
            chapterList.add(tocANode.text());
        }
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
