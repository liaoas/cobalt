package com.cobalt.framework.persistence;

import com.cobalt.common.constant.Constants;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
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
@State(name = "SettingsDao", storages = {@Storage(value = "cobalt.persistent.xml")})
public class SettingsParameter implements PersistentStateComponent<SettingsParameter> {

    public int fontSize = Constants.DEFAULT_FONT_SIZE;

    public int scrollSpacingScale = Constants.DEFAULT_READ_ROLL_SIZE;

    public int splitPosition = Constants.DEFAULT_MAIN_SPLIT_POSITION;

    private static SettingsParameter instance = null;

    public SettingsParameter() {
    }

    public SettingsParameter(int fontSize, int scrollSpacingSize, int splitPosition) {
        this.fontSize = fontSize;
        this.scrollSpacingScale = scrollSpacingSize;
        this.splitPosition = splitPosition;
    }

    public void loadState() {
        loadState(getInstance());
    }

    @Override
    public @Nullable SettingsParameter getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsParameter windowsSettingDao) {
        XmlSerializerUtil.copyBean(windowsSettingDao, this);
    }

    public static SettingsParameter getInstance() {
        if (instance == null) {
            instance = ApplicationManager.getApplication().getService(SettingsParameter.class);
        }
        return instance;
    }
}
