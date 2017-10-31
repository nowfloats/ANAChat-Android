package com.anachat.chatsdk.internal.utils.concurrent;


public class ApiExecutorFactory {
    public ApiExecutorFactory() {
    }

    public static ApiExecutor getHandlerExecutor() {
        return ApiExecutorFactory.LazyHolder.HANDLER_EXECUTOR;

    }

    private static class LazyHolder {
        private static final ApiExecutor HANDLER_EXECUTOR = new HandlerThreadExecutor("NF-cm-api-exec");

        private LazyHolder() {
        }
    }
}
