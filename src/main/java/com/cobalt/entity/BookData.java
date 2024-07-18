package com.cobalt.entity;

/**
 * <p>
 * 存放书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class BookData {

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

    public BookData() {
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
}
