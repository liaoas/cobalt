package com.cobalt.ui;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.UIConstants;
import com.cobalt.framework.factory.ViewFaction;
import com.cobalt.framework.persistence.proxy.ReadingProgressProxy;
import com.cobalt.framework.persistence.proxy.SettingsParameterProxy;
import com.cobalt.framework.persistence.proxy.SpiderActionProxy;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.fields.ExpandableTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;

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
     * 爬虫规则拉取按钮
     */
    private JButton reptilePullBut;
    /**
     * 爬虫规则输入框
     */
    private ExpandableTextField reptileConfigInput;
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
    /**
     * 导入书籍的路径
     */
    private String importBookPath = null;

    // 页面设置持久化
    private final SettingsParameterProxy settingsParameter;
    private final ReadingProgressProxy readingProgress;
    private final SpiderActionProxy spiderAction;

    public SettingsUI() {
        this.readingProgress = new ReadingProgressProxy();
        this.settingsParameter = new SettingsParameterProxy();
        this.spiderAction = new SpiderActionProxy();
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

    public String getImportBookPath() {
        return importBookPath;
    }

    public void setImportBookPath(String importBookPath) {
        this.importBookPath = importBookPath;
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
    }


    /**
     * 绑定组件默认数据
     */
    private void bindingComponentData() {
        // 加载字体大小，滚轮间距下拉框
        for (int size : UIConstants.SIZE_ARRAY) {
            fontSize.addItem(size);
            readRoll.addItem(size);
        }
        // 默认字体大小
        fontSize.setSelectedItem(16);
        // 默认正则值
        chapterRegular.setText("下次版本更新，目前只可导入书籍");
    }

    /**
     * 绑定页面上的组件监听事件
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

                importBookPath = file.getPath();

                isSelBook = true;
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

        // 规则拉去按钮
        reptilePullBut.addActionListener(e -> {
            String config = ViewFaction.loadGitHubConfig();
            reptileConfigInput.setText(config);
            // 通知 apple 按钮
            informApplyState();
        });

        // 爬虫规则输入框监听
        reptileConfigInput.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                // 通知 apple 按钮
                informApplyState();
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {
                // 通知 apple 按钮
                informApplyState();
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
    public void loadPersistentState() {
        fontSize.setSelectedItem(settingsParameter.getFontSize());
        readRoll.setSelectedItem(settingsParameter.getScrollSpacingScale());
        selectFile.setText(readingProgress.getImportPath());
        reptileConfigInput.setText(spiderAction.getSpiderActionStr());
    }

    public void apply() {
        // 保存规则，若为空则跳过
        String reptileConfig = reptileConfigInput.getText();

        if (reptileConfig != null && !reptileConfig.isEmpty()) {
            spiderAction.setSpiderActionStr(reptileConfig);
        }
    }

}
