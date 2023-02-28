package com.liao.book.factory;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.liao.book.entity.DataCenter;
import com.liao.book.window.BookMainWindow;
import org.jetbrains.annotations.NotNull;

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
        BookMainWindow noteListWindow = new BookMainWindow(project, toolWindow);
        // 获取内容工厂实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取用于toolWindows显示的内容
        Content content = contentFactory.createContent(noteListWindow.getBookMainJPanel(),
                DataCenter.TAB_CONTROL_TITLE_HOME, false);

        // 给toolWindows设置内容
        toolWindow.getContentManager().addContent(content);

        // 加载沉浸阅读页面
        new FullScreenReadingFaction().createToolWindowContent(project, toolWindow);
    }

}
