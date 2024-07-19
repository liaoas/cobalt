package com.cobalt.book;

import com.cobalt.chapter.Chapter;
import com.cobalt.framework.viewer.HTMLDocumentFactory;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 本地导入的书籍相关操作，单例
 * </p>
 *
 * @author LiAo
 * @since 2023-04-19
 */
public class BookMetadata {

    // 单例
    private static final BookMetadata INSTANCE = new BookMetadata();

    // 存储书籍信息
    private Map<String, String> bookMap = new HashMap<>(16);

    // 存储章节列表
    private List<Chapter> chapterList = new ArrayList<>(16);

    // Epub Book
    private Book epubBook = null;

    // books
    private List<com.cobalt.book.Book> books = null;

    // HTMLDocument
    private HTMLDocument bookHTMLDocument = null;

    // 章节内容
    private JEditorPane textContent = null;

    private Navigator navigator = null;

    private BookMetadata() {
    }

    public static void initDocument(int index) {
        Resource resource = INSTANCE.getBookResource(index);
        HTMLDocument document = INSTANCE.getHTMLDocument(resource);
        INSTANCE.getTextContent().setDocument(document);
        INSTANCE.setBookHTMLDocument(document);
    }

    public Resource getBookResource(int index) {
        return INSTANCE.epubBook.getTableOfContents().getTocReferences().get(index).getResource();
    }

    public HTMLDocument getHTMLDocument(Resource resource) {
        HTMLDocumentFactory htmlDocumentFactory = new HTMLDocumentFactory(INSTANCE.getNavigator(), INSTANCE.getTextContent().getEditorKit());
        return htmlDocumentFactory.getDocument(resource);
    }

    public static BookMetadata getInstance() {
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

    public JEditorPane getTextContent() {
        return INSTANCE.textContent;
    }

    public void setTextContent(JEditorPane textContent) {
        INSTANCE.textContent = textContent;
    }

    public void setEpubBookBook(Book ePubBook) {
        Navigator navigator = new Navigator(ePubBook);
        setNavigator(navigator);
        INSTANCE.epubBook = ePubBook;
    }

    public HTMLDocument getBookHTMLDocument() {
        return INSTANCE.bookHTMLDocument;
    }

    public void setBookHTMLDocument(HTMLDocument bookHTMLDocument) {
        INSTANCE.bookHTMLDocument = bookHTMLDocument;
    }

    public Navigator getNavigator() {
        return INSTANCE.navigator;
    }

    public void setNavigator(Navigator navigator) {
        INSTANCE.navigator = navigator;
    }

    public List<com.cobalt.book.Book> getBooks() {
        return books;
    }

    public void setBooks(List<com.cobalt.book.Book> books) {
        this.books = books;
    }
}
