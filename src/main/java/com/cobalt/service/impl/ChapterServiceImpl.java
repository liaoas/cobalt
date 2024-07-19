package com.cobalt.service.impl;

import cn.hutool.http.HttpUtil;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.entity.Chapter;
import com.cobalt.entity.ImportBookData;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.cobalt.service.ChapterService;
import com.rabbit.foot.common.enums.ReptileType;
import com.rabbit.foot.core.factory.ResolverFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * <p>
 * 爬取数据章节列表
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class ChapterServiceImpl implements ChapterService {

    // 重试次数
    public static int index = 2;

    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();

    /**
     * 爬取章节信息
     *
     * @param link 链接
     */
    @Override
    public void getBookChapterByType(String link) {
        if (instance.searchType.equals(ModuleConstants.IMPORT)) {
            importChapterData();
        } else {
            rabbitFootChapterData(link);
        }
    }

    @Override
    public void rabbitFootChapterData(String link) {

        instance.chapters.clear();

        ResolverFactory<Chapter> search = new ResolverFactory<>(spiderActionDao.spiderActionStr, instance.searchType, ReptileType.CHAPTER, link);

        List<Chapter> capture = search.capture();

        instance.chapters.addAll(capture);
    }

    /**
     * 加载手动导入的章节信息
     */
    @Override
    public void importChapterData() {
        instance.chapters.clear();

        ImportBookData importBookData = ImportBookData.getInstance();
        List<Chapter> chapterList = importBookData.getChapterList();
        instance.chapters.addAll(chapterList);
    }
}
