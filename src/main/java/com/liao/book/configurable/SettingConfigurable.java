package com.liao.book.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.liao.book.factory.BeanFactory;
import com.liao.book.ui.MainUI;
import com.liao.book.ui.SettingsUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
        System.out.println("SettingsUI");
        BeanFactory.setBean("SettingsUI", settingsUI);
        return settingsUI.getSettingWin();
    }

    @Override
    public boolean isModified() {
        return settingsUI.isModified();
    }

    @Override
    public void apply() {

        MainUI mainUI = (MainUI) BeanFactory.getBean("MainUI");
        SettingsUI settingsUI1 = (SettingsUI) BeanFactory.getBean("SettingsUI");

        JTextArea textContent = mainUI.getTextContent();

        JComboBox<Integer> fontSize = settingsUI1.getFontSize();

        int integer = Integer.parseInt(Objects.requireNonNull(fontSize.getSelectedItem()).toString());

        textContent.setFont(new Font("", Font.BOLD, integer));
    }
}
