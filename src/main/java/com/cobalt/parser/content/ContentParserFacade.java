package com.cobalt.parser.content;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.parser.book.BookMetadata;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ContentParserFacade {

    private final static Logger log = LoggerFactory.getLogger(ContentParserFacade.class);

    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();

    public boolean initContent(Object object) {
        boolean result;

        if (instance.searchType.equals(ModuleConstants.IMPORT)) {
            result = new FileContentParser().parser(object);
        } else {
            result = new NetworkContentParser().parser(object);
        }

        if (instance.textContent == null) {
            return false;
        }

        return result;

    }

    /**
     * 获取章节内容
     *
     * @param url 链接
     */
    public void searchBookChapterData(String url) {
        try {
            switch (instance.searchType) {
                case ModuleConstants.DEFAULT_DATA_SOURCE_NAME:
                    break;
                case ModuleConstants.IMPORT:
                    getImportBook(url);
                    break;
                default:
                    ResolverFactory<String> search = new ResolverFactory<>(spiderActionDao.spiderActionStr,
                            instance.searchType, ReptileType.CONTENT, url);
                    List<String> capture = search.capture();
                    instance.textContent = capture.get(0);
                    break;
            }
        } catch (Exception e) {
            log.error("章节内容加载失败 url：{}", url);
        }
    }


    /**
     * 获取手动导入的章节内容
     *
     * @param url 链接/map key
     */
    public void getImportBook(String url) {
        BookMetadata bookData = BookMetadata.getInstance();
        Map<String, String> bookMap = bookData.getBookMap();
        if (instance.bookType.equals(Constants.EPUB_STR_LOWERCASE) && !bookMap.isEmpty()) {
            int index = Integer.parseInt(bookMap.get(url));
            BookMetadata.initDocument(index);
        }
        instance.textContent = bookMap.get(url);
    }
}
