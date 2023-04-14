package com.liao.book.factory;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.liao.book.content.FullScreenUIContent;
import com.liao.book.content.MainUIContent;
import com.liao.book.content.ReadingHistoryUIContent;
import com.liao.book.ui.FullScreenUI;
import com.liao.book.ui.MainUI;
import com.liao.book.ui.ReadingHistoryUI;
import org.jetbrains.annotations.NotNull;

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
}
