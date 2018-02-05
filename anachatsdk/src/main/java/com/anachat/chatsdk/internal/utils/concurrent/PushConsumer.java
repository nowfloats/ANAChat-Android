package com.anachat.chatsdk.internal.utils.concurrent;

import android.content.Context;

import com.anachat.chatsdk.internal.database.MessageRepository;
import com.anachat.chatsdk.internal.model.MessageResponse;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by lookup on 26/10/17.
 */

public class PushConsumer {
    private static volatile PushConsumer instance;
    private Boolean threadRunning = false;
    //    private List<MessageResponse> messageResponses = new ArrayList<>();
//    private Context context;
    private PriorityQueue<MessageResponse> queue = new PriorityQueue<>(5, new Checker());

    public static PushConsumer getInstance() {
        if (instance == null) { // first time lock
            synchronized (PushConsumer.class) {
                if (instance == null) {  // second time lock
                    instance = new PushConsumer();
                }
            }
        }
        return instance;
    }

    private PushConsumer() {

    }

    public void addTask(MessageResponse messageResponse, Context context) {
        queue.add(messageResponse);
        startConsumer(context);
    }


    private void startConsumer(Context context) {
        if (threadRunning) return;
        ApiExecutor apiExecutor = ApiExecutorFactory.getHandlerExecutor();
        apiExecutor.submitToPool(() -> executeQueue(context));
    }

    private void executeQueue(Context context) {
        threadRunning = true;
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
        threadRunning = false;
    }

    class Checker implements Comparator<MessageResponse> {
        public int compare(MessageResponse str1, MessageResponse str2) {
            return Long.compare(str1.getMessage().getTimestamp(),
                    str2.getMessage().getTimestamp());
        }
    }
}
