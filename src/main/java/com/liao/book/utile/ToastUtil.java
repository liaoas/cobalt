package com.liao.book.utile;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;

public class ToastUtil {


    /**
     * 全局Toast左下角窗口弹出事件
     *
     * @param str 文本
     */
    public static void toastPopUp(String str) {
        NotificationGroup fisrtplugin_id = new NotificationGroup("fisrtplugin_id", NotificationDisplayType.BALLOON, true);
        Notification notification = fisrtplugin_id.createNotification(str, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
}
