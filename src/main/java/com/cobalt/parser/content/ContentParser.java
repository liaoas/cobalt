package com.cobalt.parser.content;

import com.cobalt.parser.Parser;
import com.cobalt.parser.book.BookMetadata;
import com.cobalt.framework.persistence.ReadingProgressPersistent;

import javax.swing.*;

/**
 * Content 解析抽象类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public interface ContentParser extends Parser {


    /**
     * 解析方法
     * 解析单个章节内容
     * 若当前书籍为网络爬取，则解析结果为 String,并将结果存入{@link ReadingProgressPersistent} textContent.
     * 若资源为本地导入，则进行判断书本类型，若类型为 text，则解析结果为 String,并将结果存入
     * {@link ReadingProgressPersistent} textContent.
     * 若类型为 epub，则解析结果为 String,并将结果存入
     * {@link BookMetadata} bookHTMLDocument, 并且渲染进当前活动页面的 {@link JEditorPane}.
     *
     * @param resource 资源
     * @return 是否成功
     */
    boolean parser(Object resource);
}
