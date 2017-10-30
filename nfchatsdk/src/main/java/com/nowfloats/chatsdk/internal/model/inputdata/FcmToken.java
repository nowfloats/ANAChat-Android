package com.nowfloats.chatsdk.internal.model.inputdata;

/**
 * Created by lookup on 20/09/17.
 */

public class FcmToken {

    private String deviceId;
    private String fcmNotificationId;
    private String devicePlatform;
//    private String userId;

    public FcmToken(String deviceId, String fcmNotificationId, String devicePlatform) {
        this.deviceId = deviceId;
        this.fcmNotificationId = fcmNotificationId;
        this.devicePlatform = devicePlatform;
//        this.userId = userId;
    }
}
