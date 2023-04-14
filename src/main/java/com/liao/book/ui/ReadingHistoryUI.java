package com.liao.book.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

/**
 * <p>
 * 阅读历史记录
 * </p>
 *
 * @author LiAo
 * @since 2023-04-14
 */
public class ReadingHistoryUI {

    // 全局模块对象
    private final Project project;

    private JPanel panel1;

    // 窗口信息
    public JPanel getBookMainJPanel() {
        return panel1;
    }

    // 页面打开方法
    public ReadingHistoryUI(Project project, ToolWindow toolWindow) {
        this.project = project;
    }
}
