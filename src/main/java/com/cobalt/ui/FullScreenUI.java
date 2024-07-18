package com.cobalt.ui;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.common.enums.ToastType;
import com.cobalt.entity.Chapter;
import com.cobalt.entity.ImportBookData;
import com.cobalt.common.utils.ModuleUtils;
import com.cobalt.common.utils.ReadingUtils;
import com.cobalt.common.utils.ToastUtils;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SettingsPersistent;
import com.cobalt.work.OpenChapterWord;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * 全屏
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */

public class FullScreenUI {

    // 主体窗口
    private JPanel fullScreenPanel;
    // 文章内容滑动
    private JScrollPane paneTextContent;
    // 书籍内容
    private JEditorPane textContent;
    // 上一章按钮
    private JButton btnOn;
    // 下一章按钮
    private JButton underOn;
    // 章节列表
    private JComboBox<String> chapterList;
    // 跳转到指定章节
    private JButton jumpButton;
    // 全局模块对象
    private final Project project;

    // 用于判断是否是当前选项卡切换
    private Content lastSelectedContent = null;

    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();
    // 页面设置持久化
    static SettingsPersistent settingDao = SettingsPersistent.getInstance();
    // 窗口信息
    public JPanel getFullScreenPanel() {
        return fullScreenPanel;
    }


    // 初始化数据
    private void init() {
        paneTextContent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // 加载组件配置信息
        ModuleUtils.loadModuleConfig(paneTextContent);
        // 加载提示信息
        ModuleUtils.loadComponentTooltip(null, null, null, btnOn, underOn, jumpButton);
        // 加载阅读进度
        ReadingUtils.loadReadingProgress(chapterList, textContent);
        // 加载持久化的设置
        ModuleUtils.loadSetting(paneTextContent, textContent, null);
    }

    // 页面打开方法
    public FullScreenUI(Project project, ToolWindow toolWindow) {
        this.project = project;
        // 初始化信息
        init();
        // 上一章节跳转
        btnOn.addActionListener(e -> {
            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(fullScreenPanel, Cursor.WAIT_CURSOR);
            if (instance.chapters.isEmpty() || instance.nowChapterIndex == 0) {
                ToastUtils.showToastMassage(project, "已经是第一章了", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(fullScreenPanel, Cursor.DEFAULT_CURSOR);
                return;
            }
            instance.nowChapterIndex = instance.nowChapterIndex - 1;
            // 加载阅读信息
            new OpenChapterWord(project, textContent, chapterList, fullScreenPanel).execute();
        });

        // 下一章跳转
        underOn.addActionListener(e -> {
            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(fullScreenPanel, Cursor.WAIT_CURSOR);
            if (instance.chapters.isEmpty() || instance.nowChapterIndex == instance.chapters.size() - 1) {
                ToastUtils.showToastMassage(project, "已经是最后一章了", ToastType.ERROR);
                return;
            }
            instance.nowChapterIndex = instance.nowChapterIndex + 1;
            // 加载阅读信息
            new OpenChapterWord(project, textContent, chapterList, fullScreenPanel).execute();
        });

        // 章节跳转
        jumpButton.addActionListener(e -> {
            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(fullScreenPanel, Cursor.WAIT_CURSOR);
            if (instance.chapters.isEmpty() || instance.nowChapterIndex < 0) {
                ToastUtils.showToastMassage(project, "未知章节", ToastType.ERROR);
                return;
            }
            // 根据下标跳转
            instance.nowChapterIndex = chapterList.getSelectedIndex();
            // 加载阅读信息
            new OpenChapterWord(project, textContent, chapterList, fullScreenPanel).execute();
        });

        // 窗口加载结束
        ApplicationManager.getApplication().invokeLater(() -> {
            // 窗口未初始化
            if (project.isDisposed() || toolWindow == null) return;
            final ContentManager contentManager = toolWindow.getContentManager();
            // 监听当前选中的面板 进行阅读进度同步
            contentManager.addContentManagerListener(new ContentManagerListener() {
                @Override
                public void selectionChanged(@NotNull ContentManagerEvent event) {
                    Content selectedContent = event.getContent();

                    if (instance.chapters.isEmpty() || selectedContent == lastSelectedContent) return;
                    // 加载持久化的设置
                    ModuleUtils.loadSetting(paneTextContent, textContent, null);
                    // 只有选择的内容面板发生变化时才进行相关操作
                    lastSelectedContent = selectedContent;
                    if (selectedContent.getDisplayName().equals(ModuleConstants.TAB_CONTROL_TITLE_UNFOLD)) {
                        // 等待鼠标样式
                            ModuleUtils.loadTheMouseStyle(fullScreenPanel, Cursor.WAIT_CURSOR);
                        // 切换了书本
                        if (MainUI.isReadClick) {
                            MainUI.isReadClick = false;
                            startReading();
                        } else {
                            // 页面回显
                            if (!instance.searchType.equals(ModuleConstants.IMPORT) && !instance.bookType.equals(Constants.EPUB_STR_LOWERCASE)) {
                                // 获取新的章节位置
                                Chapter chapter = instance.chapters.get(instance.nowChapterIndex);
                                // 章节内容赋值
                                String htmlContent = ModuleUtils.fontSizeFromHtml(settingDao.fontSize, instance.textContent);
                                textContent.setText(htmlContent);
                                // 设置下拉框的值
                                chapterList.setSelectedItem(chapter.getName());
                                // 回到顶部
                                textContent.setCaretPosition(1);
                            }
                        }
                        if (instance.searchType.equals(ModuleConstants.IMPORT) && instance.bookType.equals(Constants.EPUB_STR_LOWERCASE)) {
                            ImportBookData bookData = ImportBookData.getInstance();
                            bookData.setTextContent(textContent);
                            textContent.setDocument(bookData.getBookHTMLDocument());
                        }
                        ModuleUtils.loadTheMouseStyle(fullScreenPanel, Cursor.DEFAULT_CURSOR);
                    }
                }
            });
            toolWindow.installWatcher(contentManager);
        });
    }

    // 开始阅读
    public void startReading() {
        // 清空下拉列表
        chapterList.removeAllItems();
        // 加载下拉列表
        for (Chapter chapter1 : instance.chapters) {
            chapterList.addItem(chapter1.getName());
        }
        // 加载阅读信息
        new OpenChapterWord(project, textContent, chapterList, fullScreenPanel).execute();
    }

    /**
     * 应用滚动速度滑块
     */
    private void applyScrollSpacing() {
        SettingsUI settingsUI = (SettingsUI) BeanFactory.getBean("SettingsUI");
        paneTextContent.getVerticalScrollBar().setUnitIncrement(settingsUI.getReadRollVal());
        // 持久化
        settingDao.scrollSpacingScale = settingsUI.getReadRollVal();
        settingDao.loadState(settingDao);
    }

    /**
     * 页面统一的Apply
     */
    public void apply() {
        // 字体大小
        ModuleUtils.applyFontSize(textContent);
        // 滑块滚动
        applyScrollSpacing();
    }
}
