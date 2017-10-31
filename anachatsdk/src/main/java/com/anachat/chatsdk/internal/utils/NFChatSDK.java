package com.anachat.chatsdk.internal.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by lookup on 06/09/17.
 */

public class NFChatSDK {
    private static Looper mMainThreadLooper = null;
    private static Handler mMainThreadHandler = null;
    private static int mMainThreadId;
    private static Thread mMainThread = null;
    private static Context mApplication;
    private static NFChatSDK mNFChatSDK;

    private NFChatSDK() {
    }

    public static NFChatSDK instance() {
        if (mNFChatSDK == null) {
            synchronized (NFChatSDK.class) {
                if (mApplication == null) {
                    mNFChatSDK = new NFChatSDK();
                }
            }
        }
        return mNFChatSDK;
    }

    public void initialize(Context application) {
        mApplication = application;
        if (mApplication != null) {
            mMainThreadLooper = mApplication.getMainLooper();
            mMainThreadHandler = new Handler();
            mMainThreadId = android.os.Process.myTid();
            mMainThread = Thread.currentThread();
        }
    }

    public static Context getApplication() {
        if (mApplication == null)
            throw new NullPointerException("mApplication is null");
        return mApplication;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

}
