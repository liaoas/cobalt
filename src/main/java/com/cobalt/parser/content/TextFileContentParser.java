package com.cobalt.parser.content;

import com.cobalt.parser.book.BookMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Text文件类型的单个章节内容解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class TextFileContentParser extends FileContentParser {

    private final static Logger log = LoggerFactory.getLogger(TextFileContentParser.class);

    @Override
    public boolean parser(Object object) {
        String url = String.valueOf(object);
        BookMetadata bookData = BookMetadata.getInstance();
        Map<String, String> bookMap = bookData.getBookMap();

        if (bookMap.isEmpty()) {
            log.error("Epub 书籍内容为空！");
            return false;
        }
        readingProgress.setTextContent(bookMap.get(url));
        return true;
    }
}
