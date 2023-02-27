package com.liao.book.entity;

import com.intellij.util.xmlb.annotations.Transient;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 通用信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class DataCenter {

    // 书籍列表
    @Transient
    public static List<BookData> bookData;

    // 表头内容
    @Transient
    public static String[] head = {"文章名称", "最新章节", "作者", "更新", "链接"};

    // 数据模型
    @Transient
    public static DefaultTableModel tableModel = new DefaultTableModel(null, head);

    // 书本连接
    @Transient
    public static String link;

    // 当前章节下标
    // public static int nowChapterIndex = 0;

    // 章节集合
    // public static List<Chapter> chapters = new ArrayList<>();

    // 章节内容
    // public static String textContent;

    // 全局搜索类型
    // public static String searchType;

    // 笔趣阁
    @Transient
    public static final String BI_QU_GE = "新笔趣阁";

    // 妙笔阁
    @Transient
    public static final String MI_BI_GE = "妙笔阁";

    // 全本小说
    @Transient
    public static final String QUAN_BEN = "全本小说";

    // 笔趣阁
    @Transient
    public static final String BI_QU_GE_2 = "笔趣阁";

    // 69书吧
    @Transient
    public static final String SHU_BA_69 = "69书吧";

    // 58小说
    @Transient
    public static final String SHU_BA_58 = "58小说";

    // 顶点小说
    @Transient
    public static final String SHU_TOP = "顶点小说";

    //千千小说
    @Transient
    public static final String QIAN_QIAN = "千千小说";

    // 数据源
    @Transient
    public static final String[] dataSource = new String[]{BI_QU_GE, MI_BI_GE,};

    /*****************组件悬浮提示*****************/
    // 搜索按钮
    @Transient
    public static final String searchBtn = "搜索";

    // 开始阅读
    @Transient
    public static final String startRead = "开始阅读";

    // 上一章
    @Transient
    public static final String btnOn = "上一章";

    // 下一章
    @Transient
    public static final String underOn = "下一章";

    // 跳转章节
    @Transient
    public static final String jumpButton = "章节跳转";

    // 文字放大
    @Transient
    public static final String fontSizeDown = "放大";

    // 字体缩小
    @Transient
    public static final String fontSizeUp = "缩小";

    // 同步阅读
    @Transient
    public static final String synchronous = "同步";

    // 滚动间距
    @Transient
    public static final String scrollSpacing = "阅读滚动间距";

}
