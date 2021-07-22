package com.liao.book.utile;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.sun.istack.Nullable;

public class ToastUtil {

    /**
     * 2020.3 之前版本弹窗
     *
     * @param project 页面
     * @param content 消息
     * @param type    类型
     */
    public static void notification2020_3Ago(@Nullable Project project, String content, MessageType type) {
        NotificationGroup custom_notification_group = new NotificationGroup("Custom Notification Group", NotificationDisplayType.BALLOON, true);
        Notification notification = custom_notification_group.createNotification(content, type);
        Notifications.Bus.notify(notification);
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
