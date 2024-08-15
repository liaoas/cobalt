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
    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

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
}
