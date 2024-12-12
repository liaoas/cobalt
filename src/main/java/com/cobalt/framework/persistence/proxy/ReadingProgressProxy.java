package com.cobalt.framework.persistence.proxy;

import com.cobalt.framework.persistence.ReadingProgress;
import com.cobalt.parser.chapter.Chapter;

import java.util.List;

/**
 * 阅读进度持久化操作类的代理类
 * 与业务代码进行解耦
 *
 * @author LiAo
 * @since 2024/12/12
 */
public class ReadingProgressProxy {

    private final ReadingProgress instance;

    public ReadingProgressProxy() {
        instance = ReadingProgress.getInstance();
    }

    public String getSearchType() {
        return this.instance.searchType;
    }

    public void setSearchType(String searchType) {
        this.instance.searchType = searchType;
        this.instance.loadState();
    }

    public int getNowChapterIndex() {
        return this.instance.nowChapterIndex;
    }

    public void setNowChapterIndex(int nowChapterIndex) {
        this.instance.nowChapterIndex = nowChapterIndex;
        this.instance.loadState();
    }

    public List<Chapter> getChapters() {
        return this.instance.chapters;
    }

    public void chaptersClear() {
        this.instance.chapters.clear();
        this.loadState();
    }

    public void setChapters(List<Chapter> capture) {
        this.instance.chapters = capture;
        this.loadState();
    }

    public String getTextContent() {
        return this.instance.textContent;
    }

    public void setTextContent(String textContent) {
        this.instance.textContent = textContent;
        this.loadState();
    }

    public String getImportPath() {
        return this.instance.importPath;
    }

    public void setImportPath(String importPath) {
        this.instance.importPath = importPath;
        this.loadState();
    }

    public String getBookType() {
        return this.instance.bookType;
    }

    public void setBookType(String bookType) {
        this.instance.bookType = bookType;
        this.loadState();
    }

    private void loadState() {
        this.instance.loadState();
    }
}
