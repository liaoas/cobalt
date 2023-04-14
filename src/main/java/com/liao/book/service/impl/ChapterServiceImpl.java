package com.liao.book.service.impl;

import cn.hutool.http.HttpUtil;
import com.liao.book.dao.ReadingProgressDao;
import com.liao.book.entity.Chapter;
import com.liao.book.common.ModuleConstants;
import com.liao.book.service.ChapterService;
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
public class ChapterServiceImpl implements ChapterService {

    // 重试次数
    public static int index = 2;

    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    @Override
    public void getBookChapterByType(String link) {
        switch (instance.searchType) {
            case ModuleConstants.BI_QU_GE:
                // 笔趣阁
                searchBookChapterData(link);
                break;
            case ModuleConstants.MI_BI_GE:
                // 妙笔阁
                searchBookChapterData_miao(link);
                break;
            case ModuleConstants.QUAN_BEN:
            case ModuleConstants.QIAN_QIAN:
                // 全本小说网
                searchBookChapterData_tai(link);
                break;
            case ModuleConstants.BI_QU_GE_2:
                // 笔趣阁2
                searchBookChapterData_bqg2(link);
                break;
            case ModuleConstants.SHU_BA_69:
                // 69书吧
                searchBookChapterData_69shu(link);
                break;
            case ModuleConstants.SHU_BA_58:
                // 58小说
                searchBookChapterData_58(link);
                break;
            case ModuleConstants.SHU_TOP:
                // 顶点小说
                searchBookChapterData_top(link);
                break;
        }
    }

    /**
     * 笔趣阁书籍爬取
     *
     * @param link 链接
     */
    @Override
    public void searchBookChapterData(String link) {

        // String url = "https://www.xbiquge.la/"+link;
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
            searchBookChapterData(link);
        }
    }

    /**
     * 妙笔阁书籍爬取
     *
     * @param link 链接
     */
    @Override
    public void searchBookChapterData_miao(String link) {

        instance.chapters.clear();
        String result1 = HttpUtil.get(link);
        try {
            Document parse = Jsoup.parse(result1);
            Element list = parse.getElementById("list");
            Elements item = list.getElementsByTag("a");
            for (Element element : item) {
                Chapter chapter = new Chapter();
                // 链接
                String href = element.attr("href");
                // 名称
                String name = element.text();
                chapter.setName(name);
                chapter.setLink("https://www.ibiquge.la/" + href);
                instance.chapters.add(chapter);
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
    @Override
    public void searchBookChapterData_tai(String link) {

        instance.chapters.clear();

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
                    instance.chapters.add(chapter);
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
    @Override
    public void searchBookChapterData_bqg2(String link) {

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
                chapter.setLink("https://www.biduoxs.com" + attr);

                instance.chapters.add(chapter);
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
    @Override
    public void searchBookChapterData_69shu(String link) {

        instance.chapters.clear();

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

                instance.chapters.add(chapter);
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
    @Override
    public void searchBookChapterData_58(String link) {

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
                chapter.setLink("http://www.wbxsw.com" + attr);

                instance.chapters.add(chapter);
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
     * 定点小说
     *
     * @param link 链接
     */
    @Override
    public void searchBookChapterData_top(String link) {

        instance.chapters.clear();

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
                    instance.chapters.add(chapter);
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
