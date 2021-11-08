package com.liao.book.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.liao.book.window.FullScreenReading;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * 沉浸阅读视图工厂
 * </p>
 *
 * @author LiAo
 * @since 2021/5/19
 */
public class FullScreenReadingFaction implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建NoteListWindow对象
        FullScreenReading noteListWindow = new FullScreenReading(project, toolWindow);
        // 获取内容工厂实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取用于toolWindows显示的内容
        Content content = contentFactory.createContent(noteListWindow.getBookMainJPanel(), "\uD83D\uDD32", false);

        // 给toolWindows设置内容
        toolWindow.getContentManager().addContent(content);

    }
}
