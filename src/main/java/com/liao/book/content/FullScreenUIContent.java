package com.liao.book.content;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.liao.book.common.ModuleConstants;
import com.liao.book.factory.BeanFactory;
import com.liao.book.ui.FullScreenUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <p>
 * 全屏阅读视图工厂
 * </p>
 *
 * @author LiAo
 * @since 2021/5/19
 */
public class FullScreenUIContent {

    /**
     * 创建全屏视图容器
     *
     * @param project    项目对象
     * @param toolWindow 窗口对象
     */
    public void create(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建NoteListWindow对象
        FullScreenUI fullScreenUI = new FullScreenUI(project, toolWindow);
        // 加入容器
        BeanFactory.setBean("FullScreenUI", fullScreenUI);
        // 获取内容工厂实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取用于toolWindows显示的内容
        Content content = contentFactory.createContent(fullScreenUI.getBookMainJPanel(), ModuleConstants.TAB_CONTROL_TITLE_UNFOLD, true);
        Icon icon = IconLoader.getIcon("/img/full_screen.png", FullScreenUIContent.class);
        content.setIcon(icon);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
        // 给toolWindows设置内容
        toolWindow.getContentManager().addContent(content);
    }
}
