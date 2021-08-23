package com.liao.book.window;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.ToolWindow;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import com.liao.book.service.BookChapterService;
import com.liao.book.service.BookSearchService;
import com.liao.book.service.BookTextService;
import com.liao.book.utile.DataConvert;
import com.liao.book.utile.ToastUtil;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
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


    // 初始化数据
    private void init() {
        searchBookTable.setModel(DataCenter.tableModel);
        searchBookTable.setEnabled(true);
    }


    // 页面打开方法
    public BookMainWindow(Project project, ToolWindow toolWindow) {

        // 执行初始化表格
        init();
        BookSearchService searchService = new BookSearchService();

        JScrollBar jScrollBar = new JScrollBar();
        // 滚动步长为2
        jScrollBar.setMaximum(2);
        paneTextContent.setVerticalScrollBar(jScrollBar);

        // 设置表格内容大小
        tablePane.setPreferredSize(new Dimension(-1, 30));

        chapterList.setPreferredSize(new Dimension(1200, 20));


        // 加载数据源按钮
        for (String dataSourceName : DataCenter.dataSource) {
            sourceDropdown.addItem(dataSourceName);
        }


        // 搜索单击按钮
        btnSearch.addActionListener(e -> {
            // 清空表格数据
            DataCenter.tableModel.setRowCount(0);
            // 执行搜索
            String bookSearchName = textSearchBar.getText();

            if (bookSearchName == null || bookSearchName.equals("")) {
                ToastUtil.notification2020_3Ago(project, "请输入书籍名称", MessageType.ERROR);
                return;
            }

            // 获取数据源类型
            String searchType = sourceDropdown.getSelectedItem().toString();

            // 重置 重试次数
            BookSearchService.index = 2;

            // 根据数据源类型 搜索
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<BookData> bookData;
                    bookData = searchService.getBookNameData(searchType, bookSearchName);
                    if (bookData == null || bookData.size() == 0) {
                        ToastUtil.notification2020_3Ago(project, "没有找到啊", MessageType.ERROR);
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
                ToastUtil.notification2020_3Ago(project, "还没有选择要读哪本书", MessageType.ERROR);
                return;
            }

            // 获取书籍链接
            Object valueAt = searchBookTable.getValueAt(selectedRow, 4);

            // 重置重试次数
            BookChapterService.index = 2;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 解析连接 执行章节爬取
                    if (valueAt.toString().contains("xbiquge")) {
                        BookChapterService.searchBookChapterData(valueAt.toString());
                    } else if (valueAt.toString().contains("imiaobige")) {
                        BookChapterService.searchBookChapterData_miao(valueAt.toString());
                    } else if (valueAt.toString().contains("xqb5200")) {
                        BookChapterService.searchBookChapterData_tai(valueAt.toString());
                    } else if (valueAt.toString().contains("biduoxs")) {
                        BookChapterService.searchBookChapterData_bqg2(valueAt.toString());
                    } else if (valueAt.toString().contains("69shuba")) {
                        BookChapterService.searchBookChapterData_69shu(valueAt.toString());
                    } else if (valueAt.toString().contains("wbxsw")) {
                        BookChapterService.searchBookChapterData_58(valueAt.toString());
                    } else if (valueAt.toString().contains("maxreader")) {
                        BookChapterService.searchBookChapterData_top(valueAt.toString());
                    }
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
                            initReadText();
                        }
                    });
                }
            }).start();
        });

        // 上一章节跳转
        btnOn.addActionListener(e -> {

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == 0) {
                ToastUtil.notification2020_3Ago(project, "已经是第一章了", MessageType.ERROR);
                return;
            }
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex - 1;
            initReadText();
        });

        // 下一章跳转
        underOn.addActionListener(e -> {

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex == DataCenter.chapters.size()) {
                ToastUtil.notification2020_3Ago(project, "已经是最后一章了", MessageType.ERROR);
                return;
            }

            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex + 1;
            initReadText();
        });

        // 章节跳转事件
        JumpButton.addActionListener(e -> {
            // 根据下标跳转
            DataCenter.nowChapterINdex = chapterList.getSelectedIndex();

            if (DataCenter.chapters.size() == 0 || DataCenter.nowChapterINdex < 0) {
                ToastUtil.notification2020_3Ago(project, "未知章节", MessageType.ERROR);
                return;
            }

            initReadText();
        });


        // 字号调小按钮单击事件
        fontSizeDown.addActionListener(e -> {

            if (fontSize == 1) {
                ToastUtil.notification2020_3Ago(project, "已经是最小的了", MessageType.ERROR);
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
                ToastUtil.notification2020_3Ago(project, "未知章节", MessageType.ERROR);
                return;
            }
            initReadText();
        });


    }


    // 初始化阅读信息
    public void initReadText() {

        // 清空书本表格
        // DataCenter.tableModel.setRowCount(0);
        Chapter chapter = DataCenter.chapters.get(DataCenter.nowChapterINdex);

        // 当前章节名称
        // CurrentChapterName.setText(chapter.getName());
        // 章节下标
        // textJump.setText((DataCenter.nowChapterINdex + 1) + "");

        // 重置重试次数
        BookTextService.index = 2;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 内容
                BookTextService.searchBookChapterData(chapter.getLink());
                ApplicationManager.getApplication().runReadAction(new Runnable() {
                    @Override
                    public void run() {
                        // 章节内容赋值
                        textContent.setText(DataCenter.textContent);

                        // 设置下拉框的值
                        chapterList.setSelectedItem(chapter.getName());
                        // 回到顶部
                        textContent.setCaretPosition(1);
                    }
                });
            }
        }).start();

    }


    // 窗口信息
    public JPanel getBookMainJPanel() {
        return bookMainJPanel;
    }

    private void createUIComponents() {
    }


}

