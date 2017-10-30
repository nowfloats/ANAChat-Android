package com.nowfloats.chatsdk;

/**
 * Created by lookup on 16/10/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.nowfloats.chatsdk.internal.database.PreferencesManager;
import com.nowfloats.chatsdk.uimodule.AnaChatActivity;

import static com.nowfloats.chatsdk.internal.utils.constants.Constants.UIParams.Theme_color;
import static com.nowfloats.chatsdk.internal.utils.constants.Constants.UIParams.ToolBar_Image;
import static com.nowfloats.chatsdk.internal.utils.constants.Constants.UIParams.ToolBar_Tittle;
import static com.nowfloats.chatsdk.internal.utils.constants.Constants.UIParams.ToolBar_Tittle_Desc;


public class NfChatBuilder {

    private Activity activity;
    private Intent launchIntent;
    private Bundle bundle;
    private String businessId = "";

    public NfChatBuilder(@NonNull Activity activity) {
        this.activity = activity;
        launchIntent = new Intent(activity, AnaChatActivity.class);
        bundle = new Bundle();
    }

    public NfChatBuilder setBussinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }

    public NfChatBuilder setToolBarTittle(String val) {
        bundle.putString(ToolBar_Tittle, val);
        return this;
    }

    public NfChatBuilder setThemeColor(int color) {
        bundle.putInt(Theme_color, color);
        return this;
    }

    public NfChatBuilder setToolBarDescription(String val) {
        bundle.putString(ToolBar_Tittle_Desc, val);
        return this;
    }

    public NfChatBuilder setToolBarLogo(int drawable) {
        bundle.putInt(ToolBar_Image, drawable);
        return this;
    }


    public void start() {
        if (businessId.isEmpty())
            throw new IllegalArgumentException("Business Id can't be empty please set businessId");
        PreferencesManager.getsInstance(activity).setBusinessId(businessId);
        launchIntent.putExtras(bundle);
        activity.startActivity(launchIntent);
    }
}