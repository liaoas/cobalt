package com.cobalt.persistence;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.cobalt.common.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * 窗口设置持久化
 * </p>
 *
 * @author LiAo
 * @since 2023-03-01
 */
@State(name = "SettingsDao", storages = {@Storage(value = "cobalt.settings.dao.xml")})
public class SettingsDao implements PersistentStateComponent<SettingsDao> {

    public int fontSize = Constants.DEFAULT_FONT_SIZE;

    public int scrollSpacingScale = Constants.DEFAULT_READ_ROLL_SIZE;

    public SettingsDao() {
    }

    public SettingsDao(int fontSize, int scrollSpacingSize) {
        this.fontSize = fontSize;
        this.scrollSpacingScale = scrollSpacingSize;
    }

    @Override
    public @Nullable SettingsDao getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsDao windowsSettingDao) {
        XmlSerializerUtil.copyBean(windowsSettingDao, this);
    }

    public static SettingsDao getInstance() {
        return ApplicationManager.getApplication().getService(SettingsDao.class);
    }
}
