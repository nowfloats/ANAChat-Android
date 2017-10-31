package com.anachat.chatsdk.internal.utils.concurrent;

import android.content.Context;

import com.anachat.chatsdk.internal.database.MessageRepository;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.internal.utils.ListenerManager;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by lookup on 26/10/17.
 */

public class PushConsumer implements Runnable {
    private static PushConsumer instance;
    private Thread thread;

    //    private List<MessageResponse> messageResponses = new ArrayList<>();
    private Context context;
    private PriorityQueue<MessageResponse> queue = new PriorityQueue<>(5, new Checker());

    public static PushConsumer getInstance(Context context) {
        if (instance == null) { // first time lock
            synchronized (ListenerManager.class) {
                if (instance == null) {  // second time lock
                    instance = new PushConsumer(context);
                }
            }
        }
        return instance;
    }

    private PushConsumer(Context context) {
        this.context = context;
    }

    public void addTask(MessageResponse messageResponse) {
        queue.add(messageResponse);
        if (thread == null) {
            thread = new Thread(this);
        }
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    private void executeQueue() {
        while (queue.size() > 0) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MessageRepository messageRepository =
                    MessageRepository.getInstance(context);
            MessageResponse messageResponse = queue.peek();
            queue.remove(messageResponse);
            messageRepository.handleMessageResponse(messageResponse);
        }
        thread = null;
    }

    @Override
    public void run() {
        executeQueue();
    }


    class Checker implements Comparator<MessageResponse> {
        public int compare(MessageResponse str1, MessageResponse str2) {
            return Long.compare(str1.getMessage().getTimestamp(),
                    str2.getMessage().getTimestamp());
        }
    }
}
