package com.anachat.chatsdk;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by lookup on 22/11/17.
 */

public interface LocationPickListener {

    Intent pickLocation(Activity activity);

    void sendLocation(Intent data);
}
