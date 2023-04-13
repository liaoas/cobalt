package com.liao.book.factory;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.impl.ToolWindowImpl;
import com.intellij.ui.content.AlertIcon;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.impl.ContentImpl;
import com.intellij.util.IconUtil;
import com.liao.book.common.ModuleConstants;
import com.liao.book.ui.MainUI;
import com.liao.book.ui.SettingsUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <p>
 * 首页视图工厂
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class BookMainWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // 创建NoteListWindow对象
        MainUI mainUI = new MainUI(project, toolWindow);
        // 加入容器
        BeanFactory.setBean("MainUI", mainUI);
        // 获取内容工厂实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取用于toolWindows显示的内容
        Content content = contentFactory.createContent(mainUI.getBookMainJPanel(), ModuleConstants.TAB_CONTROL_TITLE_HOME, false);
        Icon icon = IconLoader.getIcon("/img/home.png");
        content.setIcon(icon);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);

        // 给toolWindows设置内容
        toolWindow.getContentManager().addContent(content);

        // 加载沉浸阅读页面
        new FullScreenReadingFaction().createToolWindowContent(project, toolWindow);
    }

}
