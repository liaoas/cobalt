package com.liao.book.service;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * <p>
 * 爬取章节
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class BookChapterService {

    // 重试次数
    public static int index = 2;

    /**
     * 笔趣阁书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData(String link) {

        // String url = "http://www.xbiquge.la/25/25430/";
        DataCenter.chapters.clear();
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

                DataCenter.chapters.add(chapter);
            }
        } catch (Exception e) {
            // 判断次数
            if (index == 0) {
                return;
            }
            index--;
            searchBookChapterData(link);
        }

    }

    /**
     * 妙笔阁书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData_miao(String link) {

        DataCenter.chapters.clear();

        String result1 = HttpUtil.get(link);

        try {
            Document parse = Jsoup.parse(result1);
            Elements border_line = parse.getElementsByClass("border-line");
            Iterator it = border_line.iterator();
            it.next();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                Elements grid = element.parent().getElementsByTag("li");
                for (Element element2 : grid) {
                    Chapter chapter = new Chapter();

                    // 链接
                    String attr = element2.getElementsByTag("a").eq(0).attr("href");
                    // 名称
                    String name = element2.getElementsByTag("a").eq(0).text();

                    chapter.setName(name);
                    chapter.setLink("https://www.imiaobige.com/" + attr);
                    if (attr.contains("read")) {
                        DataCenter.chapters.add(chapter);
                    }
                }
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }

            index--;
            searchBookChapterData_miao(link);
        }

    }


    /**
     * 全本小说网书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData_tai(String link) {

        DataCenter.chapters.clear();

        String result1 = HttpUtil.get(link);
        try {
            Document parse = Jsoup.parse(result1);
            int begin = parse.getElementsByTag("dl").get(0).getElementsByTag("dt").get(1).siblingIndex() + 1;
            Elements grid = parse.getElementsByTag("dl").get(0).children();

            for (int i = begin; i < grid.size(); i++) {
                Element element = grid.get(i);

                Chapter chapter = new Chapter();
                // 链接
                String attr = element.getElementsByTag("a").eq(0).attr("href");
                // 名称
                String name = element.getElementsByTag("a").eq(0).text();

                chapter.setName(name);
                chapter.setLink(link + attr);
                if (!name.isEmpty()) {
                    DataCenter.chapters.add(chapter);
                }
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }
            index--;
            searchBookChapterData_tai(link);
        }
    }

    /**
     * 笔趣阁书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData_bqg2(String link) {

        DataCenter.chapters.clear();

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
                chapter.setLink("https://www.biduoxs.com" + attr);

                DataCenter.chapters.add(chapter);
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }

            index--;
            searchBookChapterData_bqg2(link);
        }
    }

    /**
     * 69书吧书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData_69shu(String link) {

        DataCenter.chapters.clear();

        String result1 = HttpUtil.get(link);
        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementsByClass("chapterlist").get(1).getElementsByTag("li");

            for (Element element : grid) {
                Chapter chapter = new Chapter();
                // 链接
                String attr = element.getElementsByTag("a").eq(0).attr("href");
                // 名称
                String name = element.getElementsByTag("a").eq(0).text();

                chapter.setName(name);
                chapter.setLink(link + attr);

                DataCenter.chapters.add(chapter);
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }

            index--;
            searchBookChapterData_69shu(link);
        }
    }

    /**
     * 58小说书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData_58(String link) {

        DataCenter.chapters.clear();

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
                chapter.setLink("http://www.wbxsw.com" + attr);

                DataCenter.chapters.add(chapter);
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }

            index--;
            searchBookChapterData_58(link);
        }
    }

    /**
     * 58小说书籍爬取
     *
     * @param link 链接
     */
    public static void searchBookChapterData_top(String link) {

        DataCenter.chapters.clear();

        String result1 = HttpUtil.get(link);
        try {
            Document parse = Jsoup.parse(result1);
            Elements grid = parse.getElementById("readerlists").getElementsByTag("li");

            for (Element element : grid) {
                Chapter chapter = new Chapter();
                // 链接
                String attr = element.getElementsByTag("a").eq(0).attr("href");
                // 名称
                String name = element.getElementsByTag("a").eq(0).text();

                chapter.setName(name);
                chapter.setLink("https://www.maxreader.net" + attr);

                if (!name.equals("")) {
                    DataCenter.chapters.add(chapter);
                }
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }

            index--;
            searchBookChapterData_top(link);
        }
    }
}
