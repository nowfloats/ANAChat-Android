package com.anachat.chatsdk.internal.utils.constants;


import android.os.Environment;

public class Constants {

    public static final boolean isLogEnabled = true;
    public static final boolean isProduction = false;
    public static final String MESSAGE_RESPONSE = "message_response";
    public static String SEND_PATH = Environment
            .getExternalStorageDirectory() + "/Lookup/send/";
    public static final long HISTORY_MESSAGES_LIMIT = 20;
    public class FCMConstants {
        public static final String RESPONSE = "response";
        public static final String DEVICE_ID = "deviceId";
        public static final String NOTIFICATION_ID = "fcmNotificationId";
        public static final String DEVICE_PLATFORM = "devicePlatform";
        public static final String USER_ID = "userId";
        public static final String LINKS = "links";
        public static final String HREF = "href";
        public static final int MANDATORY_TRUE = 1;
        public static final int MANDATORY_FALSE = 0;
    }

    public class InputType {
        public static final int LOCATION = 7;
        public static final int DATE = 5;
        public static final int NUMERIC = 1;
        public static final int PHONE = 3;
        public static final int ADDRESS = 4;
        public static final int MEDIA = 8;
        public static final int TEXT = 0;
        public static final int TIME = 6;
        public static final int EMAIL = 2;
        public static final int OPTIONS = 10;
        public static final int LIST = 9;
    }

    public class MessagesTypeForUI {
        public static final int LOCATION = 106;
        public static final int DATE = 104;
        public static final int NUMERIC = 100;
        public static final int PHONE = 102;
        public static final int ADDRESS = 103;
        public static final int MEDIA = 107;
        public static final int TEXT = 99;
        public static final int TIME = 105;
        public static final int EMAIL = 101;
        public static final int OPTIONS = 109;
        public static final int LIST = 108;
        public static final int SIMPLE_MESSAGE_TEXT = 110;
        public static final int SIMPLE_MESSAGE_MEDIA = 111;
        public static final int CAROUSEL_MESSAGE = 112;
        public static final int BLANK_MESSAGE = 113;
        public static final int LOADING = 114;
        public static final int CAROUSEL_MESSAGE_FOR_INPUT = 115;
    }

    public class SenderType {
        public static final int AGENT = 3;
        public static final int ANA = 1;
        public static final int AI = 2;
        public static final int USER = 0;
    }

    public class MediaType {
        public static final int IMAGE = 0;
        public static final int AUDIO = 1;
        public static final int VIDEO = 2;
        public static final int FILE = 3;
    }

    public class MessageType {
        public static final int CAROUSEL = 1;
        public static final int SIMPLE = 0;
        public static final int INPUT = 2;
        public static final int EXTERNAL = 3;
    }

    public class UIParams {
        public static final String ToolBar_Tittle = "tittle_toolbar";
        public static final String ToolBar_Tittle_Desc = "desc_tittle_toolbar";
        public static final String ToolBar_Image = "image_toolbar";
        public static final String Theme_color = "theme_color_app";
    }
}
