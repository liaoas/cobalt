package com.cobalt.service.impl;

import com.cobalt.parse.EpubContentParser;
import com.cobalt.parse.TxtContentParser;
import com.cobalt.utils.StringUtils;
import com.intellij.openapi.vfs.VirtualFile;
import com.cobalt.entity.Chapter;
import com.cobalt.entity.ImportBookData;
import com.cobalt.service.ImportService;
import org.jetbrains.annotations.NotNull;

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

        // 存储书籍信息
        Map<String, String> bookMap = new HashMap<>(16);

        // 存储目录信息
        List<Chapter> chapterList = new ArrayList<>(16);
        // 执行书籍解析
        try {
            assert extension != null;
            if (extension.equals("txt") || extension.equals("TXT")) {
                bookMap = TxtContentParser.parseTxt(filePath, chapterList);
            } else if (extension.equals("epub") || extension.equals("EPUB")) {
                // epub
                bookMap = EpubContentParser.parseEpub(filePath, chapterList);
            }
        } catch (Exception e) {
            return false;
        }

        if (bookMap.isEmpty() || chapterList.isEmpty()) return false;

        // 存储书籍
        ImportBookData instance = ImportBookData.getInstance();

        instance.setChapterList(chapterList);
        instance.setBookMap(bookMap);

        return true;
    }

}
