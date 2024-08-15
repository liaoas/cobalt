package com.cobalt.parser.content;

import com.cobalt.parser.book.BookMetadata;
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
        Map<String, String> bookMap = BookMetadata.getInstance().getBookMap();

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
