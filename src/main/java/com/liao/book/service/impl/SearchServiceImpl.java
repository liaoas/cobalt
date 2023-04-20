package com.liao.book.service.impl;

import cn.hutool.http.HttpUtil;
import com.liao.book.common.ModuleConstants;
import com.liao.book.persistence.ReadingProgressDao;
import com.liao.book.entity.BookData;
import com.liao.book.service.SearchService;
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
            case ModuleConstants.XIANG_SHU:
                // 笔趣阁
                return searchBookNameData(searchBookName);
            case ModuleConstants.BI_QU_GE:
                // 笔趣阁2
                return searchBookNameData_bqg2(searchBookName);
        }
        return null;
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
     * 笔趣阁
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

}
