package com.cobalt.parser.content;

import com.cobalt.framework.persistence.ReadingProgress;
import com.cobalt.framework.persistence.SpiderAction;
import com.cobalt.framework.persistence.proxy.ReadingProgressProxy;
import com.cobalt.framework.persistence.proxy.SpiderActionProxy;

/**
 * {@link ContentParser}实现的抽象基类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public abstract class AbstractContentParser implements ContentParser {

    public final ReadingProgressProxy readingProgress;

    public final SpiderActionProxy spiderAction;

    public AbstractContentParser() {
        readingProgress = new ReadingProgressProxy();
        spiderAction = new SpiderActionProxy();
    }


}
