package com.cobalt.parse;

import com.cobalt.entity.Chapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    // Epub 内容文件后缀
    private static final String EPUB_FILE_SUFFIX = ".html";

    // 获取文件分隔符
    // private static final String fileSeparator = FileSystems.getDefault().getSeparator();
    private static final String fileSeparator = "/";

    /**
     * 解析 epub 文件为 Map 格式，K 为章节名称，Value 为章节内容
     *
     * @param file epub 文件
     * @return <章节，章节内容>
     */
    public static Map<String, String> parseEpub(String file, List<Chapter> chapterList) {

        // 存储小说
        Map<String, String> result = new LinkedHashMap<>();

        // 存储文件名称对应的编码格式
        Map<String, Charset> charsetMap = getCharsetMap(file);

        // 是否解析了章节
        boolean isParser = false;
	
        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {

                if (entry.isDirectory() || !entry.getName().endsWith(EPUB_FILE_SUFFIX)) {
                    continue;
                }

                if (!isParser) {
                    // 第一个html文件为目录文件
                    Document doc = readEntry(zipStream, charsetMap.get(entry.getName()));
                    getChapterToc(doc, chapterList);
                    isParser = true;
                    zipStream.closeEntry();
                    continue;
                }

                Document doc = readEntry(zipStream, charsetMap.get(entry.getName()));

                String chapterContent = getContent(doc);

                String name = entry.getName();

                // 截取文件名称 / 前面部分
                int i = name.lastIndexOf(fileSeparator);
                if (i != -1) name = name.substring(i + 1);

                result.put(name, chapterContent);

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
                if (!entry.isDirectory() && entry.getName().endsWith(EPUB_FILE_SUFFIX)) {

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
    private static void getChapterToc(Document doc, List<Chapter> chapterList) {
        Elements a = doc.getElementsByTag("a");
        for (Element tocANode : a) {
            String href = tocANode.attr("href");

            int i = href.lastIndexOf(fileSeparator);

            // 截取文件名称 / 前面部分
            if (i != -1) href = href.substring(i + 1);

            // 判断 href 链接.html 之后是否有后缀，并截取
            int suffixIndex = href.lastIndexOf(EPUB_FILE_SUFFIX);

            if (suffixIndex == -1) continue;

            int index = suffixIndex + EPUB_FILE_SUFFIX.length();

            // 没有后缀
            if (href.length() == index) {
                chapterList.add(new Chapter(href, tocANode.text()));
                continue;
            }

            // 有后缀
            String fileName = href.substring(0, index);
            chapterList.add(new Chapter(fileName, tocANode.text()));
        }
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
