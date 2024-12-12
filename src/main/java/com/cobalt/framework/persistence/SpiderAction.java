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
 * 爬虫行为描述资源持久化
 * </p>
 *
 * @author LiAo
 * @since 2024-01-15
 */
@State(name = "SpiderActionDao", storages = {@Storage(value = "cobalt.persistent.xml")})
public class SpiderAction implements PersistentStateComponent<SpiderAction> {

    public String spiderActionStr = "{}";

    private static SpiderAction instance = null;

    public SpiderAction() {
    }

    public SpiderAction(String spiderActionStr) {
        this.spiderActionStr = spiderActionStr;
    }

    public void loadState() {
        loadState(getInstance());
    }

    @Override
    public @Nullable SpiderAction getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SpiderAction spiderActionDao) {
        XmlSerializerUtil.copyBean(spiderActionDao, this);
    }

    public static SpiderAction getInstance() {
        if (instance == null) {
            instance = ApplicationManager.getApplication().getService(SpiderAction.class);
        }
        return instance;
    }
}
