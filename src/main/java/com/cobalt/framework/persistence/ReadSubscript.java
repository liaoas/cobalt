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
 * 阅读窗口滚动位置持久化
 * </p>
 *
 * @author LiAo
 * @since 2023-02-28
 */
@State(name = "ReadSubscriptDao", storages = {@Storage(value = "cobalt.persistent.xml")})
public class ReadSubscript implements PersistentStateComponent<ReadSubscript> {

    public int homeTextWinIndex;

    private static ReadSubscript instance = null;

    public ReadSubscript() {
    }

    public ReadSubscript(int homeTextWinIndex) {
        this.homeTextWinIndex = homeTextWinIndex;
    }

    public void loadState() {
        loadState(getInstance());
    }

    @Override
    public @Nullable
    ReadSubscript getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ReadSubscript readSubscriptDao) {
        XmlSerializerUtil.copyBean(readSubscriptDao, this);
    }

    public static ReadSubscript getInstance() {
        if (instance == null) {
            instance = ApplicationManager.getApplication().getService(ReadSubscript.class);
        }
        return instance;
    }
}
