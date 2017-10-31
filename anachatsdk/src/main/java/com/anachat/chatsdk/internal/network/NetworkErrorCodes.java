package com.anachat.chatsdk.internal.network;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lookup on 09/10/17.
 */
public interface NetworkErrorCodes {
    Integer NO_CONNECTION = Integer.valueOf(0);
    Integer GENERIC_NETWORK_ERROR = Integer.valueOf(1);
    Integer SCREENSHOT_UPLOAD_ERROR = Integer.valueOf(2);
    Integer UNKNOWN_HOST_ERROR = Integer.valueOf(3);
    Integer SSL_PEER_UNVERIFIED_ERROR = Integer.valueOf(4);
    Integer SSL_HANDSHAKE_ERROR = Integer.valueOf(5);
    Integer PROFILE_NOT_REGISTERED = Integer.valueOf(6);
    Integer OK = Integer.valueOf(200);
    Integer CONTENT_UNCHANGED = Integer.valueOf(304);
    Integer OBJECT_NOT_FOUND = Integer.valueOf(400);
    Integer UNAUTHORIZED_ACCESS = Integer.valueOf(401);
    Integer FORBIDDEN_ACCESS = Integer.valueOf(403);
    Integer CONTENT_NOT_FOUND = Integer.valueOf(404);
    Integer METHOD_NOT_ALLOWED = Integer.valueOf(405);
    Integer PARSE_ERROR = Integer.valueOf(406);
    Integer REQUEST_TIMEOUT = Integer.valueOf(408);
    Integer GONE_ERROR = Integer.valueOf(410);
    Integer NO_REQUEST_LENGTH = Integer.valueOf(411);
    Integer ENTITY_TOO_LARGE = Integer.valueOf(413);
    Integer URI_TOO_LONG = Integer.valueOf(414);
    Integer TIMESTAMP_MISMATCH = Integer.valueOf(422);
    Integer SERVER_ERROR = Integer.valueOf(500);
    Set<Integer> NOT_RETRIABLE_STATUS_CODES = new HashSet() {
        {
            this.add(NetworkErrorCodes.UNAUTHORIZED_ACCESS);
            this.add(NetworkErrorCodes.FORBIDDEN_ACCESS);
            this.add(NetworkErrorCodes.CONTENT_NOT_FOUND);
            this.add(NetworkErrorCodes.METHOD_NOT_ALLOWED);
            this.add(NetworkErrorCodes.GONE_ERROR);
            this.add(NetworkErrorCodes.NO_REQUEST_LENGTH);
            this.add(NetworkErrorCodes.ENTITY_TOO_LARGE);
            this.add(NetworkErrorCodes.URI_TOO_LONG);
        }
    };
}
