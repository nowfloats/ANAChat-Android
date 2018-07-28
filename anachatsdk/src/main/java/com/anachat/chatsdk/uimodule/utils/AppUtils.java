package com.anachat.chatsdk.uimodule.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.anachat.chatsdk.library.R;

import java.io.File;

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
        return (number.matches("\\d*\\.?\\d+"));
//            return TextUtils.isDigitsOnly(number);
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    public static boolean memoryOutofBounds (String fileName , Context context) {
       return checkIfException(new File(fileName) , context);
    }

    private static boolean checkIfException (File f , Context context){
        final long fileUploadLimitSize = 12000000; // 12 mb file size is max
        return  f.length() > fileUploadLimitSize ? showToast(context,R.string.file_big_exception)  : false;
    }

    private static boolean showToast(Context context , int message ) {
        showToast(context , message , true);
        return true;
    }
}