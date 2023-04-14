package com.liao.book.content;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.liao.book.common.ModuleConstants;
import com.liao.book.factory.BeanFactory;
import com.liao.book.ui.ReadingHistoryUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <p>
 * 阅读历史视图容器
 * </p>
 *
 * @author LiAo
 * @since 2021/5/19
 */
public class ReadingHistoryUIContent {

    /**
     * 创建视图容器
     *
     * @param project    项目对象
     * @param toolWindow 窗口对象
     */
    public void create(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建NoteListWindow对象
        ReadingHistoryUI readingHistoryUI = new ReadingHistoryUI(project, toolWindow);
        // 加入容器
        BeanFactory.setBean("ReadingHistoryUI", readingHistoryUI);
        // 获取内容工厂实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取用于toolWindows显示的内容
        Content content = contentFactory.createContent(readingHistoryUI.getBookMainJPanel(), ModuleConstants.TAB_CONTROL_READING_HISTORY, true);
        Icon icon = IconLoader.getIcon("/img/time.svg", ReadingHistoryUIContent.class);
        content.setIcon(icon);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
        // 给toolWindows设置内容
        toolWindow.getContentManager().addContent(content);
    }
}
