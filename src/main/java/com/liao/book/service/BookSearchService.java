package com.liao.book.service;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.liao.book.entity.BookData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class BookSearchService {

    public List<BookData> searchBookNameData(String searchBookName) {
        List<BookData> bookDataList = new ArrayList<>();
        String url = "https://www.xbiquge.la/modules/article/waps.php";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchkey", searchBookName);

        String result1 = HttpUtil.post(url, paramMap);

        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByTag("tr");

            Iterator it = grid.iterator();

            while (it.hasNext()) {
                Element element = (Element) it.next();

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
            e.printStackTrace();
        }

        return bookDataList;
    }

    public List<BookData> searchBookNameData_miao(String searchBookName) {
        List<BookData> bookDataList = new ArrayList<>();
        String url = "https://www.imiaobige.com/search.html";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchkey", searchBookName);

        String result1 = HttpUtil.post(url, paramMap);

        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByTag("dl");

            Iterator it = grid.iterator();

            while (it.hasNext()) {
                Element element = (Element) it.next();

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
            e.printStackTrace();
        }

        return bookDataList;
    }

    public List<BookData> searchBookNameData_tai(String searchBookName) {
        List<BookData> bookDataList = new ArrayList<>();
        String url = "https://www.taiuu.com/modules/article/search.php";

        UrlQuery urlQuery = new UrlQuery();
        urlQuery.add("searchkey", searchBookName);

        UrlBuilder urlBuilder = UrlBuilder.ofHttp(url, Charset.forName("GBK")).setQuery(urlQuery);
        String result1 = new HttpRequest(urlBuilder).charset("GBK").execute().body();

        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByTag("tr");

            Iterator it = grid.iterator();

            while (it.hasNext()) {
                Element element = (Element) it.next();

                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("a").eq(0).text();
                bookData.setBookName(bookName);
                // 链接
                String bookLink = element.getElementsByTag("a").eq(0).attr("href");
                //bookLink = bookLink.replaceAll("novel", "read");
                //bookLink = bookLink.replaceAll(".html", "");
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
            e.printStackTrace();
        }

        return bookDataList;
    }
}
