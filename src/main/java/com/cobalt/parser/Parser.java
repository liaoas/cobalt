package com.cobalt.parser;

/**
 * 插件解析抽象类基类
 *
 * @author LiAo
 * @since 2024/7/19
 */
public interface Parser {

    /**
     * 插件解析抽象方法
     *
     * @param resource 资源
     * @return 结果
     */
    boolean parser(Object resource);
}
