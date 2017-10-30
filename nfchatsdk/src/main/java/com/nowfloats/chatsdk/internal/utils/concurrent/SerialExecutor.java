package com.nowfloats.chatsdk.internal.utils.concurrent;

/**
 * Created by lookup on 19/10/17.
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SerialExecutor implements Executor {
    private final Queue<Runnable> tasks = new LinkedList<>();
    private final Executor executor;
    private Runnable active;
    private static SerialExecutor single;

    public static SerialExecutor getInstance() {
        if (single == null) {
            synchronized (SerialExecutor.class) {
                single = new SerialExecutor();
            }
        }
        return single;
    }

    private SerialExecutor() {
        executor = Executors.newFixedThreadPool(1);
    }

    public void execute(final Runnable r) {
        tasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}