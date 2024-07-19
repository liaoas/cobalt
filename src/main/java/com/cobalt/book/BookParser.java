package com.cobalt.book;

/**
 * Book 解析抽象类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public interface BookParser {

    /**
     * 解析方法
     * 若资源类型为 url，则解析结果为 List<Book>,并将结果存入{@link BookMetadata} books
     * 以供后续相关函数使用.
     * <p>
     * 若资源类型为 file，则解析结果为 章节信息: List<Chapter> 、 内容信息 Map<String, String>,
     * 并将结果存入{@link BookMetadata} chapterList、bookMap.
     * 以供后续相关函数使用
     *
     * @param resource 资源类型 url，file
     * @return 是否成功
     */
    boolean parser(Object resource);
}
