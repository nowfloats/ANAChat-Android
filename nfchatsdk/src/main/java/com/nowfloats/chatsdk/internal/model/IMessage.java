package com.nowfloats.chatsdk.internal.model;

import java.util.Date;

/**
 * For implementing by real message model
 */
public interface IMessage {

    /**
     * Returns message identifier
     *
     * @return the message id
     */
    String getMId();

    /**
     * Returns message text
     *
     * @return the message text
     */
    String getText();

    /**
     * Returns message author. See the {@link String} for more details
     *
     * @return the message author
     */
    String getUserId();

    /**
     * Returns message creation date
     *
     * @return the message creation date
     */
    Date getCreatedAt();

    /**
     * Returns message type
     *
     * @return the message type
     */
    int getMessageType();

//    /**
//     * Returns inputMessage type
//     *
//     * @return the inputMessage type
//     */
//    int getInputMessageType();
}
