package com.liao.book.utils;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.liao.book.common.ModuleConstants;
import com.liao.book.factory.BeanFactory;
import com.liao.book.persistence.ReadingProgressDao;
import com.liao.book.entity.Chapter;
import com.liao.book.service.impl.ImportServiceImpl;

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

    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    static ImportServiceImpl importService = (ImportServiceImpl) BeanFactory.getBean("ImportServiceImpl");

    /**
     * 加载阅读进度
     *
     * @param chapterList 章节列表
     * @param textContent 书籍内容
     */
    public static void loadReadingProgress(JComboBox<String> chapterList, JTextArea textContent) {
        if (instance.chapters.isEmpty()) return;

        // 清空下拉列表
        chapterList.removeAllItems();

        // 加载下拉列表
        for (Chapter chapter : instance.chapters) {
            chapterList.addItem(chapter.getName());
        }

        Chapter chapter = instance.chapters.get(instance.nowChapterIndex);
        // 章节内容赋值
        textContent.setText(instance.textContent);
        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());
        // 回到顶部
        textContent.setCaretPosition(1);

        // 加载持久化书籍
        if (instance.searchType.equals(ModuleConstants.IMPORT) && StringUtils.isNotEmpty(instance.importPath)) {
            // 通过本地文件系统获取文件对象
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(instance.importPath);

            if (file == null) {
                return;
            }

            importService.importBook(file);
        }
    }

}
