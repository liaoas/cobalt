package com.cobalt.parser.content;

import com.cobalt.common.constant.UIConstants;
import com.cobalt.common.enums.ToastType;
import com.cobalt.common.utils.ModuleUtils;
import com.cobalt.common.utils.ToastUtils;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.proxy.ReadingProgressProxy;
import com.cobalt.parser.chapter.Chapter;
import com.cobalt.parser.chapter.ChapterParserFacade;
import com.cobalt.parser.chapter.ChapterWorker;
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
public final class ContentWorker extends SwingWorker<Void, Void> {

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
    private final ReadingProgressProxy readingProgress;
    // 章节爬虫
    static ChapterParserFacade chapterParser = (ChapterParserFacade) BeanFactory.getBean("ChapterParserFacade");


    public ContentWorker(String valueAt, JComboBox<String> chapterList, Project project,
                         JEditorPane textContent, JPanel mainPanel) {
        this.valueAt = valueAt;
        this.chapterList = chapterList;
        this.project = project;
        this.textContent = textContent;
        this.mainPanel = mainPanel;
        this.readingProgress = new ReadingProgressProxy();
    }

    @Override
    protected Void doInBackground() {
        if (!chapterParser.initChapter(valueAt)) {
            ToastUtils.showToastMassage(project, "章节内容解析失败", ToastType.ERROR);
            return null;
        }
        return null;
    }

    @Override
    protected void done() {
        // 清空章节信息
        readingProgress.setNowChapterIndex(0);
        // 清空下拉列表
        chapterList.removeAllItems();
        // 加载下拉列表
        for (Chapter chapter : readingProgress.getChapters()) {
            chapterList.addItem(chapter.getName());
        }
        // 解析当前章节内容
        new ChapterWorker(project, textContent, chapterList, mainPanel).execute();
        // 书本已切换
        MainUI.isReadClick = true;

        if (!readingProgress.getSearchType().equals(UIConstants.IMPORT)) {
            // 本地导入书籍清空
            readingProgress.setImportPath(null);
        }
        // 恢复默认鼠标样式
        ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
    }
}
