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
public class ReadSubscriptPersistent implements PersistentStateComponent<ReadSubscriptPersistent> {

    public int homeTextWinIndex;

    public ReadSubscriptPersistent() {
    }

    public ReadSubscriptPersistent(int homeTextWinIndex) {
        this.homeTextWinIndex = homeTextWinIndex;
    }

    @Override
    public @Nullable
    ReadSubscriptPersistent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ReadSubscriptPersistent readSubscriptDao) {
        XmlSerializerUtil.copyBean(readSubscriptDao, this);
    }

    public static ReadSubscriptPersistent getInstance() {
        return ApplicationManager.getApplication().getService(ReadSubscriptPersistent.class);
    }
}
