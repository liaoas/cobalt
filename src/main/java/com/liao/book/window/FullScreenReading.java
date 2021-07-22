package com.liao.book.window;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import com.liao.book.service.BookTextService;
import com.liao.book.utile.ToastUtil;

import javax.swing.*;
import java.awt.*;

public class FullScreenReading {

    // 主体窗口
    private JPanel fullScreenJpanel;

    // 文章内容滑动
    private JScrollPane paneTextContent;

    // 书籍内容
    private JTextArea textContent;

    // 上一章按钮
    private JButton btnOn;

    // 下一章按钮
    private JButton underOn;

    // 章节列表
    private JComboBox<String> chapterList;

    // 跳转到指定章节
    private JButton jumpButton;

    // 字体加
    private JButton fontSizeDown;

    // 字体减
    private JButton fontSizeUp;

    // 同步阅读
    private JButton synchronous;

    // 字体默认大小
    private Integer fontSize = 12;

    // 窗口信息
    public JPanel getBookMainJPanel() {
        return fullScreenJpanel;
    }

    public FullScreenReading(Project project, ToolWindow toolWindow) {

        // 数据阅读 滚动步长为2
        JScrollBar jScrollBar = new JScrollBar();
        jScrollBar.setMaximum(2);
        paneTextContent.setVerticalScrollBar(jScrollBar);


        // 上一章节跳转
        btnOn.addActionListener(e -> {
            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == 0) {
                ToastUtil.notification2020_3Rear(project, "已经是第一章了", NotificationType.ERROR);
                return;
            }
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex - 1;
            initReadText();
        });

        // 下一章跳转
        underOn.addActionListener(e -> {
            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == DataCenter.chapters.size()) {
                ToastUtil.notification2020_3Rear(project, "已经是最后一章了", NotificationType.ERROR);
                return;
            }

            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex + 1;
            initReadText();
        });

        // 章节跳转事件
        jumpButton.addActionListener(e -> {

            if (DataCenter.nowChapterINdex < 1){
                ToastUtil.notification2020_3Rear(project, "未知章节", NotificationType.ERROR);
                return;
            }

            // 根据下标跳转
            DataCenter.nowChapterINdex = chapterList.getSelectedIndex();
            initReadText();
        });


        // 字号调小按钮单击事件
        fontSizeDown.addActionListener(e -> {

            if (fontSize == 1) {
                ToastUtil.notification2020_3Rear(project, "已经是最小的了", NotificationType.ERROR);
                return;
            }

            // 调小字体
            fontSize--;
            textContent.setFont(new Font("", 1, fontSize));
        });

        // 字体增大按钮
        fontSizeUp.addActionListener(e -> {
            // 调大字体
            fontSize++;
            textContent.setFont(new Font("", 1, fontSize));
        });

        // 同步阅读按钮
        synchronous.addActionListener(e -> {

            if (DataCenter.nowChapterINdex < 1){
                ToastUtil.notification2020_3Rear(project, "未知章节", NotificationType.ERROR);
                return;
            }

            startReading();
        });
    }

    // 开始阅读事件
    public void startReading() {
        // 清空下拉列表
        chapterList.removeAllItems();

        // 加载下拉列表
        for (Chapter chapter1 : DataCenter.chapters) {
            chapterList.addItem(chapter1.getName());
        }
        // 解析当前章节内容
        initReadText();
    }


    // 初始化阅读信息
    public void initReadText() {
        // 清空书本表格
        Chapter chapter = DataCenter.chapters.get(DataCenter.nowChapterINdex);

        // 重置重试次数
        BookTextService.index = 2;

        // 内容
        BookTextService.searchBookChapterData(chapter.getLink());
        // 章节内容赋值
        textContent.setText(DataCenter.textContent);

        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());
        // 回到顶部
        textContent.setCaretPosition(1);

    }
}
