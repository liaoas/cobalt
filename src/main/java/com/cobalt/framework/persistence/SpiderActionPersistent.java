package com.cobalt.framework.persistence;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * 持久化爬虫动作描述资源
 * </p>
 *
 * @author LiAo
 * @since 2024-01-15
 */
@State(name = "SpiderActionDao", storages = {@Storage(value = "cobalt.persistent.xml")})
public class SpiderActionPersistent implements PersistentStateComponent<SpiderActionPersistent> {

    public String spiderActionStr = "{}";

    public SpiderActionPersistent() {
    }

    public SpiderActionPersistent(String spiderActionStr) {
        this.spiderActionStr = spiderActionStr;
    }


    @Override
    public @Nullable SpiderActionPersistent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SpiderActionPersistent spiderActionDao) {
        XmlSerializerUtil.copyBean(spiderActionDao, this);
    }

    public static SpiderActionPersistent getInstance() {
        return ApplicationManager.getApplication().getService(SpiderActionPersistent.class);
    }
}
