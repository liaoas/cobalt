package com.cobalt.parser.content;

import com.rabbit.foot.enums.ReptileType;
import com.rabbit.foot.factory.ResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 网络章节解析
 *
 * @author LiAo
 * @since 2024/7/19
 */
public class NetworkContentParser extends AbstractContentParser {


    private final static Logger log = LoggerFactory.getLogger(TextFileContentParser.class);

    @Override
    public boolean parser(Object object) {
        String url = String.valueOf(object);
        ResolverFactory<String> search = new ResolverFactory<>(spiderAction.getSpiderActionStr(),
                readingProgress.getSearchType(), ReptileType.CONTENT, url);
        List<String> capture = null;

        try {
            capture = search.capture();
        } catch (Exception e) {
            log.error("爬取章节内容失败：{}", url);
        }

        if (capture == null || capture.isEmpty()) {
            log.error("爬取章节内容失败：{}", url);
            return false;
        }

        readingProgress.setTextContent(capture.get(0));
        return true;
    }
}
