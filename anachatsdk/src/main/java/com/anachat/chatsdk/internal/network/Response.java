package com.anachat.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */


import java.util.List;

public class Response {
    public final int status;
    public final String responseString;
    public final List<KeyValuePair> headers;

    public Response(int status, String responseString, List<KeyValuePair> headers) {
        this.status = status;
        this.responseString = responseString;
        this.headers = headers;
    }
}
