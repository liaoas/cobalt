package com.cobalt.parser.content;

import com.cobalt.common.constant.UIConstants;
import com.cobalt.framework.persistence.ReadingProgressPersistent;

/**
 * <p>
 * 爬取当前章节信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class ContentParserFacade {
    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    public boolean initContent(Object object) {
        boolean result;

        if (instance.searchType.equals(UIConstants.IMPORT)) {
            result = new FileContentParser().parser(object);
        } else {
            result = new NetworkContentParser().parser(object);
        }

        if (instance.textContent == null) {
            return false;
        }
        return result;
    }
}
