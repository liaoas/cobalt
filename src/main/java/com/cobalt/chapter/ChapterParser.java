package com.cobalt.chapter;

import com.cobalt.book.BookMetadata;
import com.cobalt.framework.persistence.ReadingProgressPersistent;

/**
 * Chapter 解析抽象类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public interface ChapterParser {

    /**
     * 解析方法
     * 解析章节信息为 List<Chapter>,并将结果存入{@link ReadingProgressPersistent} chapters
     * 以供后续相关函数使用.
     *
     * @param resource 资源
     * @return 是否成功
     */
    boolean parser(Object resource);
}
