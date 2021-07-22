package com.liao.book.service;

import com.liao.book.entity.DataCenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.net.URL;

/**
 * <p>
 * 爬取当前章节信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class BookTextService {

    // 重试次数
    public static int index = 2;

    public static void searchBookChapterData(String url) {
        // String result1 = HttpUtil.get(url);
        try {
            // Document parse = Jsoup.parse(result1);
            Document parse = Jsoup.parse(new URL(url), 60000);
            if (url.contains("biquge5200")) {
                Element content = parse.getElementById("content");
                DataCenter.textContent = textFormat(content);
            } else if (url.contains("imiaobige")) {
                Element content = parse.getElementById("content");
                String textContent = textFormat_miao(content);
                String ad1 = "您可以在百度里搜索";
                String ad2 = "查找最新章节！";
                int adStart = textContent.indexOf(ad1);
                int adEnd = textContent.indexOf(ad2) + ad2.length();
                if (adStart >= 0 && adEnd > 0)
                    textContent = textContent.replace(textContent.substring(adStart, adEnd), "");
                DataCenter.textContent = textContent;
            } else if (url.contains("taiuu")) {
                Element content = parse.getElementById("htmlContent");
                String textContent = textFormat(content);
                textContent = textContent.replace("<太-悠悠>小说щww.taiuu.com", "");
                textContent = textContent.replace("(全本小说网，www.TAIUU.COM)", "");
                textContent = textContent.replace("(全本小说网，www.taiuu.com，；手机阅读，m.taiuu.com｛太}{悠悠}小说 щww{taiuu][com}", "");
                DataCenter.textContent = textContent;
            } else if (url.contains("biduoxs")) {
                Element content = parse.getElementById("content");
                String textContent = textFormat(content);
                textContent = textContent.replace("笔趣阁手机端", "");
                textContent = textContent.replace("http://m.biquwu.cc", "");
                textContent = textContent.replace("看更多诱惑小说请关注微信 npxswz    各种乡村 都市 诱惑     ", "");
                textContent = textContent.replace("xh:.126.81.50", "");
                DataCenter.textContent = textContent;
            }
        } catch (Exception e) {
            if (index == 0) {
                return;
            }
            index--;
            searchBookChapterData(url);
        }
    }


    /**
     * 笔趣阁
     *
     * @param element 当前章节信息
     * @return 结果
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

    /**
     * 妙笔阁
     *
     * @param element 章节内容
     * @return 数据
     */
    private static String textFormat_miao(Element element) {

        // 用于存储节点
        StringBuilder stringBuilder = new StringBuilder();

        // 遍历内容节点
        for (Node childNode : element.childNodes()) {
            // 是否为文本
            if (childNode instanceof TextNode) {
                stringBuilder.append("  ");
                stringBuilder.append(((TextNode) childNode).text());
            }

            // 是否为标签
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;

                if (childElement.tag().getName().equalsIgnoreCase("p")) {
                    stringBuilder.append("\n");
                }

                // 递归下一次 并存储
                stringBuilder.append("  ");
                stringBuilder.append(textFormat_miao(childElement));
            }
        }
        // 返回格式化后的目录
        return stringBuilder.toString();
    }
}
