package com.cobalt.service.impl;

import cn.hutool.http.HttpUtil;
import com.cobalt.persistence.ReadingProgressDao;
import com.cobalt.persistence.SpiderActionDao;
import com.cobalt.common.ModuleConstants;
import com.cobalt.entity.BookData;
import com.cobalt.service.SearchService;
import com.rabbit.foot.core.factory.ResolverFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
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

    static SpiderActionDao spiderActionDao = SpiderActionDao.getInstance();

    // 阅读进度持久化
    static ReadingProgressDao readingProgressDao = ReadingProgressDao.getInstance();

    /**
     * 判断数据源
     *
     * @param searchBookName 书名
     * @return 结果
     */
    @Override
    public List<BookData> getBookNameData(String searchBookName) {

        return switch (readingProgressDao.searchType) {
            case ModuleConstants.XIANG_SHU ->
                // 笔趣阁
                    searchBookNameData(searchBookName);
            case ModuleConstants.BI_QU_GE ->
                // 笔趣阁2
                    searchBookNameData_bqg2(searchBookName);
            default -> {
                ResolverFactory<BookData> search = new ResolverFactory<>(spiderActionDao.spiderActionStr, readingProgressDao.searchType, "search", searchBookName);
                yield search.capture();
            }
        };
    }


    /**
     * 香书小说
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

                if (!bookName.isEmpty()) {
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
     * 笔趣阁
     *
     * @param searchBookName 书籍名称
     * @return 搜索列表
     */
    public List<BookData> searchBookNameData_bqg2(String searchBookName) {
        bookDataList.clear();
        String url = "https://www.biquge5200.com/modules/article/search.php?searchkey=" + searchBookName;

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
                bookData.setBookLink("https://www.biquge5200.com/" + bookLink);
                // 章节信息
                String chapter = element.getElementsByTag("a").eq(1).text();
                bookData.setChapter(chapter);
                // 作者
                String author = element.getElementsByTag("td").eq(2).text();
                bookData.setAuthor(author);
                // 更新时间
                String updateDate = element.getElementsByTag("td").eq(4).text();
                bookData.setUpdateDate(updateDate);

                if (!bookName.isEmpty()) {
                    bookDataList.add(bookData);
                }
            }
        } catch (Exception ignored) {
        }
        return bookDataList;
    }

}
