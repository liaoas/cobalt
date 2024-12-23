package com.cobalt.parser.content;

import com.cobalt.common.constant.UIConstants;
import com.cobalt.framework.persistence.proxy.ReadingProgressProxy;
import com.rabbit.foot.utils.ObjUtil;

/**
 * <p>
 * 爬取当前章节信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class ContentParserFacade {

    public boolean initContent(Object object) {
        boolean result;

        ReadingProgressProxy readingProgress = new ReadingProgressProxy();

        if (readingProgress.getSearchType().equals(UIConstants.IMPORT)) {
            result = new FileContentParser().parser(object);
        } else {
            result = new NetworkContentParser().parser(object);
        }

        if (ObjUtil.isNull(readingProgress.getTextContent())) {
            return false;
        }
        return result;
    }
}
