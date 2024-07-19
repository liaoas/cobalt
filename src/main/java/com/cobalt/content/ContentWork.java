package com.cobalt.content;

import com.cobalt.chapter.ChapterParser;
import com.cobalt.chapter.ChapterWord;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.chapter.Chapter;
import com.cobalt.common.utils.ModuleUtils;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.ui.MainUI;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * 打开书籍异步工作任务
 * </p>
 *
 * @author LiAo
 * @since 2024-03-27
 */
public final class ContentWork extends SwingWorker<Void, Void> {

    // 书籍链接
    private final String valueAt;
    // 章节目录下拉列表
    private final JComboBox<String> chapterList;
    // 全局模块对象
    public final Project project;
    // 章节内容
    private final JEditorPane textContent;
    // 窗口
    private final JPanel mainPanel;
    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();
    // 章节爬虫
    static ChapterParser chapterParser = (ChapterParser) BeanFactory.getBean("ChapterParser");


    public ContentWork(String valueAt, JComboBox<String> chapterList, Project project, JEditorPane textContent, JPanel mainPanel) {
        this.valueAt = valueAt;
        this.chapterList = chapterList;
        this.project = project;
        this.textContent = textContent;
        this.mainPanel = mainPanel;
    }

    @Override
    protected Void doInBackground() {
        chapterParser.getBookChapterByType(valueAt);
        return null;
    }

    @Override
    protected void done() {
        // 清空章节信息
        instance.nowChapterIndex = 0;
        // 清空下拉列表
        chapterList.removeAllItems();
        // 加载下拉列表
        for (Chapter chapter : instance.chapters) {
            chapterList.addItem(chapter.getName());
        }
        // 解析当前章节内容
        new ChapterWord(project, textContent, chapterList, mainPanel).execute();
        // 书本已切换
        MainUI.isReadClick = true;

        if (!instance.searchType.equals(ModuleConstants.IMPORT)) {
            // 本地导入书籍清空
            instance.importPath = null;
        }
        // 恢复默认鼠标样式
        ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
    }
}
