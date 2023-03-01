package com.liao.book.service.impl;

import cn.hutool.http.HttpUtil;
import com.liao.book.dao.ReadingProgressDao;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
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
 * 笔趣阁书籍爬虫
 * </p>
 *
 * @author LiAo
 * @since 2022-12-30
 */
public class BiQuGeBookReptile implements BooksReptile {

    // 重试次数
    public static int index = 2;

    // 存储数据
    public static List<BookData> bookDataList = new ArrayList<>();

    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    /**
     * 根据名称爬取书籍列表
     *
     * @param searchBookName 书籍名称
     * @return 书籍列表
     */
    @Override
    public List<BookData> getBookList(String searchBookName) {
        bookDataList.clear();
        try {
            Connection connect = Jsoup.connect("https://www.ibiquge.la/modules/article/waps.php");
            // 设置请求头
            connect.data("searchkey", searchBookName);

            Document document = connect.post();

            // Document parse = Jsoup.parse(result1);
            Elements grid = document.getElementsByTag("tr");
            for (Element element : grid) {
                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("a").eq(0).text();
                bookData.setBookName(bookName);
                // 链接
                String bookLink = element.getElementsByTag("a").eq(0).attr("href");
                bookData.setBookLink(bookLink);
                // 章节信息
                String chapter = element.getElementsByTag("a").eq(1).text();
                bookData.setChapter(chapter);
                // 作者
                String author = element.getElementsByTag("td").eq(2).text();
                bookData.setAuthor(author);
                // 更新时间
                String updateDate = element.getElementsByTag("td").eq(3).text();
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
        instance.chapters.clear();
        String result1 = HttpUtil.get(link);
        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByTag("dd");

            for (Element element : grid) {
                Chapter chapter = new Chapter();
                // 链接
                String attr = element.getElementsByTag("a").eq(0).attr("href");
                // 名称
                String name = element.getElementsByTag("a").eq(0).text();

                chapter.setName(name);
                // chapter.setLink("https://www.xbiquge.la/" + attr);
                chapter.setLink(attr);

                instance.chapters.add(chapter);
            }
        } catch (Exception e) {
            // 判断次数
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
        link = "https://www.xbiquge.la/" + link;
        try {
            parse = Jsoup.parse(new URL(link), 60000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 笔趣阁
        assert parse != null;
        content = parse.getElementById("content");
        instance.textContent = textFormat(content);
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
                stringBuilder.append(((TextNode) childNode).text());
            }

            // 是否为标签
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;

                if (childElement.tag().getName().equalsIgnoreCase("br")) {
                    stringBuilder.append("\n");
                }

                // 递归下一次 并存储
                stringBuilder.append(textFormat(childElement));
            }
        }
        // 返回格式化后的目录
        return stringBuilder.toString();
    }
}
