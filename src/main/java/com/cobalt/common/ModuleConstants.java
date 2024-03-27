package com.cobalt.common;

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
    public static final String BI_QU_GE = "笔趣阁$";

    // 笔趣阁
    public static final String XIANG_SHU = "香书小说";

    // 本地导入
    public static final String IMPORT = "导入";

    /*****************选项卡标题*****************/

    // 选项卡标签
    public static final String TAB_CONTROL_TITLE_HOME = "首页";

    public static final String TAB_CONTROL_TITLE_UNFOLD = "全屏";

    public static final String TAB_CONTROL_READING_HISTORY = "帮助";

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
