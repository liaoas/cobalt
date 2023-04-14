package com.liao.book.factory;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.liao.book.content.FullScreenUIContent;
import com.liao.book.content.MainUIContent;
import com.liao.book.content.ReadingHistoryUIContent;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * 视图工厂，用于加载窗口信息
 * </p>
 *
 * @author LiAo
 * @since 2023-04-14
 */
public class ViewFaction implements ToolWindowFactory, DumbAware {

    /**
     * 创建窗口容器
     *
     * @param project    项目
     * @param toolWindow 窗口
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        new MainUIContent().create(project, toolWindow);

        new FullScreenUIContent().create(project, toolWindow);

        new ReadingHistoryUIContent().create(project, toolWindow);

    }
}
