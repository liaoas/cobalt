package com.liao.book.service.impl;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.liao.book.dao.ReadingProgressDao;
import com.liao.book.entity.BookData;
import com.liao.book.common.ModuleConstants;
import com.liao.book.service.SearchService;
import org.jsoup.Connection;
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
public class SearchServiceImpl implements SearchService {

    // 重试次数
    public static int index = 2;

    // 存储数据
    public static List<BookData> bookDataList = new ArrayList<>();

    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    /**
     * 判断数据源
     *
     * @param searchBookName 书名
     * @return 结果
     */
    @Override
    public List<BookData> getBookNameData(String searchBookName) {

        switch (instance.searchType) {
            case ModuleConstants.BI_QU_GE:
                // 笔趣阁
                return searchBookNameData(searchBookName);
            case ModuleConstants.MI_BI_GE:
                // 妙笔阁
                return searchBookNameData_miao(searchBookName);
            case ModuleConstants.QUAN_BEN:
                // 全本小说网
                return searchBookNameData_tai(searchBookName);
            case ModuleConstants.BI_QU_GE:
                // 笔趣阁2
                return searchBookNameData_bqg2(searchBookName);
            case ModuleConstants.SHU_BA_69:
                // 69书吧
                return searchBookNameData_69shu(searchBookName);
            case ModuleConstants.SHU_BA_58:
                // 58小说
                return searchBookNameData_58(searchBookName);
            case ModuleConstants.SHU_TOP:
                // 顶点小说
                return searchBookNameData_top(searchBookName);
            case ModuleConstants.QIAN_QIAN:
                //千千小说
                return searchBookNameData_qian(searchBookName);
        }
        return null;
    }


    /**
     * 笔趣阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    @Override
    public List<BookData> searchBookNameData(String searchBookName) {
        bookDataList.clear();
        try {
            Connection connect = Jsoup.connect("https://www.ibiquges.com/modules/article/waps.php");
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
    @Override
    public List<BookData> searchBookNameData_miao(String searchBookName) {
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
    @Override
    public List<BookData> searchBookNameData_tai(String searchBookName) {
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
     * 笔趣阁 www.biduoxs.com www.biquge5200.com
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    public List<BookData> searchBookNameData_bqg2(String searchBookName) {
        bookDataList.clear();
        String url = "http://www.biquge5200.com/modules/article/search.php?searchkey=" + searchBookName;

        String result1 = HttpUtil.get(url);

        try {
            Document parse = Jsoup.parse(result1);

            // Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByTag("tr");
            for (Element element : grid) {
                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("a").eq(0).text();
                bookData.setBookName(bookName);
                // 链接
                String bookLink = element.getElementsByTag("a").eq(0).attr("href");
                bookData.setBookLink("http://www.biquge5200.com/" + bookLink);
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
     * 69书吧www.69shuba.cc
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    @Override
    public List<BookData> searchBookNameData_69shu(String searchBookName) {
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

    /**
     * 58小说www.wbxsw.com
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    @Override
    public List<BookData> searchBookNameData_58(String searchBookName) {
        bookDataList.clear();
        String url = "http://www.wbxsw.com/search.php?keyword=" + searchBookName;

       /* HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", searchBookName);*/

        String result1 = HttpUtil.get(url);

        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByClass("result-game-item-detail");

            for (Element element : grid) {
                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("span").eq(0).text();
                bookData.setBookName(bookName);
                // 链接
                String bookLink = "http://www.wbxsw.com" + element.getElementsByTag("a").eq(0).attr("href");
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
     * 顶点小说www.maxreader.net
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    @Override
    public List<BookData> searchBookNameData_top(String searchBookName) {
        bookDataList.clear();
        String url = "https://www.maxreader.net/search/result.html";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchkey", searchBookName);

        String result1 = HttpUtil.get(url, paramMap);

        try {
            Document parse = Jsoup.parse(result1);

            Elements grid = parse.getElementsByClass("pt-rank-detail");

            for (Element element : grid) {
                BookData bookData = new BookData();
                // 文章名称
                String bookName = element.getElementsByTag("a").eq(0).attr("title");
                bookData.setBookName(bookName);
                // 链接
                String bookLink = "https://www.maxreader.net" + element.getElementsByTag("a").eq(0).attr("href").replace("/book/", "/read/");
                bookData.setBookLink(bookLink);
                // 章节信息
                String chapter = element.getElementsByTag("a").eq(4).attr("title");
                bookData.setChapter(chapter);
                // 作者
                String author = element.getElementsByTag("a").eq(2).attr("title");
                bookData.setAuthor(author);
                // 更新时间
                String updateDate = "";
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
     * 千千小说网 www.qqxsw.co
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    @Override
    public List<BookData> searchBookNameData_qian(String searchBookName) {
        bookDataList.clear();
        String url = "https://www.qqxsw.co/modules/article/search.php";

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

}
