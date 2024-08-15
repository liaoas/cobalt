package com.cobalt.parser.book;

import com.cobalt.common.constant.UIConstants;
import com.cobalt.common.enums.ToastType;
import com.cobalt.common.utils.ModuleUtils;
import com.cobalt.common.utils.ToastUtils;
import com.cobalt.framework.factory.BeanFactory;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * <p>
 * 书籍搜索异步工作任务
 * </p>
 *
 * @author LiAo
 * @since 2024-03-27
 */
public final class BooksWorker extends SwingWorker<Void, List<Book>> {

    static BookParserFacade bookParser = (BookParserFacade) BeanFactory.getBean("BookParserFacade");
    // 搜索书籍名称
    private final String bookSearchName;
    // 全局模块对象
    public final Project project;
    // 窗口
    private final JPanel mainPanel;

    public BooksWorker(String bookSearchName, Project project, JPanel mainPanel) {
        this.bookSearchName = bookSearchName;
        this.project = project;
        this.mainPanel = mainPanel;
    }

    @Override
    protected Void doInBackground() {

        if (!bookParser.initBook(bookSearchName)) {
            ToastUtils.showToastMassage(project, "没有找到关于 “" + bookSearchName + "” 的书，换个搜索词试试吧。", ToastType.ERROR);
            return null;
        }
        //将当前进度信息加入chunks中
        publish(BookMetadata.getInstance().getBooks());
        return null;
    }

    @Override
    protected void process(List<List<Book>> chunks) {
        List<Book> bookData = chunks.get(0);
        for (Book bookDatum : bookData) {
            UIConstants.tableModel.addRow(bookDatum.bookData2Array());
        }
    }

    @Override
    protected void done() {
        // 恢复默认鼠标样式
        ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
    }
}
