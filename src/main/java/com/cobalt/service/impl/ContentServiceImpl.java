package com.cobalt.service.impl;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.common.domain.ImportBookData;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.cobalt.service.ContentService;
import com.cobalt.viewer.HTMLDocumentFactory;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;

import javax.swing.text.html.HTMLDocument;
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
        try {
            switch (instance.searchType) {
                case ModuleConstants.DEFAULT_DATA_SOURCE_NAME:
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
     * 获取手动导入的章节内容
     *
     * @param url 链接/map key
     */
    @Override
    public void getImportBook(String url) {
        ImportBookData bookData = ImportBookData.getInstance();

        Map<String, String> bookMap = bookData.getBookMap();

        if (instance.bookType.equals(Constants.EPUB_STR_LOWERCASE) && !bookMap.isEmpty()) {
            int index = Integer.parseInt(bookMap.get(url));
            Book book = bookData.getEpubBookBook();
            Resource resource = book.getTableOfContents().getTocReferences().get(index).getResource();
            Navigator navigator = new Navigator(book);
            HTMLDocumentFactory htmlDocumentFactory = new HTMLDocumentFactory(navigator, bookData.getTextContent().getEditorKit());
            htmlDocumentFactory.init(book);
            HTMLDocument document = htmlDocumentFactory.getDocument(resource);
            bookData.getTextContent().setDocument(document);
            bookData.setBookHTMLDocument(document);
        }

        instance.textContent = bookMap.get(url);
    }
}
