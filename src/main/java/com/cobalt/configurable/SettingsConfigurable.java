package com.cobalt.configurable;

import com.cobalt.factory.BeanFactory;
import com.cobalt.ui.FullScreenUI;
import com.cobalt.ui.MainUI;
import com.cobalt.ui.SettingsUI;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 设置窗口 Configurable
 *
 * @author LiAo
 * @since 2023-04-03
 */
public class SettingsConfigurable implements Configurable {

    /**
     * 创建自定义设置页面UI
     */
    public static SettingsUI settingsUI = null;

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public @Nullable JComponent createComponent() {

        settingsUI = new SettingsUI();

        // 状态改为未修改
        settingsUI.setModified(false);

        // 加载持久化状态
        settingsUI.loadPersistentState();

        BeanFactory.setBean("SettingsUI", settingsUI);

        return settingsUI.getSettingWin();
    }

    @Override
    public boolean isModified() {
        return settingsUI.isModified() || settingsUI.isSelBook();
    }

    @Override
    public void apply() {

        settingsUI.apply();

        MainUI mainUI = (MainUI) BeanFactory.getBean("MainUI");
        mainUI.apply();

        FullScreenUI fullScreenUI = (FullScreenUI) BeanFactory.getBean("FullScreenUI");
        fullScreenUI.apply();

    }
}
