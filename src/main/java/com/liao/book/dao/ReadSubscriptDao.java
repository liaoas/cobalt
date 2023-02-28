package com.liao.book.dao;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 *
 * </p>
 *
 * @author LiAo
 * @since 2023-02-28
 */
@State(name = "ReadSubscriptDao",
        storages = {
                @Storage(value = "reading.subscript.dao.xml",
                        roamingType = RoamingType.DEFAULT)
        }
)
public class ReadSubscriptDao implements PersistentStateComponent<ReadSubscriptDao> {

    public int homeTextWinIndex;

    public ReadSubscriptDao() {
    }

    public ReadSubscriptDao(int homeTextWinIndex) {
        this.homeTextWinIndex = homeTextWinIndex;
    }

    @Override
    public @Nullable ReadSubscriptDao getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ReadSubscriptDao readSubscriptDao) {
        XmlSerializerUtil.copyBean(readSubscriptDao, this);
    }

    public static ReadSubscriptDao getInstance() {
        return ApplicationManager.getApplication().getService(ReadSubscriptDao.class);
    }
}
