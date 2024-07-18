package com.cobalt.work;

import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.common.core.Convert;
import com.cobalt.common.enums.ToastType;
import com.cobalt.entity.BookData;
import com.cobalt.common.utils.ModuleUtils;
import com.cobalt.common.utils.ToastUtils;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.service.SearchService;
import com.cobalt.service.impl.SearchServiceImpl;
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
public final class SearchBooksWork extends SwingWorker<Void, List<BookData>> {

    static SearchService searchService = (SearchServiceImpl) BeanFactory.getBean("SearchServiceImpl");

    // 搜索书籍名称
    private final String bookSearchName;

    // 全局模块对象
    public final Project project;

    // 窗口
    private final JPanel mainPanel;

    public SearchBooksWork(String bookSearchName, Project project, JPanel mainPanel) {
        this.bookSearchName = bookSearchName;
        this.project = project;
        this.mainPanel = mainPanel;
    }

    @Override
    protected Void doInBackground() {
        List<BookData> bookData = searchService.getBookNameData(bookSearchName);

        if (bookData == null || bookData.isEmpty()) {
            ToastUtils.showToastMassage(project, "没有找到啊", ToastType.ERROR);
            return null;
        }

        //将当前进度信息加入chunks中
        publish(bookData);
        return null;
    }

    @Override
    protected void process(List<List<BookData>> chunks) {
        List<BookData> bookData = chunks.get(0);
        for (BookData bookDatum : bookData) {
            ModuleConstants.tableModel.addRow(Convert.bookData2Array(bookDatum));
        }
    }

    @Override
    protected void done() {
        // 恢复默认鼠标样式
        ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
    }

}
