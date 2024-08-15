package com.cobalt.parser.book;

/**
 * <p>
 * 存放书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class Book {

    private static final int BOOK_NAME_INDEX = 0;
    private static final int CHAPTER_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int UPDATE_DATE_INDEX = 3;
    private static final int BOOK_LINK_INDEX = 4;

    // 名称
    private String bookName;

    // 最新章节
    private String chapter;

    // 作者
    private String author;

    // 更新时间
    private String updateDate;

    // 链接
    private String bookLink;

    public Book() {
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    /**
     * BookData转为Swing JTable Model需要的数组
     *
     * @return array
     */
    public String[] bookData2Array() {
        String[] raw = new String[5];
        raw[BOOK_NAME_INDEX] = this.getBookName();
        raw[CHAPTER_INDEX] = this.getChapter();
        raw[AUTHOR_INDEX] = this.getAuthor();
        raw[UPDATE_DATE_INDEX] = this.getUpdateDate();
        raw[BOOK_LINK_INDEX] = this.getBookLink();
        return raw;
    }
}
