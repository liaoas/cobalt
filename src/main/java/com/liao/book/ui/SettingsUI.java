package com.liao.book.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.liao.book.common.Constants;
import com.liao.book.common.ModuleConstants;
import com.liao.book.dao.SettingsDao;
import com.liao.book.factory.BeanFactory;
import com.liao.book.service.impl.ImportServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 插件设置页面窗口
 *
 * @author LiAo
 * @since 2023-04-03
 */
@SuppressWarnings("all")
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

    /**
     * 是否导入书籍
     */
    public boolean isSelBook = false;

    /**
     * 字体大小值
     */
    private int fontSizeVal = Constants.DEFAULT_FONT_SIZE;

    /**
     * 滚轮速度大小值
     */
    private int readRollVal = Constants.DEFAULT_READ_ROLL_SIZE;

    // 页面设置持久化
    private static SettingsDao settingDao = SettingsDao.getInstance();

    // 书籍导入处理类
    private static ImportServiceImpl importService = (ImportServiceImpl) BeanFactory.getBean("ImportServiceImpl");


    public SettingsUI() {
        init();
    }

    public JPanel getSettingWin() {
        return settingWin;
    }

    public boolean isModified() {
        return isModified;
    }

    public boolean isSelBook() {
        return isSelBook;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public int getFontSizeVal() {
        return fontSizeVal;
    }

    public void setFontSizeVal(int fontSizeVal) {
        this.fontSizeVal = fontSizeVal;
    }

    public int getReadRollVal() {
        return readRollVal;
    }

    public void setReadRollVal(int readRollVal) {
        this.readRollVal = readRollVal;
    }

    /**
     * 组件初始化函数
     */
    private void init() {
        // 绑定数据
        bindingComponentData();
        // 加载组件事件
        bindingComponentEvent();
        // 加载持久化状态
        loadPersistentState();

        // 创建一个只允许选择 .txt 文件的 FileChooserDescriptor
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();

        // 创建 TextFieldWithBrowseButton 组件，并将 FileChooserDescriptor 传递给它
        selectFile.addBrowseFolderListener(null, null, null, descriptor);
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
     * 绑定组件响应事件
     */
    private void bindingComponentEvent() {
        // 书籍选择
        selectFile.addBrowseFolderListener("选择书籍文件", null, null, new FileChooserDescriptor(true, false, false, false, false, false) {
            @Override
            public void validateSelectedFiles(VirtualFile @NotNull [] files) {
                if (files.length == 0) {
                    return;
                }

                VirtualFile file = files[0];

                // 解析并导入
                if (importService.importBook(file)) {
                    // 导入成功
                    isSelBook = true;
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

        // 字体大小下拉框数值发生变化
        fontSize.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedItem = String.valueOf(fontSize.getSelectedItem());
                fontSizeVal = selectedItem == null ? Constants.DEFAULT_FONT_SIZE : Integer.parseInt(selectedItem);
                // 通知 apple 按钮
                informApplyState();
            }
        });

        // 滚轮速度下拉框数值发生变化
        readRoll.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedItem = String.valueOf(readRoll.getSelectedItem());
                readRollVal = selectedItem == null ? Constants.DEFAULT_READ_ROLL_SIZE : Integer.parseInt(selectedItem);
                // 通知 apple 按钮
                informApplyState();
            }
        });

        // 监听字体大小下拉框键盘输入选项
        fontSize.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField editor = (JTextField) fontSize.getEditor().getEditorComponent();
                if (Constants.INTERGER_PATTERN.matcher(editor.getText()).matches()) {
                    fontSizeVal = Integer.parseInt(editor.getText());
                    // 通知 apple 按钮
                    informApplyState();
                }
            }
        });

        // 监听滚轮速度下拉框键盘输入选项
        readRoll.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField editor = (JTextField) readRoll.getEditor().getEditorComponent();
                if (Constants.INTERGER_PATTERN.matcher(editor.getText()).matches()) {
                    readRollVal = Integer.parseInt(editor.getText());
                    // 通知 apple 按钮
                    informApplyState();
                }
            }
        });
    }

    /**
     * 获取字体大小选中值
     *
     * @return 字体大小
     */
    public int getFontSizeSelectedItem() {
        String selectedItem = String.valueOf(fontSize.getSelectedItem());
        return selectedItem == null ? Constants.DEFAULT_FONT_SIZE : Integer.parseInt(selectedItem);
    }

    /**
     * 获取滚动速度选中值
     *
     * @return 滚动速度
     */
    public int getReadRollSelectedItem() {
        String selectedItem = String.valueOf(readRoll.getSelectedItem());
        return selectedItem == null ? Constants.DEFAULT_READ_ROLL_SIZE : Integer.parseInt(selectedItem);
    }

    /**
     * 通知 Apple 状态
     */
    private void informApplyState() {
        // 通知 apple 按钮
        isModified = true;
    }

    /**
     * 加载持久化状态
     */
    private void loadPersistentState() {
        fontSize.setSelectedItem(settingDao.fontSize);
        readRoll.setSelectedItem(settingDao.scrollSpacingScale);
    }

}
