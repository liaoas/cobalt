package com.liao.book.service;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * 导入本地书籍处理类
 * </p>
 *
 * @author LiAo
 * @since 2023-04-14
 */
public interface ImportService {

    /**
     * 导入书籍
     *
     * @param file 书籍
     * @return 是否导入成功，true：成功、false：失败
     */
    boolean importBook(@NotNull VirtualFile file);
}
