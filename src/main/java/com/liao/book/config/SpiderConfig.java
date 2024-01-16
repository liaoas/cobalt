package com.liao.book.config;

/**
 * <p>
 * 爬虫配置文件
 * </p>
 *
 * @author LiAo
 * @since 2024-01-15
 */
public class SpiderConfig {

    /**
     * 读取爬虫描述内容，写入persistence中
     */
    static {
        String configValue = ProjectConfig.getConfigValue("spider-action-default");


    }
}
