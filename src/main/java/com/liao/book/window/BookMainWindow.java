package com.liao.book.window;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.AnimatedIcon;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import com.liao.book.service.BookChapterService;
import com.liao.book.service.BookSearchService;
import com.liao.book.service.BookTextService;
import com.liao.book.utile.DataConvert;
import com.liao.book.utile.ToastUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

/**
 * <p>
 * 页面
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
@SuppressWarnings("all")
public class BookMainWindow {
    // 搜索按钮
    private JButton btnSearch;

    // 搜索文本框
    private JTextField textSearchBar;

    // 窗口
    private JPanel bookMainJPanel;

    // 开始阅读按钮
    private JButton opneBook;

    // 搜索书本新信息
    private JTable searchBookTable;

    // 上一章按钮
    private JButton btnOn;

    // 下一章按钮
    private JButton underOn;

    // 章节跳转按钮
    private JButton JumpButton;

    // 章节内容
    private JTextArea textContent;

    // 章节内容外部框
    private JScrollPane paneTextContent;

    // 字体放大
    private JButton fontSizeDown;

    // 字体调小
    private JButton fontSizeUp;

    // 章节目录下拉列表
    private JComboBox<String> chapterList;

    // 表格外围
    private JScrollPane tablePane;

    // 小说内容盒子
    private JPanel textJPanel;

    // 同步阅读
    private JButton synchronous;

    // 字体默认大小
    private Integer fontSize = 12;

    // 全屏阅读控制
    private Boolean isShow = true;

    // 表格大小
    private double tableHeight = 0;

    // 搜索下拉列表数据源
    private JComboBox sourceDropdown;

    // 滚动间距
    private JSlider scrollSpacing;


    // 初始化数据
    private void init() {

        // 初始化表格
        searchBookTable.setModel(DataCenter.tableModel);
        searchBookTable.setEnabled(true);


        // 页面滚动步长
        JScrollBar jScrollBar = new JScrollBar();
        // 滚动步长为2
        jScrollBar.setMaximum(2);
        paneTextContent.setVerticalScrollBar(jScrollBar);

        // 设置表格内容大小
        tablePane.setPreferredSize(new Dimension(-1, 30));

        chapterList.setPreferredSize(new Dimension(1200, 20));


        // 加载数据源下拉框
        for (String dataSourceName : DataCenter.dataSource) {
            sourceDropdown.addItem(dataSourceName);
        }

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
    public BookMainWindow(Project project, ToolWindow toolWindow) {

        // 执行初始化表格
        init();

        // 书籍处理
        BookSearchService searchService = new BookSearchService();

        // 搜索单击按钮
        btnSearch.addActionListener(e -> {

            JLabel label = new JLabel("Loading...", new AnimatedIcon.Default(), SwingConstants.RIGHT);

            // 清空表格数据
            DataCenter.tableModel.setRowCount(0);
            // 执行搜索
            String bookSearchName = textSearchBar.getText();

            if (bookSearchName == null || bookSearchName.equals("")) {
                ToastUtil.notification2020_3Rear(project, "请输入书籍名称", NotificationType.ERROR);
                return;
            }

            // 获取数据源类型
            DataCenter.searchType = sourceDropdown.getSelectedItem().toString();

            // 重置 重试次数
            BookSearchService.index = 2;

            // 根据数据源类型 搜索
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<BookData> bookData;
                    bookData = searchService.getBookNameData(bookSearchName);
                    if (bookData == null || bookData.size() == 0) {
                        ToastUtil.notification2020_3Rear(project, "没有找到啊", NotificationType.ERROR);
                        return;
                    }

                    for (BookData bookDatum : bookData) {
                        DataCenter.tableModel.addRow(DataConvert.comvert(bookDatum));
                    }
                }
            }).start();
        });

        // 开始阅读按钮
        opneBook.addActionListener(e -> {
            // 获取选中行数据
            int selectedRow = searchBookTable.getSelectedRow();

            if (selectedRow < 0) {
                ToastUtil.notification2020_3Rear(project, "还没有选择要读哪本书", NotificationType.ERROR);
                return;
            }

            // 获取书籍链接
            String valueAt = searchBookTable.getValueAt(selectedRow, 4).toString();

            // 重置重试次数
            BookChapterService.index = 2;

            new Thread(new Runnable() {
                @Override
                public void run() {

                    switch (DataCenter.searchType) {
                        case DataCenter.BI_QU_GE:
                            // 笔趣阁
                            BookChapterService.searchBookChapterData(valueAt);
                            break;
                        case DataCenter.MI_BI_GE:
                            // 妙笔阁
                            BookChapterService.searchBookChapterData_miao(valueAt);
                            break;
                        case DataCenter.QUAN_BEN:
                            // 全本小说网
                            BookChapterService.searchBookChapterData_tai(valueAt);
                            break;
                        case DataCenter.BI_QU_GE_2:
                            // 笔趣阁2
                            BookChapterService.searchBookChapterData_bqg2(valueAt);
                            break;
                        case DataCenter.SHU_BA_69:
                            // 69书吧
                            BookChapterService.searchBookChapterData_69shu(valueAt);
                            break;
                        case DataCenter.SHU_BA_58:
                            // 58小说
                            BookChapterService.searchBookChapterData_58(valueAt);
                            break;
                        case DataCenter.SHU_TOP:
                            // 顶点小说
                            BookChapterService.searchBookChapterData_top(valueAt);
                            break;
                    }


                    // 解析连接 执行章节爬取
                    /*if (valueAt.contains("xbiquge")) {
                        BookChapterService.searchBookChapterData(valueAt);
                    } else if (valueAt.contains("imiaobige")) {
                        BookChapterService.searchBookChapterData_miao(valueAt);
                    } else if (valueAt.contains("xqb5200")) {
                        BookChapterService.searchBookChapterData_tai(valueAt);
                    } else if (valueAt.contains("biduoxs")) {
                        BookChapterService.searchBookChapterData_bqg2(valueAt);
                    } else if (valueAt.contains("69shuba")) {
                        BookChapterService.searchBookChapterData_69shu(valueAt);
                    } else if (valueAt.contains("wbxsw")) {
                        BookChapterService.searchBookChapterData_58(valueAt);
                    } else if (valueAt.contains("maxreader")) {
                        BookChapterService.searchBookChapterData_top(valueAt);
                    }*/


                    ApplicationManager.getApplication().runReadAction(new Runnable() {
                        @Override
                        public void run() {
                            // 清空章节信息
                            DataCenter.nowChapterINdex = 0;

                            // 清空下拉列表
                            chapterList.removeAllItems();

                            // 加载下拉列表
                            for (Chapter chapter : DataCenter.chapters) {
                                chapterList.addItem(chapter.getName());
                            }

                            // 解析当前章节内容
                            initReadText(project);
                        }
                    });
                }
            }).start();
        });

        // 上一章节跳转
        btnOn.addActionListener(e -> {

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == 0) {
                ToastUtil.notification2020_3Rear(project, "已经是第一章了", NotificationType.ERROR);
                return;
            }
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex - 1;
            initReadText(project);
        });

        // 下一章跳转
        underOn.addActionListener(e -> {

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == DataCenter.chapters.size()) {
                ToastUtil.notification2020_3Rear(project, "已经是最后一章了", NotificationType.ERROR);
                return;
            }

            // 章节下标加一
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex + 1;
            initReadText(project);
        });

        // 章节跳转事件
        JumpButton.addActionListener(e -> {
            // 根据下标跳转
            DataCenter.nowChapterINdex = chapterList.getSelectedIndex();

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex < 0) {
                ToastUtil.notification2020_3Rear(project, "未知章节", NotificationType.ERROR);
                return;
            }

            initReadText(project);
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
            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex < 0) {
                ToastUtil.notification2020_3Rear(project, "未知章节", NotificationType.ERROR);
                return;
            }
            initReadText(project);
        });


        // 滑块滑动事件
        scrollSpacing.addChangeListener(new ChangeListener() {
            /**
             * Invoked when the target of the listener has changed its state.
             *
             * @param e a ChangeEvent object
             */
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


    // 初始化阅读信息
    public void initReadText(Project project) {

        // 清空书本表格
        // DataCenter.tableModel.setRowCount(0);
        Chapter chapter = DataCenter.chapters.get(DataCenter.nowChapterINdex);

        // 当前章节名称
        // CurrentChapterName.setText(chapter.getName());
        // 章节下标
        // textJump.setText((DataCenter.nowChapterINdex + 1) + "");

        // 重置重试次数
        BookTextService.index = 2;

        // 内容
        BookTextService.searchBookChapterData(chapter.getLink());

        if (DataCenter.textContent == null) {
            ToastUtil.notification2020_3Rear(project, "章节内容为空", NotificationType.ERROR);
        }

        // 章节内容赋值
        textContent.setText(DataCenter.textContent);
        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());
        // 回到顶部
        textContent.setCaretPosition(1);

        /*new Thread(new Runnable() {
            @Override
            public void run() {

                ApplicationManager.getApplication().runReadAction(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();*/
    }

    /**
     * 初始化页面组件提示信息
     */
    public void setComponentTooltip() {
        // 搜索按钮
        btnSearch.setToolTipText(DataCenter.searchBtn);
        // 阅读按钮
        opneBook.setToolTipText(DataCenter.startRead);
        // 上一章
        btnOn.setToolTipText(DataCenter.btnOn);
        // 下一章
        underOn.setToolTipText(DataCenter.underOn);
        // 跳转
        JumpButton.setToolTipText(DataCenter.jumpButton);
        // 放大
        fontSizeDown.setToolTipText(DataCenter.fontSizeDown);
        // 缩小
        fontSizeUp.setToolTipText(DataCenter.fontSizeUp);
        // 同步阅读
        synchronous.setToolTipText(DataCenter.synchronous);
        // 滚动间距
        scrollSpacing.setToolTipText(DataCenter.scrollSpacing);

    }


    // 窗口信息
    public JPanel getBookMainJPanel() {
        return bookMainJPanel;
    }

    private void createUIComponents() {
    }


}

