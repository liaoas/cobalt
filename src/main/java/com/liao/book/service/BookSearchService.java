package com.liao.book.service;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.liao.book.entity.BookData;
import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<BookData> bookDataList = new ArrayList<>();
        String url = "https://www.xbiquge.la/modules/article/waps.php";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchkey", searchBookName);

        String result1 = HttpUtil.post(url, paramMap);

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

    /**
     * 妙笔阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    private List<BookData> searchBookNameData_miao(String searchBookName) {
        List<BookData> bookDataList = new ArrayList<>();

        String url = "https://www.imiaobige.com/search.html";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchkey", searchBookName);

        String result1 = HttpUtil.post(url, paramMap);

        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByTag("dl");

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
            e.printStackTrace();
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
        List<BookData> bookDataList = new ArrayList<>();
        String url = "https://www.taiuu.com/modules/article/search.php";

        UrlQuery urlQuery = new UrlQuery();
        urlQuery.add("searchkey", searchBookName);

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
        List<BookData> bookDataList = new ArrayList<>();
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
}
