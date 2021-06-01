package com.liao.book.utile;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.sun.istack.Nullable;

public class ToastUtil {


    /**
     * 全局Toast左下角窗口弹出事件
     *
     * @param content 文本
     */
    public static void toastPopUp(@Nullable Project project, String content) {
        /*try {
            Class.forName("com.intellij.notification.NotificationGroupManager");

            NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
                    .createNotification(content, NotificationType.WARNING).notify(project);

        } catch (ClassNotFoundException e) {

            NotificationGroup fisrtplugin_id = new NotificationGroup("fisrtplugin_id", NotificationDisplayType.BALLOON, true);
            Notification notification = fisrtplugin_id.createNotification(content, MessageType.INFO);
            Notifications.Bus.notify(notification);
        }*/

        NotificationGroup fisrtplugin_id = new NotificationGroup("fisrtplugin_id", NotificationDisplayType.BALLOON, true);
        Notification notification = fisrtplugin_id.createNotification(content, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
}
