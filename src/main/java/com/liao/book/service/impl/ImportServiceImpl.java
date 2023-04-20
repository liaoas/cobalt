package com.liao.book.service.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.liao.book.entity.ImportBookData;
import com.liao.book.parse.EpubContentParser;
import com.liao.book.parse.TxtContentParser;
import com.liao.book.service.ImportService;
import com.liao.book.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导入本地书籍处理类
 * </p>
 *
 * @author LiAo
 * @since 2023-04-18
 */
public class ImportServiceImpl implements ImportService {

    /**
     * 导入书籍
     *
     * @param file 书籍
     * @return 是否导入成功，true：成功、false：失败
     */
    @Override
    public boolean importBook(@NotNull VirtualFile file) {
        // 获取扩展名
        String extension = file.getExtension();

        if (StringUtils.isEmpty(extension)) return false;

        // 获取文件路径
        String filePath = file.getPath();

        if (StringUtils.isEmpty(filePath)) return false;

        Map<String, String> bookMap = new HashMap<>(16);

        // 执行书籍解析
        try {
            if (extension.equals("txt") || extension.equals("TXT")) {
                bookMap = TxtContentParser.parseTxt(filePath);
            } else if (extension.equals("epub") || extension.equals("EPUB")) {
                // epub
                bookMap = EpubContentParser.parseEpub(filePath);
            }
        } catch (Exception e) {
            return false;
        }

        if (bookMap.isEmpty()) return false;

        List<String> chapterList = new ArrayList<>(bookMap.keySet());

        // 存储书籍
        ImportBookData instance = ImportBookData.getInstance();

        instance.setChapterList(chapterList);
        instance.setBookMap(bookMap);

        return true;
    }

}
