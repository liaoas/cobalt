package com.cobalt.common.constant;

import java.util.regex.Pattern;

/**
 * <p>
 * 常量信息
 * </p>
 *
 * @author LiAo
 * @since 2023-04-04
 */
public class Constants {

    /**
     * 默认章节解析正则
     */
    public static final String DEFAULT_CHAPTER_REGULAR = "^\\s*[第卷][0123456789一二三四五六七八九十零〇百千两]*[章回部节集卷].*";

    /**
     * 默认字体大小
     */
    public static final int DEFAULT_FONT_SIZE = 16;

    /**
     * 默认滚轮速度
     */
    public static final int DEFAULT_READ_ROLL_SIZE = 8;

    /**
     * Main 页面分割面板默认位置
     */
    public static final int DEFAULT_MAIN_SPLIT_POSITION = 300;

    /**
     * 页面大小数值设置正则
     */
    public static final String SIZE_REGULAR = "^[1-9][0-9]?$|^100$";

    /**
     * 默认正则
     */
    public static final Pattern INTERGER_PATTERN = Pattern.compile(SIZE_REGULAR);

    /**
     * epub
     */
    public static final String EPUB_STR_UPPERCASE = "EPUB";
    public static final String EPUB_STR_LOWERCASE = "epub";

    /**
     * txt
     */
    public static final String TXT_STR_UPPERCASE = "TXT";
    public static final String TXT_STR_LOWERCASE = "txt";
}


