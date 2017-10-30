/*******************************************************************************
 * Copyright 2016 stfalcon.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.nowfloats.chatsdk.uimodule.chatuikit.messages;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.nowfloats.chatsdk.internal.model.IMessage;
import com.nowfloats.chatsdk.uimodule.chatuikit.commons.ImageLoader;
import com.nowfloats.chatsdk.uimodule.chatuikit.commons.ViewHolder;
import com.nowfloats.chatsdk.uimodule.chatuikit.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Adapter for {@link MessagesList}.
 */
@SuppressWarnings("WeakerAccess")
public class MessagesListAdapter<MESSAGE extends IMessage>
        extends RecyclerView.Adapter<ViewHolder>
        implements RecyclerScrollMoreListener.OnLoadMoreListener {

    private MessageHolders holders;
    private String senderId;
    private List<Wrapper> items;

    private int selectedItemsCount;
    private SelectionListener selectionListener;

    protected static boolean isSelectionModeEnabled;

    private OnLoadMoreListener loadMoreListener;
    private OnMessageClickListener<MESSAGE> onMessageClickListener;
    private OnMessageViewClickListener<MESSAGE> onMessageViewClickListener;
    private OnMessageLongClickListener<MESSAGE> onMessageLongClickListener;
    private OnMessageViewLongClickListener<MESSAGE> onMessageViewLongClickListener;
    private ImageLoader imageLoader;
    private RecyclerView.LayoutManager layoutManager;
    //    private MessagesListStyle messagesListStyle;
    private DateFormatter.Formatter dateHeadersFormatter;
    private SparseArray<OnMessageViewClickListener> viewClickListenersArray = new SparseArray<>();

    /**
     * For default list item layout and view holder.
     *
     * @param senderId    identifier of sender.
     * @param imageLoader image loading method.
     */
    public MessagesListAdapter(String senderId, ImageLoader imageLoader) {
        this(senderId, new MessageHolders(), imageLoader);
    }

    /**
     * For default list item layout and view holder.
     *
     * @param senderId    identifier of sender.
     * @param holders     custom layouts and view holders. See {@link MessageHolders} documentation for details
     * @param imageLoader image loading method.
     */
    public MessagesListAdapter(String senderId, MessageHolders holders,
                               ImageLoader imageLoader) {
        this.senderId = senderId;
        this.holders = holders;
        this.imageLoader = imageLoader;
        this.items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return holders.getHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wrapper wrapper = items.get(position);
        holders.bind(holder, wrapper.item, wrapper.isSelected, imageLoader,
                getMessageClickListener(wrapper),
                getMessageLongClickListener(wrapper),
                dateHeadersFormatter,
                viewClickListenersArray);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return holders.getViewType(items.get(position).item, senderId);
    }

    @Override
    public void onLoadMore(int page, int total) {
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore(page, total);
        }
    }


    /*
    * PUBLIC METHODS
    * */

    /**
     * Adds message to bottom of list and scroll if needed.
     *
     * @param message message to add.
     * @param scroll  {@code true} if need to scroll list to bottom when message added.
     */
    public void addToStart(MESSAGE message, boolean scroll) {
        checkRemoveLoadingIfExist();
        boolean isNewMessageToday = !isPreviousSameDate(0, message.getCreatedAt());
        if (isNewMessageToday) {
            items.add(0, new Wrapper<>(message.getCreatedAt(),
                    message.getCreatedAt().getTime() - 1));
        }
        Wrapper<MESSAGE> element = new Wrapper<>(message, message.getCreatedAt().getTime());
        items.add(0, element);
//        for (Wrapper wrapper : items) {
//            Log.e("beforeSortTime", "" + wrapper.timestamp);
//        }
      //  sortItems();
        notifyDataSetChanged();
//        for (Wrapper wrapper : items) {
//            Log.e("afterSortTime", "" + wrapper.timestamp);
//        }
//        notifyItemRangeInserted(0, isNewMessageToday ? 2 : 1);
        if (layoutManager != null && scroll) {
            layoutManager.scrollToPosition(0);
        }
    }

    private void sortItems() {
        Collections.sort(items, (w1, w2) -> Long.compare(w2.timestamp, w1.timestamp));
    }

    public void addLoadingIndicator() {
        if (items.size() > 0 &&
                items.get(0).item instanceof String) return;
        items.add(0, new Wrapper<>("Loading", System.currentTimeMillis()));
        notifyDataSetChanged();
    }


