package com.liao.book.entity;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  通用信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class DataCenter {

    // 书籍列表
    public static List<BookData> bookData;

    // 表头内容
    public static String[] head = {"文章名称", "最新章节", "作者", "更新","链接"};

    // 数据模型
    public static DefaultTableModel tableModel = new DefaultTableModel(null, head);


    // 书本连接
    public static String link;

    // 当前章节下标
    public static int nowChapterINdex = 0;

    // 章节集合
    public static List<Chapter> chapters = new ArrayList<>();

    // 章节内容
    public static String textContent;
}
