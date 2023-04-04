package com.liao.book.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
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
     * 字体大小下拉框
     */
    private JComboBox fontSize;

    /**
     * 阅读滚动间距下拉框
     */
    private JComboBox readRoll;

    private JProgressBar progressBar1;
    private JProgressBar progressBar2;

    /**
     * 书籍导入组件
     */
    private TextFieldWithBrowseButton selectFile;

    public SettingsUI() {

    }

    public JPanel getSettingWin() {
        return settingWin;
    }

    /**
     * 组件初始化函数
     */
    private void init() {
        // 加载组件事件
        bindingComponentEvent();
    }


    /**
     * 绑定组件默认数据
     */
    private void bindingComponentData() {
    }

    /**
     * 绑定组件相关事件
     */
    private void bindingComponentEvent() {
        // 文件选择事件
        selectFile.addBrowseFolderListener("选择书籍文件", null, null, new FileChooserDescriptor(true, false, false, false, false, false) {
            @Override
            public void validateSelectedFiles(VirtualFile @NotNull [] files) throws Exception {
                System.out.println(files);
            }
        });
    }

}
