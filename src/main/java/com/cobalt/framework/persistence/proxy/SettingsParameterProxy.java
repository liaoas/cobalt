package com.cobalt.framework.persistence.proxy;

import com.cobalt.framework.persistence.SettingsParameter;

/**
 * 窗口设置持久化类的代理类
 * 与业务代码进行解耦
 *
 * @author LiAo
 * @since 2024/12/12
 */
public class SettingsParameterProxy {

    private final SettingsParameter instance;

    public SettingsParameterProxy() {
        this.instance = SettingsParameter.getInstance();
    }

    public int getFontSize() {
        return this.instance.fontSize;
    }

    public void setFontSize(int fontSize) {
        this.instance.fontSize = fontSize;
        this.loadState();
    }

    public int getScrollSpacingScale() {
        return this.instance.scrollSpacingScale;
    }

    public void setScrollSpacingScale(int scrollSpacingScale) {
        this.instance.scrollSpacingScale = scrollSpacingScale;
        this.loadState();
    }

    public int getSplitPosition() {
        return this.instance.splitPosition;
    }

    public void setSplitPosition(int splitPosition) {
        this.instance.splitPosition = splitPosition;
        this.loadState();
    }

    private void loadState() {
        this.instance.loadState();
    }
}
