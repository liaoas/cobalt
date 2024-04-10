package com.cobalt.framework.factory;

import com.cobalt.config.ProjectConfig;
import com.cobalt.content.FullScreenUIContent;
import com.cobalt.content.MainUIContent;
import com.cobalt.content.ReadingHistoryUIContent;
import com.cobalt.framework.persistence.SpiderActionPersistent;
import com.cobalt.ui.FullScreenUI;
import com.cobalt.ui.MainUI;
import com.cobalt.ui.ReadingHistoryUI;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.rabbit.foot.core.github.GitHubFileReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 视图工厂，用于加载窗口信息
 * </p>
 *
 * @author LiAo
 * @since 2023-04-14
 */
public class ViewFaction implements ToolWindowFactory, DumbAware {

    private final static Logger log = LoggerFactory.getLogger(ViewFaction.class);

    // 页面设置持久化
    static SpiderActionPersistent spiderActionDao = SpiderActionPersistent.getInstance();

    /**
     * 创建窗口容器
     *
     * @param project    项目
     * @param toolWindow 窗口
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        new MainUIContent().createContent(project, toolWindow);

        new FullScreenUIContent().createContent(project, toolWindow);

        new ReadingHistoryUIContent().createContent(project, toolWindow);

        // 加载全局Button组件鼠标悬浮样式
        loadOverallComponentStyle();

        // 加载爬虫配置文件，回填组件
        initSpiderConfig();
    }

    /**
     * 加载全局的组件样式
     */
    private void loadOverallComponentStyle() {
        List<Component> components = getComponents();

        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;

                button.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        // 鼠标进入组件时设置背景色
                        button.setContentAreaFilled(true);
                    }

                    public void mouseExited(MouseEvent e) {
                        // 鼠标离开组件时恢复背景色
                        button.setContentAreaFilled(false);
                    }
                });
            }
        }
    }

    /**
     * 获取全局页面JPanel对象
     *
     * @return JPanel集合
     */
    private List<Component> getComponents() {
        MainUI mainUI = (MainUI) BeanFactory.getBean("MainUI");
        FullScreenUI fullScreenUI = (FullScreenUI) BeanFactory.getBean("FullScreenUI");
        ReadingHistoryUI readingHistoryUI = (ReadingHistoryUI) BeanFactory.getBean("ReadingHistoryUI");

        List<Component> components = new ArrayList<>(16);

        JPanel bookMainJPanel = mainUI.getMainPanel();
        JPanel fullScreenPanel = fullScreenUI.getFullScreenPanel();
        JPanel readingHistoryPanel = readingHistoryUI.getReadingHistoryPanel();

        components.addAll(Arrays.asList(bookMainJPanel.getComponents()));
        components.addAll(Arrays.asList(fullScreenPanel.getComponents()));
        components.addAll(Arrays.asList(readingHistoryPanel.getComponents()));

        List<Component> returnList = new ArrayList<>(16);

        // 递归出每个组件
        recursionComponent(components, returnList);

        return returnList;
    }

    /**
     * 递归出每个 Component
     *
     * @param l1 页面外层 Component
     * @param l2 页面所有的 Component
     */
    private void recursionComponent(List<Component> l1, List<Component> l2) {

        for (Component component : l1) {
            if (component instanceof JPanel) {

                JPanel panel = (JPanel) component;
                recursionComponent(Arrays.asList(panel.getComponents()), l2);
            }
            l2.add(component);
        }
    }


    /**
     * 加载爬虫喷子h
     */
    public static void initSpiderConfig() {

        log.info("读取 persistence 为空......");

        if (!spiderActionDao.spiderActionStr.equals("{}")) return;

        // 加载远程配置中心配置
        spiderActionDao.spiderActionStr = loadGitHubConfig();

        spiderActionDao.loadState(spiderActionDao);
    }

    /**
     * 加载GitHub配置，并持久化
     */
    public static String loadGitHubConfig() {

        String configValue = null;

        String owner = ProjectConfig.getConfigValue("spider_config_owner");
        String repo = ProjectConfig.getConfigValue("spider_config_repo");
        String path = ProjectConfig.getConfigValue("spider_config_path");

        try {

            configValue = GitHubFileReader.getFileContent(owner, repo, path);

            log.info("爬虫资源获取成功->{}...", configValue.substring(0, 200));
        } catch (Exception exception) {
            log.error("从目标网站加载配置文件失败...... url->{};owner->{};repo->{};path->{}", GitHubFileReader.GITHUB_API_URL, owner, repo, path);
        }

        if (configValue == null || configValue.isEmpty()) {
            log.error("爬虫资源获取为空");
            throw new RuntimeException("爬虫资源获取为空");
        }

        return configValue;
    }
}
