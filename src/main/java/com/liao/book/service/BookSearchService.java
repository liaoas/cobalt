package com.liao.book.service;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.liao.book.entity.BookData;
import com.liao.book.entity.DataCenter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class BookSearchService {

    // 重试次数
    public static int index = 2;

    // 存储数据
    public static List<BookData> bookDataList = new ArrayList<>();

    /**
     * 判断数据源
     *
     * @param searchType     类型
     * @param searchBookName 书名
     * @return 结果
     */
    public List<BookData> getBookNameData(String searchType, String searchBookName) {

        switch (searchType) {
            case DataCenter.BI_QU_GE:
                // 笔趣阁
                return searchBookNameData(searchBookName);
            case DataCenter.MI_BI_GE:
                // 妙笔阁
                return searchBookNameData_miao(searchBookName);
            case DataCenter.QUAN_BEN:
                // 全本小说网
                return searchBookNameData_tai(searchBookName);
            case DataCenter.BI_QU_GE_2:
                //笔趣阁2
                return searchBookNameData_bqg2(searchBookName);
            case DataCenter.SHU_BA_69:
                return searchBookNameData_69shu(searchBookName);
        }
        return null;
    }


    /**
     * 笔趣阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    private List<BookData> searchBookNameData(String searchBookName) {
        bookDataList.clear();
        try {
            Connection connect = Jsoup.connect("https://www.xbiquge.la/modules/article/waps.php");
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
            searchBookNameData(searchBookName);
        }

        return bookDataList;
    }

    /**
     * 妙笔阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    private List<BookData> searchBookNameData_miao(String searchBookName) {
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
            searchBookNameData(searchBookName);
        }

        return bookDataList;
    }

    /**
     * 全本小说网
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    private List<BookData> searchBookNameData_tai(String searchBookName) {
        bookDataList.clear();
        String url = "https://xqb5200.com/modules/article/search.php";

        UrlQuery urlQuery = new UrlQuery();
        urlQuery.add("searchkey", searchBookName);

        UrlBuilder urlBuilder = UrlBuilder.ofHttp(url, Charset.forName("GBK")).setQuery(urlQuery);
        String result1 = new HttpRequest(urlBuilder).charset("GBK").execute().body();

        try {
            Document parse = Jsoup.parse(result1);

            Elements grid = parse.getElementsByTag("li");

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
                String author = element.getElementsByClass("s4").eq(0).text();
                bookData.setAuthor(author);
                // 更新时间
                String updateDate = element.getElementsByClass("s5").eq(0).text();
                bookData.setUpdateDate(updateDate);

                if (!"".equals(bookName) && (!author.equals("") || !updateDate.equals(""))) {
                    bookDataList.add(bookData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookDataList;
    }

    /**
     * 笔趣阁www.biduoxs.com
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    private List<BookData> searchBookNameData_bqg2(String searchBookName) {
        bookDataList.clear();
        String url = "https://www.biduoxs.com/search.php";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", searchBookName);

        String result1 = HttpUtil.get(url, paramMap);

        try {
            Document parse = Jsoup.parse(result1);

            Elements grid = parse.getElementsByClass("result-game-item-detail");

            for (Element element : grid) {
                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("span").eq(0).text();
                bookData.setBookName(bookName);
                // 链接
                String bookLink = "https://www.biduoxs.com" + element.getElementsByTag("a").eq(0).attr("href");
                bookData.setBookLink(bookLink);
                // 章节信息
                String chapter = element.getElementsByClass("result-game-item-info-tag-item").eq(0).text();
                bookData.setChapter(chapter);
                // 作者
                String author = element.getElementsByTag("span").eq(2).text();
                bookData.setAuthor(author);
                // 更新时间
                String updateDate = element.getElementsByTag("span").eq(6).text();
                bookData.setUpdateDate(updateDate);

                if (!"".equals(bookName)) {
                    bookDataList.add(bookData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookDataList;
    }

    /**
     * 69书吧www.69shuba.cc
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    private List<BookData> searchBookNameData_69shu(String searchBookName) {
        List<BookData> bookDataList = new ArrayList<>();
        String url = "https://www.69shuba.cc/modules/article/search.php";

        UrlQuery urlQuery = new UrlQuery();
        urlQuery.add("searchkey", searchBookName);
        urlQuery.add("submit", "搜索");

        UrlBuilder urlBuilder = UrlBuilder.ofHttp(url, Charset.forName("GBK")).setQuery(urlQuery);
        String result1 = new HttpRequest(urlBuilder).charset("GBK").execute().body();

        try {
            Document parse = Jsoup.parse(result1);

            Elements grid = parse.getElementsByTag("tr");

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
                String updateDate = element.getElementsByTag("td").eq(4).text();
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
            searchBookNameData(searchBookName);
        }
        return bookDataList;
    }


}
