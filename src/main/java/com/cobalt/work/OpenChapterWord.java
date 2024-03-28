package com.cobalt.work;

import com.cobalt.entity.Chapter;
import com.cobalt.enums.ToastType;
import com.cobalt.factory.BeanFactory;
import com.cobalt.persistence.ReadingProgressDao;
import com.cobalt.service.ContentService;
import com.cobalt.service.impl.ContentServiceImpl;
import com.cobalt.utils.ModuleUtils;
import com.cobalt.utils.ToastUtils;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * <p>
 * 打开章节异步工作队列
 * </p>
 *
 * @author LiAo
 * @since 2024-03-27
 */
public final class OpenChapterWord extends SwingWorker<Void, Chapter> {
    
    // 全局模块对象
    public final Project project;

    // 章节内容
    private final JEditorPane textContent;

    // 章节目录下拉列表
    private final JComboBox<String> chapterList;

    // 窗口
    private final JPanel mainPanel;


    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    // 内容爬虫
    static ContentService textService = (ContentServiceImpl) BeanFactory.getBean("ContentServiceImpl");

    public OpenChapterWord(Project project, JEditorPane textContent, JComboBox<String> chapterList, JPanel mainPanel) {
        this.project = project;
        this.textContent = textContent;
        this.chapterList = chapterList;
        this.mainPanel = mainPanel;
    }

    @Override
    protected Void doInBackground() {
        // 清空书本表格
        Chapter chapter = instance.chapters.get(instance.nowChapterIndex);

        // 重置重试次数
        ContentServiceImpl.index = 2;

        // 内容
        textService.searchBookChapterData(chapter.getLink());

        if (instance.textContent == null) {
            ToastUtils.showToastMassage(project, "章节内容为空", ToastType.ERROR);
            return null;
        }

        //将当前进度信息加入chunks中
        publish(chapter);
        return null;
    }

    @Override
    protected void process(List<Chapter> chapters) {
        Chapter chapter = chapters.get(0);
        // 章节内容赋值
        textContent.setText(instance.textContent);
        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());
        // 回到顶部
        textContent.setCaretPosition(1);
    }

    @Override
    protected void done() {
        // 恢复默认鼠标样式
        ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
    }
}
