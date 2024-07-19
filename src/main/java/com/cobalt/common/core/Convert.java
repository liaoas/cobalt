package com.cobalt.common.core;

import com.cobalt.parser.book.Book;

/**
 * <p>
 * 数据类型转换
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class Convert {
    /**
     * BookData转为数组
     *
     * @param noteData book
     * @return array
     */
    public static String[] bookData2Array(Book noteData) {
        String[] raw = new String[5];
        raw[0] = noteData.getBookName();
        raw[1] = noteData.getChapter();
        raw[2] = noteData.getAuthor();
        raw[3] = noteData.getUpdateDate();
        raw[4] = noteData.getBookLink();
        return raw;
    }
}
