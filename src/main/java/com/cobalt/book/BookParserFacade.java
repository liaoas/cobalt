package com.cobalt.book;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * <p>
 * 爬取书籍信息
 * </p>
 *
 * @author LiAo
 * @since 2021/1/13
 */
public class BookParserFacade {

    public boolean initBook(Object book) {
        if (book == null) return false;

        if (book instanceof String) {
            return new NetworkBookParser().parser(book);
        } else if (book instanceof VirtualFile) {
            return new FileBookParser().parser(book);
        }
        return false;
    }
}
