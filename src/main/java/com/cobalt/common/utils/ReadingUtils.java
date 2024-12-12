package com.cobalt.common.utils;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.UIConstants;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.proxy.ReadingProgressProxy;
import com.cobalt.framework.persistence.proxy.SettingsParameterProxy;
import com.cobalt.parser.book.BookParserFacade;
import com.cobalt.parser.chapter.Chapter;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

/**
 * <p>
 * 阅读相关工具类
 * </p>
 *
 * @author LiAo
 * @since 2023-03-01
 */
public class ReadingUtils {

    static BookParserFacade bookParser = (BookParserFacade) BeanFactory.getBean("BookParserFacade");

    /**
     * 加载阅读进度
     *
     * @param chapterList 章节列表
     * @param textContent 书籍内容
     */
    public static void loadReadingProgress(JComboBox<String> chapterList, JEditorPane textContent) {

        ReadingProgressProxy readingProgress = new ReadingProgressProxy();
        SettingsParameterProxy settingsParameter = new SettingsParameterProxy();

        if (readingProgress.getChapters().isEmpty()) return;
        // 清空下拉列表
        chapterList.removeAllItems();
        // 加载下拉列表
        for (Chapter chapter : readingProgress.getChapters()) {
            chapterList.addItem(chapter.getName());
        }
        Chapter chapter = readingProgress.getChapters().get(readingProgress.getNowChapterIndex());

        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());

        // 加载持久化书籍
        if (readingProgress.getSearchType().equals(UIConstants.IMPORT) &&
                StringUtils.isNotEmpty(readingProgress.getImportPath())) {
            // 通过本地文件系统获取文件对象
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(readingProgress.getImportPath());
            if (file == null) {
                return;
            }
            bookParser.initBook(file);
        }
        // 页面回显
        if (!readingProgress.getBookType().equals(Constants.EPUB_STR_LOWERCASE)) {
            // 章节内容赋值
            String htmlContent = ModuleUtils.fontSizeFromHtml(settingsParameter.getFontSize(), readingProgress.getTextContent());
            textContent.setText(htmlContent);
            // 回到顶部
            textContent.setCaretPosition(1);
        }
    }

}
