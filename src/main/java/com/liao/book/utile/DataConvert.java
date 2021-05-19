package com.liao.book.utile;

import com.liao.book.entity.BookData;

/**
 * <p>
 * 表格内容赋值
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class DataConvert {
    public static String[] comvert(BookData noteData){
        String[] raw = new String[5];

        raw[0] = noteData.getBookName();
        raw[1] = noteData.getChapter();
        raw[2] = noteData.getAuthor();
        raw[3] = noteData.getUpdateDate();
        raw[4] = noteData.getBookLink();
        return raw;
    }
}
