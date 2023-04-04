package com.liao.book.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.liao.book.common.Constants;
import com.liao.book.common.ModuleConstants;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 插件设置页面窗口
 *
 * @author LiAo
 * @since 2023-04-03
 */
public class SettingsUI {

    /**
     * 组件窗口
     */
    private JPanel settingWin;

    /**
     * 阅读标题分割线
     */
    private JProgressBar readProgressBar;

    /**
     * 字体大小下拉框
     */
    private JComboBox<Integer> fontSize;

    /**
     * 阅读滚动间距下拉框
     */
    private JComboBox<Integer> readRoll;

    /**
     * 导入标题分割线
     */
    private JProgressBar importProgressBar;

    /**
     * 书籍导入组件
     */
    private TextFieldWithBrowseButton selectFile;

    /**
     * 章节正则
     */
    private JTextField chapterRegular;

    /**
     * 章节正则override
     */
    private JCheckBox overrideCheckBox;

    /**
     * 页面是否修改
     */
    public boolean isModified = false;

    public SettingsUI() {
        init();
    }

    public JPanel getSettingWin() {
        return settingWin;
    }

    public boolean isModified() {
        return isModified;
    }

    /**
     * 组件初始化函数
     */
    private void init() {
        // 绑定数据
        bindingComponentData();
        // 加载组件事件
        bindingComponentEvent();
    }


    /**
     * 绑定组件默认数据
     */
    private void bindingComponentData() {
        // 加载字体大小，滚轮间距下拉框
        for (int size : ModuleConstants.SIZE_ARRAY) {
            fontSize.addItem(size);
            readRoll.addItem(size);
        }

        // 默认字体大小
        fontSize.setSelectedItem(16);

        // 默认正则值
        chapterRegular.setText(Constants.DEFAULT_CHAPTER_REGULAR);

    }

    /**
     * 绑定组件相关事件
     */
    private void bindingComponentEvent() {
        // 书籍选择
        selectFile.addBrowseFolderListener("选择书籍文件", null, null, new FileChooserDescriptor(true, false, false, false, false, false) {
            @Override
            public void validateSelectedFiles(VirtualFile @NotNull [] files) {
                if (files.length != 0) {
                    isModified = true;
                }
            }
        });

        // 正则 Override 复选框单击事件
        overrideCheckBox.addActionListener(e -> {
            if (overrideCheckBox.isSelected()) {
                chapterRegular.setEnabled(true);
                chapterRegular.setText("");
            } else {
                chapterRegular.setEnabled(false);
                chapterRegular.setText(Constants.DEFAULT_CHAPTER_REGULAR);
            }

            isModified = true;
        });
    }

}
