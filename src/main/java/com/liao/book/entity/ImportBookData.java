package com.liao.book.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导入的书籍数据存放
 * </p>
 *
 * @author LiAo
 * @since 2023-04-19
 */
public class ImportBookData {

    // 单例
    private static final ImportBookData INSTANCE = new ImportBookData();

    // 存储书籍信息
    private Map<String, String> bookMap = new HashMap<>(16);

    // 存储章节列表
    private List<Chapter> chapterList = new ArrayList<>(16);

    private ImportBookData() {
    }

    public static ImportBookData getInstance() {
        return INSTANCE;
    }

    public Map<String, String> getBookMap() {
        return INSTANCE.bookMap;
    }

    public void setBookMap(Map<String, String> bookMap) {
        INSTANCE.bookMap = bookMap;
    }

    public List<Chapter> getChapterList() {
        return INSTANCE.chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        INSTANCE.chapterList = chapterList;
    }
}
