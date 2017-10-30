package com.nowfloats.chatsdk.internal.network;

import com.nowfloats.chatsdk.internal.utils.ExceptionType;

/**
 * Created by lookup on 09/10/17.
 */

public enum NetworkException implements ExceptionType {
    GENERIC,
    NO_CONNECTION,
    UNKNOWN_HOST,
    SSL_PEER_UNVERIFIED,
    SSL_HANDSHAKE,
    UNHANDLED_STATUS_CODE,
    TIMESTAMP_CORRECTION_RETRIES_EXHAUSTED,
    ENTITY_TOO_LARGE_RETRIES_EXHAUSTED,
    CONTENT_NOT_FOUND,
    UNSUPPORTED_ENCODING_EXCEPTION,
    UNABLE_TO_GENERATE_SIGNATURE,
    UNSUPPORTED_MIME_TYPE,
    NON_RETRIABLE,
    CONVERSATION_ARCHIVED,
    SCREENSHOT_UPLOAD_ERROR;

    public int serverStatusCode;
    public String route;

    private NetworkException() {
    }
}
