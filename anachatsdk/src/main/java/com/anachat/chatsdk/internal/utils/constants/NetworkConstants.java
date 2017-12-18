package com.anachat.chatsdk.internal.utils.constants;

/**
 * Created by lookup on 09/10/17.
 */

public class NetworkConstants {
    public static final int CONNECT_TIMEOUT = 5000;
    public static final int UPLOAD_CONNECT_TIMEOUT = 60000;
    public static final int SOCKET_TIMEOUT = 5000;
    public static final int POOL_SHUTDOWN_TIMEOUT = 60;
    public static final String apiVersion = "2";
    public static final String apiBase = "/api/lib/";
    public static String scheme = "https://";
    public static final String contentType = "application/x-www-form-urlencoded";
    public static int DEFAULT_POOL_SIZE = 4096;
    public static int DEFAULT_REQUEST_MAX_SIZE = 8;

    public NetworkConstants() {
    }
}
