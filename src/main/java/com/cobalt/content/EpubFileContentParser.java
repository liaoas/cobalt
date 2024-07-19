package com.cobalt.content;

import com.cobalt.book.BookMetadata;
import com.cobalt.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 本地 Epub 类型书籍单个章节内容解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class EpubFileContentParser extends FileContentParser {

    private final static Logger log = LoggerFactory.getLogger(EpubFileContentParser.class);

    @Override
    public boolean parser(Object object) {
        String url = String.valueOf(object);
        BookMetadata bookData = BookMetadata.getInstance();
        Map<String, String> bookMap = bookData.getBookMap();

        if (bookMap.isEmpty()) {
            log.error("Epub 书籍内容为空！");
            return false;
        }
        int index = Integer.parseInt(bookMap.get(url));
        BookMetadata.initDocument(index);
        instance.textContent = bookMap.get(url);
        return true;
    }
}
