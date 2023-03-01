package com.liao.book.dao;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.liao.book.entity.Chapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 阅读进度持久化操作
 * </p>
 *
 * @author LiAo
 * @since 2023-02-24
 */
@State(name = "ReadingProgressDao",
        storages = {
                @Storage(value = "reading.progress.dao.xml"
                )
        }
)
public class ReadingProgressDao implements PersistentStateComponent<ReadingProgressDao> {

    // 全局搜索类型
    public String searchType;

    // 当前章节下标
    public int nowChapterIndex;

    // 章节集合
    public List<Chapter> chapters = new ArrayList<>();

    // 章节内容
    public String textContent;

    public ReadingProgressDao() {
    }

    public ReadingProgressDao(String searchType, int nowChapterIndex, List<Chapter> chapters, String textContent) {
        this.searchType = searchType;
        this.nowChapterIndex = nowChapterIndex;
        this.chapters = chapters;
        this.textContent = textContent;
    }

    @Override
    @Nullable
    public ReadingProgressDao getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ReadingProgressDao s) {
        XmlSerializerUtil.copyBean(s, this);
    }

    public static ReadingProgressDao getInstance() {
        return ApplicationManager.getApplication().getService(ReadingProgressDao.class);
    }
}

