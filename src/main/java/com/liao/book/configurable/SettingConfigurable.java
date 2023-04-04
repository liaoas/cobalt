package com.liao.book.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.liao.book.ui.SettingsUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 设置窗口 Configurable
 *
 * @author LiAo
 * @since 2023-04-03
 */
public class SettingConfigurable implements Configurable {

    private SettingsUI settingsUI = new SettingsUI();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "LiAo";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return settingsUI.getSettingWin();
    }

    @Override
    public boolean isModified() {
        return settingsUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
