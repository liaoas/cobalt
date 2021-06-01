package com.liao.book.service;

import cn.hutool.http.HttpUtil;
import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

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

            DataCenter.textContent = textFormat(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 格式化当前章节内容
     *
     * @param element 当前章节信息
     * @return
     */
    private static String textFormat(Element element) {

        // 用于存储节点
        StringBuilder stringBuilder = new StringBuilder();

        // 遍历内容节点
        for (Node childNode : element.childNodes()) {
            // 是否为文本
            if (childNode instanceof TextNode) {
                stringBuilder.append(((TextNode) childNode).text());
            }

            // 是否为标签
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;

                if (childElement.tag().getName().equalsIgnoreCase("br")) {
                    stringBuilder.append("\n");
                }

                // 递归下一次 并存储
                stringBuilder.append(textFormat(childElement));
            }
        }
        // 返回格式化后的目录
        return stringBuilder.toString();
    }
}
