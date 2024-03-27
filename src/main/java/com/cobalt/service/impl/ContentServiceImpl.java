package com.cobalt.service.impl;

import com.cobalt.persistence.ReadingProgressDao;
import com.cobalt.persistence.SpiderActionDao;
import com.cobalt.common.ModuleConstants;
import com.cobalt.entity.ImportBookData;
import com.cobalt.service.ContentService;
import com.rabbit.foot.core.factory.ResolverFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.net.URL;
import java.util.List;
import java.util.Map;

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

    static SpiderActionDao spiderActionDao = SpiderActionDao.getInstance();

    /**
     * 获取章节内容
     *
     * @param url 链接
     */
    @Override
    public void searchBookChapterData(String url) {

        Document parse;

        Element content;

        String textContent;

        try {
            switch (instance.searchType) {
                case ModuleConstants.XIANG_SHU:
                    url = "https://www.xbiquge.la/" + url;
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 笔趣阁
                    content = parse.getElementById("content");
                    instance.textContent = textFormat(content);
                    break;
                case ModuleConstants.BI_QU_GE:
                    parse = Jsoup.parse(new URL(url), 60000);
                    // 笔趣阁2
                    content = parse.getElementById("content");
                    textContent = textFormat(content);
                    instance.textContent = textContent;
                    break;
                case ModuleConstants.IMPORT:
                    getImportBook(url);
                    break;
                default:
                    ResolverFactory<String> search = new ResolverFactory<>(spiderActionDao.spiderActionStr, instance.searchType, "content", url);

                    List<String> capture = search.capture();

                    instance.textContent = capture.get(0);
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
     * 书籍内容格式化
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
            if (childNode instanceof Element childElement) {

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
     * 获取手动导入的章节内容
     *
     * @param url 链接/map key
     */
    @Override
    public void getImportBook(String url) {
        ImportBookData instance1 = ImportBookData.getInstance();

        Map<String, String> bookMap = instance1.getBookMap();

        instance.textContent = bookMap.get(url);
    }
}
