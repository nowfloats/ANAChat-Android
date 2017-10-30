package com.nowfloats.chatsdk.internal.utils.concurrent;

import android.util.Log;

public class RunnableUtil {
    public RunnableUtil() {
    }

    public static class RunnableFuture<T> {
        private final RunnableUtil.ValueRunnable<T> runnable;
        private final RunnableUtil.NotifyingRunnable notifyingRunnable;

        RunnableFuture(RunnableUtil.ValueRunnable<T> r, RunnableUtil.NotifyingRunnable notifyingRunnable) {
            this.runnable = r;
            this.notifyingRunnable = notifyingRunnable;
        }

        public T get() {
            this.notifyingRunnable.waitForCompletion();
            return this.runnable.runnableValue;
        }
    }

    public abstract static class ValueRunnable<T> implements Runnable {
        public T runnableValue;

        public ValueRunnable() {
        }
    }

    public static class NotifyingRunnable implements Runnable {
        private static final String TAG = "Helpshift_NotiRunnable";
        private final Runnable runnable;
        private boolean isFinished;
        private final Object syncLock = new Object();

        NotifyingRunnable(Runnable r) {
            this.runnable = r;
        }

        public void waitForCompletion() {
            Object var1 = this.syncLock;
            synchronized (this.syncLock) {
                try {
                    if (!this.isFinished) {
                        this.syncLock.wait();
                    }
                } catch (InterruptedException var4) {
                    Log.d("Nf_NotiRunnable", "Exception in NotifyingRunnable", var4);
                }

            }
        }

        public void run() {
            Object var1 = this.syncLock;
            synchronized (this.syncLock) {
                try {
                    this.runnable.run();
                    this.isFinished = true;
                } finally {
                    this.syncLock.notifyAll();
                }

            }
        }
    }
}
