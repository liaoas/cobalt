package com.cobalt.service.impl;

import com.cobalt.common.constant.Constants;
import com.cobalt.common.domain.Chapter;
import com.cobalt.common.domain.ImportBookData;
import com.cobalt.common.parse.EpubContentParser;
import com.cobalt.common.parse.TxtContentParser;
import com.cobalt.common.utils.StringUtils;
import com.cobalt.framework.persistence.ReadingProgressPersistent;
import com.cobalt.service.ImportService;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        String filePath = file.getPath();

        if (StringUtils.isEmpty(filePath)) return false;
        // 存储书籍信息
        Map<String, String> bookMap = new HashMap<>(16);
        // 存储目录信息
        List<Chapter> chapterList = new ArrayList<>(16);
        try {
            if (isText(extension)) {
                bookMap = TxtContentParser.parseTxt(filePath, chapterList);
            } else if (isEpub(extension)) {
                bookMap = EpubContentParser.parseEpubByEpubLib(filePath, chapterList);
            }
        } catch (Exception e) {
            return false;
        }
        return !bookMap.isEmpty() && !chapterList.isEmpty();
    }

    public boolean isText(String extension){
        return extension.equals(Constants.TXT_STR_LOWERCASE) || extension.equals(Constants.TXT_STR_UPPERCASE);
    }


    public boolean isEpub(String extension){
        return extension.equals(Constants.EPUB_STR_LOWERCASE) || extension.equals(Constants.EPUB_STR_UPPERCASE);
    }
}