//    public Message getLastMessage() {
//        try {
//            if (items.size() > 0)
//                return (Message) items.get(0).item;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * Adds messages list in chronological order. Use this method to add history.
     *
     * @param messages messages from history.
     * @param reverse  {@code true} if need to reverse messages before adding.
     */
    public void addToEnd(List<MESSAGE> messages, boolean reverse) {
        if (reverse) Collections.reverse(messages);

        if (!items.isEmpty()) {
            int lastItemPosition = items.size() - 1;
            Date lastItem = (Date) items.get(lastItemPosition).item;
            if (DateFormatter.isSameDay(messages.get(0).getCreatedAt(), lastItem)) {
                items.remove(lastItemPosition);
                notifyItemRemoved(lastItemPosition);
            }
        }

        int oldSize = items.size();
        generateDateHeaders(messages);
        notifyItemRangeInserted(oldSize, items.size() - oldSize);
    }

    /**
     * Updates message by its id.
     *
     * @param message updated message object.
     */
    public void update(MESSAGE message) {
        update(message.getMId(), message);
    }

    /**
     * Updates message by old identifier (use this method if id has changed). Otherwise use {@link #update(IMessage)}
     *
     * @param oldId      an identifier of message to update.
     * @param newMessage new message object.
     */
    public void update(String oldId, MESSAGE newMessage) {
        int position = getMessagePositionById(oldId);
        if (position >= 0) {
            Wrapper<MESSAGE> element = new Wrapper<>(newMessage, newMessage.getCreatedAt().getTime());
            items.set(position, element);
            notifyItemChanged(position);
        }
    }

    /**
     * Deletes message.
     *
     * @param message message to delete.
     */
    public void delete(MESSAGE message) {
        deleteById(message.getMId());
    }

    /**
     * Deletes messages list.
     *
     * @param messages messages list to delete.
     */
    public void delete(List<MESSAGE> messages) {
        for (MESSAGE message : messages) {
            int index = getMessagePositionById(message.getMId());
            items.remove(index);
            notifyItemRemoved(index);
        }
        recountDateHeaders();
    }

    /**
     * Deletes message by its identifier.
     *
     * @param id identifier of message to delete.
     */
    public void deleteById(String id) {
        int index = getMessagePositionById(id);
        if (index >= 0) {
            items.remove(index);
            notifyItemRemoved(index);
            recountDateHeaders();
        }
    }

    /**
     * Deletes messages by its identifiers.
     *
     * @param ids array of identifiers of messages to delete.
     */
    public void deleteByIds(String[] ids) {
        for (String id : ids) {
            int index = getMessagePositionById(id);
            items.remove(index);
            notifyItemRemoved(index);
        }
        recountDateHeaders();
    }

    /**
     * Returns {@code true} if, and only if, messages count in adapter is non-zero.
     *
     * @return {@code true} if size is 0, otherwise {@code false}
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Clears the messages list.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Enables selection mode.
     *
     * @param selectionListener listener for selected items count. To get selected messages use {@link #getSelectedMessages()}.
     */
    public void enableSelectionMode(SelectionListener selectionListener) {
        if (selectionListener == null) {
            throw new IllegalArgumentException("SelectionListener must not be null. Use `disableSelectionMode()` if you want tp disable selection mode");
        } else {
            this.selectionListener = selectionListener;
        }
    }

    /**
     * Disables selection mode and removes {@link SelectionListener}.
     */
    public void disableSelectionMode() {
        this.selectionListener = null;
        unselectAllItems();
    }

    /**
     * Returns the list of selected messages.
     *
     * @return list of selected messages. Empty list if nothing was selected or selection mode is disabled.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<MESSAGE> getSelectedMessages() {
        ArrayList<MESSAGE> selectedMessages = new ArrayList<>();
        for (Wrapper wrapper : items) {
            if (wrapper.item instanceof IMessage && wrapper.isSelected) {
                selectedMessages.add((MESSAGE) wrapper.item);
            }
        }
        return selectedMessages;
    }

    /**
     * Returns selected messages text and do {@link #unselectAllItems()} for you.
     *
     * @param formatter The formatter that allows you to format your message model when copying.
     * @param reverse   Change ordering when copying messages.
     * @return formatted text by {@link Formatter}. If it's {@code null} - {@code MESSAGE#toString()} will be used.
     */
    public String getSelectedMessagesText(Formatter<MESSAGE> formatter, boolean reverse) {
        String copiedText = getSelectedText(formatter, reverse);
        unselectAllItems();
        return copiedText;
    }

    /**
     * Copies text to device clipboard and returns selected messages text. Also it does {@link #unselectAllItems()} for you.
     *
     * @param context   The context.
     * @param formatter The formatter that allows you to format your message model when copying.
     * @param reverse   Change ordering when copying messages.
     * @return formatted text by {@link Formatter}. If it's {@code null} - {@code MESSAGE#toString()} will be used.
     */
    public String copySelectedMessagesText(Context context, Formatter<MESSAGE> formatter, boolean reverse) {
        String copiedText = getSelectedText(formatter, reverse);
        copyToClipboard(context, copiedText);
        unselectAllItems();
        return copiedText;
    }

    /**
     * Unselect all of the selected messages. Notifies {@link SelectionListener} with zero count.
     */
    public void unselectAllItems() {
        for (int i = 0; i < items.size(); i++) {
            Wrapper wrapper = items.get(i);
            if (wrapper.isSelected) {
                wrapper.isSelected = false;
                notifyItemChanged(i);
            }
        }
        isSelectionModeEnabled = false;
        selectedItemsCount = 0;
        notifySelectionChanged();
    }

    /**
     * Deletes all of the selected messages and disables selection mode.
     * Call {@link #getSelectedMessages()} before calling this method to delete messages from your data source.
     */
    public void deleteSelectedMessages() {
        List<MESSAGE> selectedMessages = getSelectedMessages();
        delete(selectedMessages);
        unselectAllItems();
    }

    /**
     * Sets click listener for item. Fires ONLY if list is not in selection mode.
     *
     * @param onMessageClickListener click listener.
     */
    public void setOnMessageClickListener(OnMessageClickListener<MESSAGE> onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    /**
     * Sets click listener for message view. Fires ONLY if list is not in selection mode.
     *
     * @param onMessageViewClickListener click listener.
     */
    public void setOnMessageViewClickListener(OnMessageViewClickListener<MESSAGE> onMessageViewClickListener) {
        this.onMessageViewClickListener = onMessageViewClickListener;
    }

    /**
     * Registers click listener for view by id
     *
     * @param viewId                     view
     * @param onMessageViewClickListener click listener.
     */
    public void registerViewClickListener(int viewId, OnMessageViewClickListener<MESSAGE> onMessageViewClickListener) {
        this.viewClickListenersArray.append(viewId, onMessageViewClickListener);
    }

    /**
     * Sets long click listener for item. Fires only if selection mode is disabled.
     *
     * @param onMessageLongClickListener long click listener.
     */
    public void setOnMessageLongClickListener(OnMessageLongClickListener<MESSAGE> onMessageLongClickListener) {
        this.onMessageLongClickListener = onMessageLongClickListener;
    }

    /**
     * Sets long click listener for message view. Fires ONLY if selection mode is disabled.
     *
     * @param onMessageViewLongClickListener long click listener.
     */
    public void setOnMessageViewLongClickListener(OnMessageViewLongClickListener<MESSAGE> onMessageViewLongClickListener) {
        this.onMessageViewLongClickListener = onMessageViewLongClickListener;
    }

    /**
     * Set callback to be invoked when list scrolled to top.
     *
     * @param loadMoreListener listener.
     */
    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * Sets custom {@link DateFormatter.Formatter} for text representation of date headers.
     */
    public void setDateHeadersFormatter(DateFormatter.Formatter dateHeadersFormatter) {
        this.dateHeadersFormatter = dateHeadersFormatter;
    }

    /*
    * PRIVATE METHODS
    * */
    private void recountDateHeaders() {
        List<Integer> indicesToDelete = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Wrapper wrapper = items.get(i);
            if (wrapper.item instanceof Date) {
                if (i == 0) {
                    indicesToDelete.add(i);
                } else {
                    if (items.get(i - 1).item instanceof Date) {
                        indicesToDelete.add(i);
                    }
                }
            }
        }

        Collections.reverse(indicesToDelete);
        for (int i : indicesToDelete) {
            items.remove(i);
            notifyItemRemoved(i);
        }
    }

    private void generateDateHeaders(List<MESSAGE> messages) {
        for (int i = 0; i < messages.size(); i++) {
            MESSAGE message = messages.get(i);
            this.items.add(new Wrapper<>(message, message.getCreatedAt().getTime()));
            if (messages.size() > i + 1) {
                MESSAGE nextMessage = messages.get(i + 1);
                if (!DateFormatter.isSameDay(message.getCreatedAt(), nextMessage.getCreatedAt())) {
                    this.items.add(new Wrapper<>(message.getCreatedAt(), message.getCreatedAt().getTime()));
                }
            } else {
                this.items.add(new Wrapper<>(message.getCreatedAt(), message.getCreatedAt().getTime()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int getMessagePositionById(String id) {
        for (int i = 0; i < items.size(); i++) {
            Wrapper wrapper = items.get(i);
            if (wrapper.item instanceof IMessage) {
                MESSAGE message = (MESSAGE) wrapper.item;
                if (message.getMId().contentEquals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private boolean isPreviousSameDate(int position, Date dateToCompare) {
        if (items.size() <= position) return false;
        if (items.get(position).item instanceof IMessage) {
            Date previousPositionDate = ((MESSAGE) items.get(position).item).getCreatedAt();
            return DateFormatter.isSameDay(dateToCompare, previousPositionDate);
        } else return false;
    }

    public void checkRemoveLoadingIfExist() {
        if (items.size() > 0 &&
                items.get(0).item instanceof String) {
            items.remove(0);
            notifyItemRemoved(0);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean isPreviousSameAuthor(String id, int position) {
        int nextPos = position - 1;
        if (nextPos < 0 || items.size() <= nextPos) return true;
        else if (items.get(nextPos).item instanceof IMessage) {
//            Message next = (Message) items.get(nextPos).item;
//            if (next.getMessageType() == Constants.MessageType.SIMPLE &&
//                    next.getMessageSimple().getMedia() != null) return true;

            return ((MESSAGE) items.get(nextPos).item).getMessageType() !=
                    ((MESSAGE) items.get(position).item).getMessageType();
        }
        return false;
    }

//    public boolean isNextSameForCarousel(String id, int position) {
//        int nextPos = position - 1;
//        if (nextPos < 0 || items.size() <= nextPos) return true;
//        else if (items.get(nextPos).item instanceof IMessage) {
//            Message next = (Message) items.get(nextPos).item;
//            Message current = (Message) items.get(position).item;
//            return (current.getMessageId().equals(next.getResponseTo()));
////            return ((MESSAGE) items.get(nextPos).item).getMessageType() !=
////                    ((MESSAGE) items.get(position).item).getMessageType();
//        }
//        return true;
//    }


    private void incrementSelectedItemsCount() {
        selectedItemsCount++;
        notifySelectionChanged();
    }

    private void decrementSelectedItemsCount() {
        selectedItemsCount--;
        isSelectionModeEnabled = selectedItemsCount > 0;

        notifySelectionChanged();
    }

    private void notifySelectionChanged() {
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedItemsCount);
        }
    }

    private void notifyMessageClicked(MESSAGE message) {
        if (onMessageClickListener != null) {
            onMessageClickListener.onMessageClick(message);
        }
    }

    private void notifyMessageViewClicked(View view, MESSAGE message) {
        if (onMessageViewClickListener != null) {
            onMessageViewClickListener.onMessageViewClick(view, message);
        }
    }

    private void notifyMessageLongClicked(MESSAGE message) {
        if (onMessageLongClickListener != null) {
            onMessageLongClickListener.onMessageLongClick(message);
        }
    }

    private void notifyMessageViewLongClicked(View view, MESSAGE message) {
        if (onMessageViewLongClickListener != null) {
            onMessageViewLongClickListener.onMessageViewLongClick(view, message);
        }
    }

    private View.OnClickListener getMessageClickListener(final Wrapper<MESSAGE> wrapper) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectionListener != null && isSelectionModeEnabled) {
                    wrapper.isSelected = !wrapper.isSelected;

                    if (wrapper.isSelected) incrementSelectedItemsCount();
                    else decrementSelectedItemsCount();

                    MESSAGE message = (wrapper.item);
                    notifyItemChanged(getMessagePositionById(message.getMId()));
                } else {
                    notifyMessageClicked(wrapper.item);
                    notifyMessageViewClicked(view, wrapper.item);
                }
            }
        };
    }

    private View.OnLongClickListener getMessageLongClickListener(final Wrapper<MESSAGE> wrapper) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (selectionListener == null) {
                    notifyMessageLongClicked(wrapper.item);
                    notifyMessageViewLongClicked(view, wrapper.item);
                    return true;
                } else {
                    isSelectionModeEnabled = true;
                    view.performClick();
                    return true;
                }
            }
        };
    }

    private String getSelectedText(Formatter<MESSAGE> formatter, boolean reverse) {
        StringBuilder builder = new StringBuilder();

        ArrayList<MESSAGE> selectedMessages = getSelectedMessages();
        if (reverse) Collections.reverse(selectedMessages);

        for (MESSAGE message : selectedMessages) {
            builder.append(formatter == null
                    ? message.toString()
                    : formatter.format(message));
            builder.append("\n\n");
        }
        builder.replace(builder.length() - 2, builder.length(), "");

        return builder.toString();
    }

    private void copyToClipboard(Context context, String copiedText) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(copiedText, copiedText);
        clipboard.setPrimaryClip(clip);
    }

    void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /*
    * WRAPPER
    * */
    private class Wrapper<DATA> {
        protected DATA item;
        protected boolean isSelected;
        protected long timestamp;

        Wrapper(DATA item, long timestamp) {
            this.item = item;
            this.timestamp = timestamp;

        }
    }

    /*
    * LISTENERS
    * */

    /**
     * Interface definition for a callback to be invoked when next part of messages need to be loaded.
     */
    public interface OnLoadMoreListener {

        /**
         * Fires when user scrolled to the end of list.
         *
         * @param page            next page to download.
         * @param totalItemsCount current items count.
         */
        void onLoadMore(int page, int totalItemsCount);
    }

    /**
     * Interface definition for a callback to be invoked when selected messages count is changed.
     */
    public interface SelectionListener {

        /**
         * Fires when selected items count is changed.
         *
         * @param count count of selected items.
         */
        void onSelectionChanged(int count);
    }

    /**
     * Interface definition for a callback to be invoked when message item is clicked.
     */
    public interface OnMessageClickListener<MESSAGE extends IMessage> {

        /**
         * Fires when message is clicked.
         *
         * @param message clicked message.
         */
        void onMessageClick(MESSAGE message);
    }

    /**
     * Interface definition for a callback to be invoked when message view is clicked.
     */
    public interface OnMessageViewClickListener<MESSAGE extends IMessage> {

        /**
         * Fires when message view is clicked.
         *
         * @param message clicked message.
         */
        void onMessageViewClick(View view, MESSAGE message);
    }

    /**
     * Interface definition for a callback to be invoked when message item is long clicked.
     */
    public interface OnMessageLongClickListener<MESSAGE extends IMessage> {

        /**
         * Fires when message is long clicked.
         *
         * @param message clicked message.
         */
        void onMessageLongClick(MESSAGE message);
    }

    /**
     * Interface definition for a callback to be invoked when message view is long clicked.
     */
    public interface OnMessageViewLongClickListener<MESSAGE extends IMessage> {

        /**
         * Fires when message view is long clicked.
         *
         * @param message clicked message.
         */
        void onMessageViewLongClick(View view, MESSAGE message);
    }

    /**
     * Interface used to format your message model when copying.
     */
    public interface Formatter<MESSAGE> {

        /**
         * Formats an string representation of the message object.
         *
         * @param message The object that should be formatted.
         * @return Formatted text.
         */
        String format(MESSAGE message);
    }
}
