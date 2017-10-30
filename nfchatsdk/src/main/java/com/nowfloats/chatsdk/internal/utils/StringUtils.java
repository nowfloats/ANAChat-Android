package com.nowfloats.chatsdk.internal.utils;

/**
 * Created by lookup on 09/10/17.
 */


public class StringUtils {
    public StringUtils() {
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }
}
