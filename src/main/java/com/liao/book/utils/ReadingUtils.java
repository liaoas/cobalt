package com.liao.book.utils;

import com.liao.book.common.Constants;
import com.liao.book.dao.ReadingProgressDao;
import com.liao.book.entity.Chapter;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    }

}
