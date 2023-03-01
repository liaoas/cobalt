package com.liao.book.utils;

import com.liao.book.dao.WindowsSettingDao;
import com.liao.book.entity.DataCenter;

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
    static WindowsSettingDao settingDao = WindowsSettingDao.getInstance();

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
     * @param scrollSpacing   滚动间距
     */
    public static void loadSetting(JScrollPane paneTextContent, JTextArea textContent, JSlider scrollSpacing) {
        // 同步滚动步长
        if (settingDao.scrollSpacingScale != paneTextContent.getVerticalScrollBar().getUnitIncrement()) {
            paneTextContent.getVerticalScrollBar().setUnitIncrement(Math.max(settingDao.scrollSpacingScale, 8));
        }
        // 字体大小
        if (settingDao.fontSize != textContent.getFont().getSize()) {
            textContent.setFont(new Font("", Font.BOLD, Math.max(settingDao.fontSize, 16)));
        }
        // 滑块刻度值
        if (settingDao.scrollSpacingScale != scrollSpacing.getValue()) {
            scrollSpacing.setValue(Math.max(settingDao.scrollSpacingScale, 8));
        }
    }

    /**
     * 加载组件配置:
     * 设置滑块刻度间距
     * 显示标签
     * 页面滚动步长
     *
     * @param paneTextContent 章节内容外部框
     * @param scrollSpacing   滚动间距
     */
    public static void loadModuleConfig(JScrollPane paneTextContent, JSlider scrollSpacing) {
        // 页面滚动步长
        JScrollBar jScrollBar = new JScrollBar();
        // 滚动步长为8
        jScrollBar.setMaximum(8);
        paneTextContent.setVerticalScrollBar(jScrollBar);

        // 设置设备滑块 最大最小值
        scrollSpacing.setMinimum(8);
        scrollSpacing.setMaximum(26);

        // 设置滑块刻度间距
        scrollSpacing.setMajorTickSpacing(2);

        // 显示标签
        scrollSpacing.setPaintLabels(true);
        scrollSpacing.setPaintTicks(true);
        scrollSpacing.setPaintTrack(true);
    }

    /**
     * 初始化页面组件提示信息
     *
     * @param btnSearch     搜索按钮
     * @param openBook      阅读按钮
     * @param btnOn         上一章
     * @param underOn       下一章
     * @param jumpButton    跳转
     * @param fontSizeDown  放大
     * @param fontSizeUp    缩小
     * @param scrollSpacing 滚动间距
     */
    public static void loadComponentTooltip(JButton btnSearch, JButton openBook, JButton btnOn,
                                            JButton underOn, JButton jumpButton, JButton fontSizeDown,
                                            JButton fontSizeUp, JSlider scrollSpacing) {
        // 搜索按钮
        if (btnSearch != null) {
            btnSearch.setToolTipText(DataCenter.SEARCH_BTN);
        }
        // 阅读按钮
        if (openBook != null) {
            openBook.setToolTipText(DataCenter.START_READ);
        }
        // 上一章
        btnOn.setToolTipText(DataCenter.BTN_ON);
        // 下一章
        underOn.setToolTipText(DataCenter.UNDER_ON);
        // 跳转
        jumpButton.setToolTipText(DataCenter.JUMP_BUTTON);
        // 放大
        fontSizeDown.setToolTipText(DataCenter.FONT_SIZE_DOWN);
        // 缩小
        fontSizeUp.setToolTipText(DataCenter.FONT_SIZE_UP);
        // 滚动间距
        scrollSpacing.setToolTipText(DataCenter.SCROLL_SPACING);
    }
}
