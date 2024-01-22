package com.cobalt.persistence;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 *  持久化爬虫动作描述资源
 * </p>
 *
 * @author LiAo
 * @since 2024-01-15
 */
@State(name = "SpiderActionDao", storages = {@Storage(value = "cobalt.settings.dao.xml")})
public class SpiderActionDao implements PersistentStateComponent<SpiderActionDao> {

    public String spiderActionStr = "{}";

    public SpiderActionDao() {
    }

    public SpiderActionDao(String spiderActionStr) {
        this.spiderActionStr = spiderActionStr;
    }


    @Override
    public @Nullable SpiderActionDao getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SpiderActionDao spiderActionDao) {
        XmlSerializerUtil.copyBean(spiderActionDao, this);
    }

    public static SpiderActionDao getInstance() {
        return ApplicationManager.getApplication().getService(SpiderActionDao.class);
    }
}
