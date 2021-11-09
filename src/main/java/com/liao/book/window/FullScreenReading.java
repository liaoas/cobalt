package com.liao.book.window;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import com.liao.book.factory.BeanFactory;
import com.liao.book.service.BookTextService;
import com.liao.book.service.impl.BookTextServiceImpl;
import com.liao.book.utile.ToastUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

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

    // 滚动间距
    private JSlider scrollSpacing;

    // 字体默认大小
    private Integer fontSize = 12;

    // 全局模块对象
    private final Project project;

    // 内容爬虫
    static BookTextService textService = (BookTextServiceImpl) BeanFactory
            .getBean("BookTextServiceImpl");

    // 窗口信息
    public JPanel getBookMainJPanel() {
        return fullScreenJpanel;
    }


    // 初始化数据
    private void init() {

        // 页面滚动步长
        JScrollBar jScrollBar = new JScrollBar();
        // 滚动步长为2
        jScrollBar.setMaximum(2);
        paneTextContent.setVerticalScrollBar(jScrollBar);

        chapterList.setPreferredSize(new Dimension(1200, 20));

        // 设置设备滑块 最大最小值
        scrollSpacing.setMinimum(0);
        scrollSpacing.setMaximum(20);

        scrollSpacing.setValue(2);
        // 设置滑块刻度间距
        scrollSpacing.setMajorTickSpacing(2);

        // 显示标签
        scrollSpacing.setPaintLabels(true);
        scrollSpacing.setPaintTicks(true);
        scrollSpacing.setPaintTrack(true);


        // 加载提示信息
        setComponentTooltip();
    }

    // 页面打开方法
    public FullScreenReading(Project project, ToolWindow toolWindow) {

        this.project = project;

        // 初始化信息
        init();

        // 上一章节跳转
        btnOn.addActionListener(e -> {
            // 等待鼠标样式
            setTheMouseStyle(Cursor.WAIT_CURSOR);

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == 0) {
                ToastUtil.notification2020_3Ago(project, "已经是第一章了", MessageType.ERROR);
                // 恢复默认鼠标样式
                setTheMouseStyle(Cursor.DEFAULT_CURSOR);
                return;
            }
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex - 1;
            // 加载阅读信息
            new LoadChapterInformation().execute();
        });

        // 下一章跳转
        underOn.addActionListener(e -> {

            // 等待鼠标样式
            setTheMouseStyle(Cursor.WAIT_CURSOR);

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == DataCenter.chapters.size()) {
                ToastUtil.notification2020_3Ago(project, "已经是最后一章了", MessageType.ERROR);
                return;
            }

            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex + 1;

            // 加载阅读信息
            new LoadChapterInformation().execute();
        });

        // 章节跳转事件
        jumpButton.addActionListener(e -> {
            // 等待鼠标样式
            setTheMouseStyle(Cursor.WAIT_CURSOR);

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex < 0) {
                ToastUtil.notification2020_3Ago(project, "未知章节", MessageType.ERROR);
                return;
            }

            // 根据下标跳转
            DataCenter.nowChapterINdex = chapterList.getSelectedIndex();

            // 加载阅读信息
            new LoadChapterInformation().execute();
        });

        // 字号调小按钮单击事件
        fontSizeDown.addActionListener(e -> {

            if (fontSize == 1) {
                ToastUtil.notification2020_3Ago(project, "已经是最小的了", MessageType.ERROR);
                return;
            }

            // 调小字体
            fontSize--;
            textContent.setFont(new Font("", Font.BOLD, fontSize));
        });

        // 字体增大按钮
        fontSizeUp.addActionListener(e -> {
            // 调大字体
            fontSize++;
            textContent.setFont(new Font("", Font.BOLD, fontSize));
        });

        // 同步阅读按钮
        synchronous.addActionListener(e -> {
            // 等待鼠标样式
            setTheMouseStyle(Cursor.WAIT_CURSOR);

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex < 0) {
                ToastUtil.notification2020_3Ago(project, "未知章节", MessageType.ERROR);
                return;
            }

            startReading();
        });

        // 滑块滑动事件
        scrollSpacing.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider jSlider = (JSlider) e.getSource();
                // 判断滑块是否停止
                if (!jSlider.getValueIsAdjusting()) {
                    paneTextContent.getVerticalScrollBar().setUnitIncrement(jSlider.getValue());
                }
            }
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
        // 加载阅读信息
        new LoadChapterInformation().execute();
    }

    /**
     * 异步GUI 线程加载 加载章节信息
     */
    final class LoadChapterInformation extends SwingWorker<Void, Chapter> {
        @Override
        protected Void doInBackground() {
            // 清空书本表格
            Chapter chapter = DataCenter.chapters.get(DataCenter.nowChapterINdex);

            // 重置重试次数
            BookTextServiceImpl.index = 2;

            // 内容
            textService.searchBookChapterData(chapter.getLink());

            if (DataCenter.textContent == null) {
                ToastUtil.notification2020_3Ago(project, "章节内容为空", MessageType.ERROR);
                return null;
            }

            //将当前进度信息加入chunks中
            publish(chapter);
            return null;
        }

        @Override
        protected void process(List<Chapter> chapters) {
            Chapter chapter = chapters.get(0);
            // 章节内容赋值
            textContent.setText(DataCenter.textContent);
            // 设置下拉框的值
            chapterList.setSelectedItem(chapter.getName());
            // 回到顶部
            textContent.setCaretPosition(1);
        }

        @Override
        protected void done() {
            // 恢复默认鼠标样式
            setTheMouseStyle(Cursor.DEFAULT_CURSOR);
        }
    }

    /**
     * 加载页面鼠标样式
     *
     * @param type 鼠标样式 {@link Cursor}
     */
    public void setTheMouseStyle(int type) {
        Cursor cursor = new Cursor(type);
        fullScreenJpanel.setCursor(cursor);
    }

    /**
     * 初始化页面组件提示信息
     */
    public void setComponentTooltip() {
        // 上一章
        btnOn.setToolTipText(DataCenter.btnOn);
        // 下一章
        underOn.setToolTipText(DataCenter.underOn);
        // 放大
        fontSizeDown.setToolTipText(DataCenter.fontSizeDown);
        // 缩小
        fontSizeUp.setToolTipText(DataCenter.fontSizeUp);
        // 同步阅读
        synchronous.setToolTipText(DataCenter.synchronous);
        // 滚动间距
        scrollSpacing.setToolTipText(DataCenter.scrollSpacing);

    }

    // 初始化阅读信息
    public void initReadText() {

        // 清空书本表格
        Chapter chapter = DataCenter.chapters.get(DataCenter.nowChapterINdex);

        // 重置重试次数
        BookTextServiceImpl.index = 2;

        // 内容
        textService.searchBookChapterData(chapter.getLink());
        // 章节内容赋值
        textContent.setText(DataCenter.textContent);

        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());
        // 回到顶部
        textContent.setCaretPosition(1);

    }
}
