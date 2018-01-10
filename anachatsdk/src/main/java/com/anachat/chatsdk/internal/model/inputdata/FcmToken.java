package com.anachat.chatsdk.internal.model.inputdata;

/**
 * Created by lookup on 20/09/17.
 */

public class FcmToken {

    private String deviceId;
    private String fcmNotificationId;
    private String devicePlatform;
    private String businessId;
    private String userId;

    public FcmToken(String deviceId, String fcmNotificationId, String devicePlatform
            , String businessId, String userId) {
        this.deviceId = deviceId;
        this.fcmNotificationId = fcmNotificationId;
        this.devicePlatform = devicePlatform;
        this.businessId = businessId;
        if (userId != null && !userId.isEmpty())
            this.userId = userId;
    }
}
