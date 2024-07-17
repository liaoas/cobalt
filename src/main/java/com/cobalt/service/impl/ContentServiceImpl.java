package com.cobalt.service.impl;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.common.model.ImportBookData;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.cobalt.service.ContentService;
import com.cobalt.viewer.HTMLDocumentFactory;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javax.swing.text.html.HTMLDocument;
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
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();

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
                    ResolverFactory<String> search = new ResolverFactory<>(spiderActionDao.spiderActionStr, instance.searchType, ReptileType.CONTENT, url);

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
     * 获取手动导入的章节内容
     *
     * @param url 链接/map key
     */
    @Override
    public void getImportBook(String url) {
        ImportBookData bookData = ImportBookData.getInstance();

        Map<String, String> bookMap = bookData.getBookMap();

        if (bookData.getBookType().equals(Constants.EPUB_STR_LOWERCASE) ||
                bookData.getBookType().equals(Constants.EPUB_STR_UPPERCASE)) {

            int i = Integer.parseInt(bookMap.get(url));

            Book book = bookData.getEpubBookBook();

            Resource resource = book.getTableOfContents().getTocReferences().get(i).getResource();

            Navigator navigator = new Navigator(book);
            HTMLDocumentFactory htmlDocumentFactory = new HTMLDocumentFactory(navigator, bookData.getTextContent().getEditorKit());
            htmlDocumentFactory.init(book);
            HTMLDocument document = htmlDocumentFactory.getDocument(resource);
            String string = document.toString();
            bookData.getTextContent().setDocument(document);
        }

        instance.textContent = bookMap.get(url);
    }
}
