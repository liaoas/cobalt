package com.cobalt.chapter;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.enums.ToastType;
import com.cobalt.common.utils.ModuleUtils;
import com.cobalt.common.utils.ToastUtils;
import com.cobalt.content.ContentParserFacade;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SettingsPersistent;
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
public final class ChapterWorker extends SwingWorker<Void, Chapter> {

    // 全局模块对象
    public final Project project;
    // 章节内容
    private final JEditorPane textContent;
    // 章节目录下拉列表
    private final JComboBox<String> chapterList;
    // 窗口
    private final JPanel mainPanel;

    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();
    // 页面设置持久化
    static SettingsPersistent settingDao = SettingsPersistent.getInstance();
    // 内容爬虫
    static ContentParserFacade contentParser = (ContentParserFacade) BeanFactory.getBean("ContentParserFacade");

    public ChapterWorker(Project project, JEditorPane textContent, JComboBox<String> chapterList, JPanel mainPanel) {
        this.project = project;
        this.textContent = textContent;
        this.chapterList = chapterList;
        this.mainPanel = mainPanel;
    }

    @Override
    protected Void doInBackground() {
        // 清空书本表格
        Chapter chapter = instance.chapters.get(instance.nowChapterIndex);
        if (!contentParser.initContent(chapter.getLink())) {
            ToastUtils.showToastMassage(project, "章节获取失败", ToastType.ERROR);
            return null;
        }
        //将当前进度信息加入chunks中
        publish(chapter);
        return null;
    }

    @Override
    protected void process(List<Chapter> chapters) {
        Chapter chapter = chapters.get(0);
        if (!instance.bookType.equals(Constants.EPUB_STR_LOWERCASE)) {
            String htmlContent = ModuleUtils.fontSizeFromHtml(settingDao.fontSize, instance.textContent);
            textContent.setText(htmlContent);
            // 回到顶部
            textContent.setCaretPosition(1);
        }
        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());

    }

    @Override
    protected void done() {
        // 恢复默认鼠标样式
        ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
    }
}
