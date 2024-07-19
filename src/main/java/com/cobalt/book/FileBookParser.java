package com.cobalt.book;

import com.cobalt.chapter.Chapter;
import com.cobalt.common.constant.Constants;
import com.cobalt.common.utils.StringUtils;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author LiAo
 * @since 2024/7/19
 */
public class FileBookParser extends AbstractBookParser {

    // 存储书籍信息
    Map<String, String> bookMap = new HashMap<>(16);
    // 存储目录信息
    List<Chapter> chapterList = new ArrayList<>(16);

    @Override
    public boolean parser(Object object) {
        VirtualFile file = (VirtualFile) object;

        // 获取扩展名
        String extension = file.getExtension();

        if (StringUtils.isEmpty(extension)) return false;

        if (isText(extension)) {
            return new TextBookParser().parser(object);
        } else {
            return new EpubBookParser().parser(object);
        }
    }
}
