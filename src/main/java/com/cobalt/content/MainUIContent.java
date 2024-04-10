package com.cobalt.content;

import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.ui.MainUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <p>
 * 首页容器
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class MainUIContent {

    /**
     * 创建视图容器
     *
     * @param project    项目对象
     * @param toolWindow 窗口对象
     */
    public void createContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        // 创建NoteListWindow对象
        MainUI mainUI = new MainUI(project, toolWindow);
        // 加入容器
        BeanFactory.setBean("MainUI", mainUI);
        // 获取内容工厂实例
        // ideaIC 211.1 - 221.1
        /*ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();*/
        // ideaIC 222.1 - 223.2 JDK 17
        ContentFactory contentFactory = ContentFactory.getInstance();
        // 获取用于toolWindows显示的内容
        Content content = contentFactory.createContent(mainUI.getMainPanel(), ModuleConstants.TAB_CONTROL_TITLE_HOME, false);
        Icon icon = IconLoader.getIcon("/img/home.svg", MainUIContent.class);
        content.setIcon(icon);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);

        // 给toolWindows设置内容
        toolWindow.getContentManager().addContent(content);
    }

}
