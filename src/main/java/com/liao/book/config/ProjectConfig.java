
package com.liao.book.config;

import com.intellij.AbstractBundle;
import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * <p>
 * Config 配置信息获取
 * AbstractBundle 类 2022.2 API 删除 改为 DynamicBundle
 * https://plugins.jetbrains.com/docs/intellij/api-changes-list-2022.html#intellij-platform-20222
 *
 * </p>
 *
 * @author LiAo
 * @since 2022-12-28
 */
public class ProjectConfig extends AbstractBundle {

    // 配置文件名称
    public static final String CONFIG_NAME = "application";

    private static final ProjectConfig config = new ProjectConfig();

    protected ProjectConfig() {
        super(CONFIG_NAME);
    }

    /**
     * 获取配置文件中的指定key属性
     *
     * @param key    key
     * @param params params
     * @return value
     */
    public static String getConfigValue(
            @NotNull @PropertyKey(resourceBundle = CONFIG_NAME) String key, @NotNull Object... params) {
        return config.getMessage(key, params);
    }
}