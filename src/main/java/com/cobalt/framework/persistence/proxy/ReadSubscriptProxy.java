package com.cobalt.framework.persistence.proxy;

import com.cobalt.framework.persistence.ReadSubscript;

/**
 * 阅读窗口滚动位置持久化类的代理类
 * 与业务代码进行解耦
 *
 * @author LiAo
 * @since 2024/12/12
 */
public class ReadSubscriptProxy {

    private final ReadSubscript instance;

    public ReadSubscriptProxy() {
        instance = ReadSubscript.getInstance();
    }

    public int getHomeTextWinIndex() {
        return this.instance.homeTextWinIndex;
    }

    public void setHomeTextWinIndex(int homeTextWinIndex) {
        this.instance.homeTextWinIndex = homeTextWinIndex;
        this.loadState();
    }

    private void loadState() {
        this.instance.loadState();
    }
}
