package com.nowfloats.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */

import java.util.List;

public class Request {
    public final Method method;
    public final String url;
    public final List<KeyValuePair> headers;
    public final int timeout;

    public Request(Method method, String url, List<KeyValuePair> headers, int timeout) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.timeout = timeout;
    }
}
