package com.liao.book.entity;

import javax.swing.table.DefaultTableModel;

/**
 * <p>
 * 通用信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class DataCenter {

    // 表头内容
    public static String[] head = {"文章名称", "最新章节", "作者", "更新", "链接"};

    // 数据模型
    public static DefaultTableModel tableModel = new DefaultTableModel(null, head);

    // 笔趣阁
    public static final String BI_QU_GE = "新笔趣阁";

    // 妙笔阁
    public static final String MI_BI_GE = "妙笔阁";

    // 全本小说
    public static final String QUAN_BEN = "全本小说";

    // 笔趣阁
    public static final String BI_QU_GE_2 = "笔趣阁";

    // 69书吧
    public static final String SHU_BA_69 = "69书吧";

    // 58小说
    public static final String SHU_BA_58 = "58小说";

    // 顶点小说
    public static final String SHU_TOP = "顶点小说";

    //千千小说
    public static final String QIAN_QIAN = "千千小说";

    // 数据源
    public static final String[] DATA_SOURCE = new String[]{BI_QU_GE, MI_BI_GE,};

    // 选项卡白标签
    public static final String TAB_CONTROL_TITLE_HOME = "首页";

    public static final String TAB_CONTROL_TITLE_UNFOLD = "全屏";

    /*****************组件悬浮提示*****************/
    // 搜索按钮
    public static final String SEARCH_BTN = "搜索";

    // 开始阅读
    public static final String START_READ = "开始阅读";

    // 上一章
    public static final String BTN_ON = "上一章";

    // 下一章
    public static final String UNDER_ON = "下一章";

    // 跳转章节
    public static final String JUMP_BUTTON = "章节跳转";

    // 文字放大
    public static final String FONT_SIZE_DOWN = "放大";

    // 字体缩小
    public static final String FONT_SIZE_UP = "缩小";

    // 滚动间距
    public static final String SCROLL_SPACING = "阅读滚动间距";

}
