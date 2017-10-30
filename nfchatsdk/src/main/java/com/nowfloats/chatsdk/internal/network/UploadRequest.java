package com.nowfloats.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */

import java.util.List;
import java.util.Map;

public class UploadRequest extends Request {
    public static final String BOUNDARY = "*****";
    public final Map<String, String> data;
    public final String mimeType;

    public UploadRequest(Method method, String url, Map<String, String> data, String mimeType, List<KeyValuePair> headers, int timeout) {
        super(method, url, headers, timeout);
        this.data = data;
        this.mimeType = mimeType;
    }
}
