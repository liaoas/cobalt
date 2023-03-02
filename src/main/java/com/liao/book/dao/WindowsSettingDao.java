package com.liao.book.dao;

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
@State(name = "WindowsSettingDao",
        storages = {
                @Storage(value = "windows.setting.dao.xml"
                )
        }
)
public class WindowsSettingDao implements PersistentStateComponent<WindowsSettingDao> {

    public int fontSize = 16;

    public int scrollSpacingScale;

    public WindowsSettingDao() {
    }

    public WindowsSettingDao(int fontSize, int scrollSpacingSize) {
        this.fontSize = fontSize;
        this.scrollSpacingScale = scrollSpacingSize;
    }

    @Override
    public @Nullable
    WindowsSettingDao getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull WindowsSettingDao windowsSettingDao) {
        XmlSerializerUtil.copyBean(windowsSettingDao, this);
    }

    public static WindowsSettingDao getInstance() {
        return ApplicationManager.getApplication().getService(WindowsSettingDao.class);
    }
}
