package com.liao.book.service.impl;

import com.liao.book.dao.ReadingProgressDao;
import com.liao.book.common.ModuleConstants;
import com.liao.book.service.ContentService;
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
public class ContentServiceImpl implements ContentService {

    // 重试次数
    public static int index = 2;

    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    @Override
    public void searchBookChapterData(String url) {

        Document parse;

        Element content;

        String textContent;

        try {
            switch (instance.searchType) {
                case ModuleConstants.BI_QU_GE:
                    url = "https://www.xbiquge.la/" + url;
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 笔趣阁
                    content = parse.getElementById("content");
                    instance.textContent = textFormat(content);
                    break;
                case ModuleConstants.MI_BI_GE:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 妙笔阁
                    content = parse.getElementById("content");
                    textContent = textFormat_miao(content);
                    String ad1 = "您可以在百度里搜索";
                    String ad2 = "查找最新章节！";
                    int adStart = textContent.indexOf(ad1);
                    int adEnd = textContent.indexOf(ad2) + ad2.length();
                    if (adStart >= 0 && adEnd > 0)
                        textContent = textContent.replace(textContent.substring(adStart, adEnd), "");
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.QUAN_BEN:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 全本小说网
                    content = parse.getElementById("content");
                    textContent = textFormat(content);
                    textContent = textContent.replace("全本小说网 www.xqb5200.com，最快更新", "");
                    textContent = textContent.replace(" ！", "");
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.QIAN_QIAN:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 千千小说网
                    content = parse.getElementById("content");
                    textContent = textFormat(content);
                    textContent = textContent.replace("千千小说网 www.qqxsw.co，最快更新", "");
                    textContent = textContent.replace(" ！", "");
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.BI_QU_GE_2:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 笔趣阁2
                    content = parse.getElementById("content");
                    textContent = textFormat(content);
                    textContent = textContent.replace("笔趣阁手机端", "");
                    textContent = textContent.replace("http://m.biquwu.cc", "");
                    textContent = textContent.replace("看更多诱惑小说请关注微信 npxswz    各种乡村" +
                            " 都市 诱惑     ", "");
                    textContent = textContent.replace("xh:.126.81.50", "");
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.SHU_BA_69:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 69书吧
                    content = parse.getElementById("htmlContent");
                    textContent = textFormat(content);
                    textContent = textContent.replace("xh211", "");
                    textContent = textContent.replace(" 69书吧 www.69shuba.cc，最快更新", "");
                    textContent = textContent.replace("最新章节！", "");
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.SHU_BA_58:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 58小说
                    content = parse.getElementById("content");
                    textContent = textFormat(content);
                    textContent = textContent.replace("提供无弹窗全字在线阅读，更新速度更快章质量更好，如果您觉得不" +
                            "错就多多分享本站!谢谢各位读者的支持!", "");
                    textContent = textContent.replace("高速首发", "");
                    textContent = textContent.replace("最新章节", "");
                    textContent = textContent.replace("地址为如果你觉的本章节还不错的话请不要忘记向您QQ群和微博里的" +
                            "朋友推荐哦！", "");
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.SHU_TOP:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 顶点小说
                    content = parse.getElementsByClass("pt-read-text").get(0);
                    textContent = textFormat_miao(content);
                    textContent = textContent.replace("欢迎广大书友光临阅读，最新、最快、最火的连载作品尽在！" +
                            "手机用户请到m.阅读。  百度一下“", "\n");
                    textContent = textContent.replace("百度一下“", "\n");
                    textContent = textContent.replace("顶点小说www.maxreader.net”最新章节第一时间免费阅读。"
                            , "");
                    instance.textContent = textContent;
                    break;
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
    @Override
    public String textFormat(Element element) {

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
    @Override
    public String textFormat_miao(Element element) {

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
