package com.liao.book.service.impl;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import com.liao.book.service.BooksReptile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 妙笔阁书籍爬虫
 * </p>
 *
 * @author LiAo
 * @since 2022-12-30
 */
public class MiBiGeBookReptile implements BooksReptile {

    // 重试次数
    public static int index = 2;

    // 存储数据
    public static List<BookData> bookDataList = new ArrayList<>();

    /**
     * 根据名称爬取书籍列表
     *
     * @param searchBookName 书籍名称
     * @return 书籍列表
     */
    @Override
    public List<BookData> getBookList(String searchBookName) {
        bookDataList.clear();

        Connection connect = Jsoup.connect("https://www.imiaobige.com/search.html");
        // 设置请求头
        connect.data("searchkey", searchBookName);

        try {
            Document document = connect.post();
            Elements grid = document.getElementsByTag("dl");

            for (Element element : grid) {
                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("a").eq(1).text();
                bookData.setBookName(bookName);
                // 链接
                String bookLink = element.getElementsByTag("a").eq(1).attr("href");
                bookLink = bookLink.replaceAll("novel", "read");
                bookLink = bookLink.replaceAll(".html", "");
                bookData.setBookLink(bookLink);
                // 章节信息
                String chapter = element.getElementsByTag("a").eq(4).text();
                bookData.setChapter(chapter);
                // 作者
                String author = element.getElementsByTag("a").eq(3).text();
                bookData.setAuthor(author);
                // 更新时间
                String updateDate = element.getElementsByTag("span").eq(3).text();
                bookData.setUpdateDate(updateDate);

                if (!"".equals(bookName)) {
                    bookDataList.add(bookData);
                }
            }
        } catch (Exception e) {
            if (index == 0) {
                return null;
            }

            index--;
            getBookList(searchBookName);
        }

        return bookDataList;
    }

    /**
     * 根据链接，爬取指定书籍章节列表列表
     *
     * @param link 书籍链接
     */
    @Override
    public void getBookChapterList(String link) {
        DataCenter.chapters.clear();
        String result = HttpUtil.get(link);
        try {
            Document html = Jsoup.parse(result);
            Element list = html.getElementById("list");
            Elements item = list.getElementsByTag("a");
            for (Element element : item) {
                Chapter chapter = new Chapter();
                // 链接
                String href = element.attr("href");
                // 名称
                String name = element.text();
                chapter.setName(name);
                chapter.setLink("https://www.ibiquge.la/" + href);
                DataCenter.chapters.add(chapter);
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }
            index--;
            getBookChapterList(link);
        }
    }

    /**
     * 根据链接爬取书籍指定章节内容
     *
     * @param link 书籍链接
     */
    @Override
    public void getBookChapterContent(String link) {
        Document parse = null;

        Element content;

        String textContent;

        try {
            parse = Jsoup.parse(new URL(link), 60000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 妙笔阁
        assert parse != null;
        content = parse.getElementById("content");
        textContent = textFormat(content);
        String ad1 = "您可以在百度里搜索";
        String ad2 = "查找最新章节！";
        int adStart = textContent.indexOf(ad1);
        int adEnd = textContent.indexOf(ad2) + ad2.length();
        if (adStart >= 0 && adEnd > 0)
            textContent = textContent.replace(textContent.substring(adStart, adEnd), "");
        DataCenter.textContent = textContent;
    }

    /**
     * 解析章节内容
     *
     * @param element 爬取的内容节点
     * @return 内容
     */
    @Override
    public String textFormat(Element element) {
        // 用于存储节点
        StringBuilder stringBuilder = new StringBuilder();

        // 遍历内容节点
        for (Node childNode : element.childNodes()) {
            // 是否为文本
            if (childNode instanceof TextNode) {
                stringBuilder.append("  ");
                stringBuilder.append(((TextNode) childNode).text());
            }

            // 是否为标签
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;

                if (childElement.tag().getName().equalsIgnoreCase("p")) {
                    stringBuilder.append("\n");
                }

                // 递归下一次 并存储
                stringBuilder.append("  ");
                stringBuilder.append(textFormat(childElement));
            }
        }
        // 返回格式化后的目录
        return stringBuilder.toString();
    }
}
