package com.anachat.chatsdk;

import android.content.Context;

/**
 * Created by admin on 13-Mar-18.
 */

public interface CustomMethodListener {

    void implementCustomMethod(Context context, String deeplinkUrl, String title, String value, Boolean fromCarousel);

}
