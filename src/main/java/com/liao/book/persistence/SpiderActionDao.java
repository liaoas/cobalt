package com.liao.book.persistence;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

/**
 * <p>
 *  持久化爬虫动作描述资源
 * </p>
 *
 * @author LiAo
 * @since 2024-01-15
 */
@State(name = "SpiderActionDao", storages = {@Storage(value = "idea.ebook.settings.dao.xml")})
public class SpiderActionDao {
}
