package com.cobalt.entity;

import nl.siegmann.epublib.domain.Book;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
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

    // 书籍类型
    private String bookType = "";

    // Epub Book
    private Book epubBook = null;

    // HTMLDocument
    private HTMLDocument bookHTMLDocument = null;

    // 章节内容
    private JEditorPane textContent = null;

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

    public String getBookType() {
        return INSTANCE.bookType;
    }

    public void setBookType(String bookType) {
        INSTANCE.bookType = bookType;
    }

    public JEditorPane getTextContent() {
        return INSTANCE.textContent;
    }

    public void setTextContent(JEditorPane textContent) {
        INSTANCE.textContent = textContent;
    }

    public Book getEpubBookBook() {
        return INSTANCE.epubBook;
    }

    public void setEpubBookBook(Book ePubBook) {
        INSTANCE.epubBook = ePubBook;
    }

    public HTMLDocument getBookHTMLDocument() {
        return INSTANCE.bookHTMLDocument;
    }

    public void setBookHTMLDocument(HTMLDocument bookHTMLDocument) {
        INSTANCE.bookHTMLDocument = bookHTMLDocument;
    }
}
