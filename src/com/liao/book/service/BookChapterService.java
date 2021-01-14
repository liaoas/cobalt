package com.liao.book.service;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <p>
 * 爬取章节
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class BookChapterService {

    public static void searchBookChapterDate(String link) {
        String url = "http://www.xbiquge.la/25/25430/";
        DataCenter.chapters.clear();
        String result1= HttpUtil.get(link);
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
                chapter.setLink("http://www.xbiquge.la/"+attr);

                DataCenter.chapters.add(chapter);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
