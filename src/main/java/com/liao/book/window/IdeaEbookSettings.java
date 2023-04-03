package com.liao.book.window;

import javax.swing.*;

/**
 * 插件设置页面窗口
 *
 * @author LiAo
 * @since 2023-04-03
 */
public class IdeaEbookSettings {
    private JPanel panel1;

    private JProgressBar progressBar1;

    /**
     * 字体大小下拉框
     */
    private JComboBox fontSize;

    /**
     * 阅读滚动间距下拉框
     */
    private JComboBox readRoll;

    public JPanel getPanel1() {
        return panel1;
    }
}
