package com.cobalt.framework.persistence.proxy;

import com.cobalt.framework.persistence.SpiderAction;

/**
 * 爬虫行为描述资源持久化类的代理类
 * 与业务代码进行解耦
 *
 * @author LiAo
 * @since 2024/12/12
 */
public class SpiderActionProxy {

    private final SpiderAction instance;

    public SpiderActionProxy() {
        this.instance = SpiderAction.getInstance();
    }

    public String getSpiderActionStr() {
        return this.instance.spiderActionStr;
    }

    public void setSpiderActionStr(String spiderActionStr) {
        this.instance.spiderActionStr = spiderActionStr;
        this.loadState();
    }

    private void loadState() {
        this.instance.loadState();
    }
}
