package com.liao.book.window;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author LiAo
 * @since 2023-04-03
 */
public class IdeaEbook implements Configurable {

    private IdeaEbookSettings ideaEbookSettings = new IdeaEbookSettings();
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return null;
    }

    @Override
    public @Nullable JComponent createComponent() {
        JPanel panel1 = ideaEbookSettings.getPanel1();
        return panel1;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        System.out.println("apply");
    }
}
