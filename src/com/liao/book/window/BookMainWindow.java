package com.liao.book.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
import com.liao.book.entity.DataCenter;
import com.liao.book.service.BookChapterService;
import com.liao.book.service.BookSearchService;
import com.liao.book.service.BookTextService;
import com.liao.book.utile.DataConvert;

import javax.swing.*;
import java.util.List;

/**
 * <p>
 * 页面
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
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
    // 章节跳转信息
    private JTextField textJump;
    // 章节跳转按钮
    private JButton JumpButton;
    // 当前章节信息
    private JLabel CurrentChapterName;
    // 章节内容
    private JTextArea textContent;
    // 章节内容
    // private JLabel textContent;


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
        // 搜索单击按钮
        btnSearch.addActionListener(e -> {
            List<BookData> bookData = null;
            // 清空表格数据
            DataCenter.tableModel.setRowCount(0);
            // 执行搜索
            String bookSearchName = textSearchBar.getText();
            bookData = searchService.searchBookNameDate(bookSearchName);
            for (BookData bookDatum : bookData) {
                DataCenter.tableModel.addRow(DataConvert.comvert(bookDatum));
            }
        });

        // 开始阅读按钮
        opneBook.addActionListener(e -> {
            // 获取选中行数据
            int selectedRow = searchBookTable.getSelectedRow();
            Object valueAt = searchBookTable.getValueAt(selectedRow, 4);
            // 获取链接
            BookChapterService.searchBookChapterDate(valueAt.toString());
            // 清空章节信息
            DataCenter.nowChapterINdex = 0;
            // 解析当前章节内容
            initReadText();

        });

        // 上一章节跳转
        btnOn.addActionListener(e -> {

            if (DataCenter.nowChapterINdex == 0) {
                return;
            }
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex - 1;
            initReadText();
        });

        // 下一章跳转
        underOn.addActionListener(e -> {
            DataCenter.nowChapterINdex = DataCenter.nowChapterINdex + 1;
            initReadText();
        });

        // 章节跳转事件
        JumpButton.addActionListener(e -> {
            // 获取输入框内容
            String text = textJump.getText();
            if (text.matches("^\\+?[1-9][0-9]*$")) {
                Integer integer = Integer.valueOf(text);
                DataCenter.nowChapterINdex = integer - 1;
                initReadText();
            }

        });
    }

    // 初始化阅读信息
    public void initReadText() {
        // 清空书本表格
        // DataCenter.tableModel.setRowCount(0);
        Chapter chapter = DataCenter.chapters.get(DataCenter.nowChapterINdex);
        // 当前章节名称
        CurrentChapterName.setText(chapter.getName());
        // 章节下标
        textJump.setText((DataCenter.nowChapterINdex + 1) + "");
        // 内容
        BookTextService.searchBookChapterDate(chapter.getLink());
        // 章节内容赋值
        textContent.setText(DataCenter.textContent);
    }


    // 窗口信息
    public JPanel getBookMainJPanel() {
        return bookMainJPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
