package com.nowfloats.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */

import java.util.List;

public class POSTRequest extends Request {
    public final String query;

    public POSTRequest(Method method, String url, String query, List<KeyValuePair> headers, int timeout) {
        super(method, url, headers, timeout);
        this.query = query;
    }
}
