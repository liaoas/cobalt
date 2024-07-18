package com.cobalt.framework.persistence;

import com.cobalt.common.model.Chapter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
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
@State(name = "ReadingProgressDao", storages = {@Storage(value = "cobalt.settings.persistent.xml")})
public class ReadingProgressPersistent implements PersistentStateComponent<ReadingProgressPersistent> {

    // 全局搜索类型
    public String searchType;

    // 当前章节下标
    public int nowChapterIndex;

    // 章节集合
    public List<Chapter> chapters = new ArrayList<>();

    // 章节内容
    public String textContent;

    public String importPath;

    public String bookType;

    public ReadingProgressPersistent() {
    }

    public ReadingProgressPersistent(String searchType, int nowChapterIndex, List<Chapter> chapters, String textContent, String importPath, String bookType) {
        this.searchType = searchType;
        this.nowChapterIndex = nowChapterIndex;
        this.chapters = chapters;
        this.textContent = textContent;
        this.importPath = importPath;
        this.bookType = bookType;
    }

    @Override
    @Nullable
    public ReadingProgressPersistent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ReadingProgressPersistent s) {
        XmlSerializerUtil.copyBean(s, this);
    }

    public static ReadingProgressPersistent getInstance() {
        ApplicationManager.getApplication().getService(ReadingProgressPersistent.class);
        return ApplicationManager.getApplication().getService(ReadingProgressPersistent.class);
    }
}

