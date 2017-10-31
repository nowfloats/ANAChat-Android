package com.anachat.chatsdk.internal.utils.concurrent;


public interface ApiExecutor {
    void runAsync(Runnable var1);

    void runUiWithDelay(Runnable var1);

    void runSync(Runnable var1);

    void runOnUiThread(Runnable var1);

    void submitToPool(Runnable var1);

    <T> RunnableUtil.RunnableFuture<T> callAndReturn(RunnableUtil.ValueRunnable<T> var1);
}
