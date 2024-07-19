package com.cobalt.content;

import com.cobalt.common.constant.Constants;

/**
 * 文件类型的单个章节内容解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class FileContentParser extends AbstractContentParser {

    @Override
    public boolean parser(Object bookName) {
        if (instance.bookType.equals(Constants.EPUB_STR_LOWERCASE)) {
            return new EpubFileContentParser().parser(bookName);
        }
        return new TextFileContentParser().parser(bookName);
    }
}
