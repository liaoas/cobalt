package com.liao.book.common;

import javax.swing.table.DefaultTableModel;

/**
 * <p>
 * 组件信息常量
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class ModuleConstants {

    // 表头内容
    public static String[] head = {"文章名称", "最新章节", "作者", "更新", "链接"};

    // 搜索结果表格
    public static DefaultTableModel tableModel = new DefaultTableModel(null, head);

    /*****************数据源类型*****************/

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

    // 千千小说
    public static final String QIAN_QIAN = "千千小说";

    // 搜索数据源
    public static final String[] DATA_SOURCE = new String[]{BI_QU_GE, MI_BI_GE};

    /*****************选项卡标题*****************/

    // 选项卡标签
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

    // 文字设置
    public static final String SETTINGS = "设置";

    /*****************组件默认数据*****************/

    // 字体大小下拉框，鼠标滚动下拉框
    public static final int[] SIZE_ARRAY = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};

}
