package com.anachat.chatsdk.internal.utils.concurrent;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerThreadExecutor implements ApiExecutor {
    private Handler handler;
    private Handler uiHandler;
    private final Object syncLock = new Object();
    private ExecutorService service;

    private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private int KEEP_ALIVE_TIME = 1;
    private TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();

    public HandlerThreadExecutor(String name) {
        HandlerThread handlerThread = new HandlerThread(name);
        handlerThread.start();
        service = new ThreadPoolExecutor(NUMBER_OF_CORES,
                NUMBER_OF_CORES * 2,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                taskQueue);
        this.handler = new Handler(handlerThread.getLooper());
        this.uiHandler = new Handler(Looper.getMainLooper());
    }

    public void runAsync(Runnable r) {
        this.handler.post(r);
    }

    public void runUiWithDelay(Runnable var1) {
        this.uiHandler.postDelayed(var1, 2000);
    }

    public void runOnUiThread(final Runnable r) {
        this.runAsync(new Runnable() {
            public void run() {
                HandlerThreadExecutor.this.uiHandler.post(r);
            }
        });
    }


    public void submitToPool(final Runnable r) {
        service.execute(r);
    }

    public void runSync(Runnable r) {
        RunnableUtil.NotifyingRunnable runnable = new RunnableUtil.NotifyingRunnable(r);
        Object var3 = this.syncLock;
        synchronized (this.syncLock) {
            this.handler.post(runnable);
            runnable.waitForCompletion();
        }
    }

    public <T> RunnableUtil.RunnableFuture<T> callAndReturn(RunnableUtil.ValueRunnable<T> valueRunnable) {
        RunnableUtil.NotifyingRunnable runnable = new RunnableUtil.NotifyingRunnable(valueRunnable);
        RunnableUtil.RunnableFuture<T> result = new RunnableUtil.RunnableFuture<>(valueRunnable, runnable);
        this.handler.post(runnable);
        return result;
    }
}
