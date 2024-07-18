package com.cobalt.common.utils;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.constant.ModuleConstants;
import com.cobalt.entity.Chapter;
import com.cobalt.framework.factory.BeanFactory;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.framework.persistence.SettingsPersistent;
import com.cobalt.service.impl.ImportServiceImpl;
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

    // 阅读进度持久化
    static ReadingProgressPersistent instance = ReadingProgressPersistent.getInstance();

    // 页面设置持久化
    static SettingsPersistent settingDao = SettingsPersistent.getInstance();

    static ImportServiceImpl importService = (ImportServiceImpl) BeanFactory.getBean("ImportServiceImpl");

    /**
     * 加载阅读进度
     *
     * @param chapterList 章节列表
     * @param textContent 书籍内容
     */
    public static void loadReadingProgress(JComboBox<String> chapterList, JEditorPane textContent) {
        if (instance.chapters.isEmpty()) return;
        // 清空下拉列表
        chapterList.removeAllItems();
        // 加载下拉列表
        for (Chapter chapter : instance.chapters) {
            chapterList.addItem(chapter.getName());
        }
        Chapter chapter = instance.chapters.get(instance.nowChapterIndex);

        // 设置下拉框的值
        chapterList.setSelectedItem(chapter.getName());

        // 加载持久化书籍
        if (instance.searchType.equals(ModuleConstants.IMPORT) && StringUtils.isNotEmpty(instance.importPath)) {
            // 通过本地文件系统获取文件对象
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(instance.importPath);
            if (file == null) {
                return;
            }
            importService.importBook(file);
        }
        // 页面回显
        if (!instance.bookType.equals(Constants.EPUB_STR_LOWERCASE)) {
            // 章节内容赋值
            String htmlContent = ModuleUtils.fontSizeFromHtml(settingDao.fontSize, instance.textContent);
            textContent.setText(htmlContent);
            // 回到顶部
            textContent.setCaretPosition(1);
        }
    }

}
