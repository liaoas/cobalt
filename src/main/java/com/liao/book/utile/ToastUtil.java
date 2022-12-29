package com.liao.book.utile;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.liao.book.config.ProjectConfig;
import com.liao.book.enums.ToastType;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * Toast 弹出函数
 * </p>
 *
 * @author LiAo
 * @since 2022/12/29
 */
public class ToastUtil {

    /**
     * 控制不同版本的消息发送类型
     *
     * @param project   窗口对象
     * @param content   消息内容
     * @param toastType 消息类型
     */
    public static void showToastMassage(@Nullable Project project, String content, ToastType toastType) {
        String is_notification_2020_3 = ProjectConfig.getConfigValue("is_notification_2020_3");

        if (StringUtil.isNotEmpty(is_notification_2020_3) && is_notification_2020_3.equals("true")) {
            // 使用2020.3 及其以后版本弹窗
            switch (toastType) {
                case ERROR:
                    notification2020_3Rear(project, content, NotificationType.ERROR);
                    break;
                case INFO:
                    notification2020_3Rear(project, content, NotificationType.INFORMATION);
                    break;
                case WARNING:
                    notification2020_3Rear(project, content, NotificationType.WARNING);
                    break;
                default:
                    break;
            }
        } else {
            // 使用2020.3 之前版本弹窗
            switch (toastType) {
                case ERROR:
                    notification2020_3Ago(project, content, MessageType.ERROR);
                    break;
                case INFO:
                    notification2020_3Ago(project, content, MessageType.INFO);
                    break;
                case WARNING:
                    notification2020_3Ago(project, content, MessageType.WARNING);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 2020.3 之前版本弹窗
     *
     * @param project 页面
     * @param content 消息
     * @param type    类型
     */
    public static void notification2020_3Ago(@Nullable Project project, String content, MessageType type) {
        /*NotificationGroup custom_notification_group = new NotificationGroup(
                "Custom Notification Group", NotificationDisplayType.BALLOON, true);
        Notification notification = custom_notification_group.createNotification(content, type);
        Notifications.Bus.notify(notification);*/
    }

    /**
     * 2020.3 以后版本弹窗
     *
     * @param project 页面
     * @param content 消息
     * @param type    类型
     */
    public static void notification2020_3Rear(@Nullable Project project, String content, NotificationType type) {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                .createNotification(content, type)
                .notify(project);
    }
}
