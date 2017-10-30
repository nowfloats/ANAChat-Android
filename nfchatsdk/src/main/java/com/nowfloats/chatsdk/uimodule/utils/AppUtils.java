package com.nowfloats.chatsdk.uimodule.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

public class AppUtils {

    public static void showToast(Context context, @StringRes int text, boolean isLong) {
        showToast(context, context.getString(text), isLong);
    }

    public static void showToast(Context context, String text, boolean isLong) {
        Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static Boolean checkStringMinLen(String text, int len) {
        return text.length() > len || text.length() == len;
//        showToast(context, "Minimum Length should be " + len );
    }

    public static Boolean checkStringMaxLen(String text, int len) {
        return text.length() < len || text.length() == len;
    }

    public static Boolean checkMultiLineLen(int count, int len) {
        return count < len || count == len;
    }

    public static Boolean checkEmailString(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Boolean checkPhoneString(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static Boolean checkNumberString(String number) {
        return number.chars().allMatch(Character::isDigit);
    }

}