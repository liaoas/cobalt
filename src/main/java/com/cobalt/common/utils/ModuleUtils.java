package com.cobalt.common.utils;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.ReadSubscriptPersistent;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SettingsPersistent;
import com.cobalt.ui.SettingsUI;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * 组件相关工具类
 * </p>
 *
 * @author LiAo
 * @since 2023-03-01
 */
public class ModuleUtils {

    // 页面设置持久化
    static SettingsPersistent settingDao = SettingsPersistent.getInstance();

    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    // 阅读窗口滚动位置持久化
    static ReadSubscriptPersistent readSubscriptDao = ReadSubscriptPersistent.getInstance();

    /**
     * 加载页面鼠标样式
     *
     * @param bookMainJPanel 页面对象
     * @param type           鼠标样式 {@link Cursor}
     */
    public static void loadTheMouseStyle(JPanel bookMainJPanel, int type) {
        Cursor cursor = new Cursor(type);
        bookMainJPanel.setCursor(cursor);
    }

    /**
     * 加载持久化的设置:字体大小,滚动间距
     *
     * @param paneTextContent 章节内容外部框
     * @param textContent     章节内容
     */
    public static void loadSetting(JScrollPane paneTextContent, JEditorPane textContent, JSplitPane bookTabContentSplit) {
        // 同步滚动步长
        paneTextContent.getVerticalScrollBar().setUnitIncrement(settingDao.scrollSpacingScale);
        // 字体大小
        textContent.setFont(new Font("", Font.BOLD, settingDao.fontSize));
        if (bookTabContentSplit == null) {
            return;
        }
        bookTabContentSplit.setDividerLocation(settingDao.splitPosition);
    }

    /**
     * 加载组件配置:
     * 设置滑块刻度间距
     * 显示标签
     * 页面滚动步长
     *
     * @param paneTextContent 章节内容外部框
     */
    public static void loadModuleConfig(JScrollPane paneTextContent) {
        // 页面滚动步长
        JScrollBar jScrollBar = new JScrollBar();
        // 滚动步长为8
        jScrollBar.setMaximum(8);
        paneTextContent.setVerticalScrollBar(jScrollBar);
    }

    /**
     * 初始化页面组件提示信息
     *
     * @param btnSearch  搜索按钮
     * @param openBook   阅读按钮
     * @param settingBtn 设置按钮
     * @param btnOn      上一章
     * @param underOn    下一章
     * @param jumpButton 跳转
     */
    public static void loadComponentTooltip(JButton btnSearch, JButton openBook, JButton settingBtn, JButton btnOn, JButton underOn, JButton jumpButton) {
        // 搜索按钮
        if (btnSearch != null) {
            btnSearch.setToolTipText(ModuleConstants.SEARCH_BTN);
        }
        // 阅读按钮
        if (openBook != null) {
            openBook.setToolTipText(ModuleConstants.START_READ);
        }

        // 设置按钮
        if (settingBtn != null) {
            settingBtn.setToolTipText(ModuleConstants.SETTINGS);
        }
        // 上一章
        btnOn.setToolTipText(ModuleConstants.BTN_ON);
        // 下一章
        underOn.setToolTipText(ModuleConstants.UNDER_ON);
        // 跳转
        jumpButton.setToolTipText(ModuleConstants.JUMP_BUTTON);
    }


    /**
     * 将文本内容格式化为 html 格式 用于 JEditorPane contentType 类型为 text/html 时文本大小的切换
     *
     * @param fontSize 字体大小
     * @param content  内容
     * @return 格式化后 html 的大小
     */
    public static String fontSizeFromHtml(int fontSize, String content) {
        content = "<html><body style='font-size:" + fontSize + "px'>" + content + "</body></html>";
        return content;
    }

    /**
     * 应用字体大小的修改
     */
    public static void applyFontSize(JEditorPane textContent) {
        SettingsUI settingsUI = (SettingsUI) BeanFactory.getBean("SettingsUI");

        if (!instance.bookType.equals(Constants.EPUB_STR_LOWERCASE)) {
            // 章节内容赋值
            String htmlContent = ModuleUtils.fontSizeFromHtml(settingDao.fontSize, instance.textContent);
            textContent.setText(htmlContent);
        }

        // 还原滚动位置
        // textContent.setCaretPosition(readSubscriptDao.homeTextWinIndex);
        // 持久化
        settingDao.fontSize = settingsUI.getFontSizeVal();
        settingDao.loadState(settingDao);
    }
}
