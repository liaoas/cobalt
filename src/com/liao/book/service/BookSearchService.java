package com.liao.book.service;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.BookData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    public List<BookData> searchBookNameDate(String searchBookName) {
        List<BookData> bookDataList = new ArrayList<>();
        String url = "http://www.paoshuzw.com/modules/article/waps.php";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchkey", searchBookName);

        String result1 = HttpUtil.post(url,paramMap);
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
}
