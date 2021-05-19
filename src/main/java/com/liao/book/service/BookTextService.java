package com.liao.book.service;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * <p>
 * 爬取当前章节信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class BookTextService {

    public static void searchBookChapterDate(String url) {

        String result1 = HttpUtil.get(url);
        try {
            Document parse = Jsoup.parse(result1);
            Element content = parse.getElementById("content");
            DataCenter.textContent = content.text();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
