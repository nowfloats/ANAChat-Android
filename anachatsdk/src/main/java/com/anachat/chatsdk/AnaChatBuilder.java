package com.anachat.chatsdk;

/**
 * Created by nowfloats on 16/10/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.network.ApiCalls;
import com.anachat.chatsdk.uimodule.AnaChatActivity;

import org.json.JSONObject;

import java.util.HashMap;

import static com.anachat.chatsdk.internal.utils.constants.Constants.UIParams.Theme_color;
import static com.anachat.chatsdk.internal.utils.constants.Constants.UIParams.ToolBar_Image;
import static com.anachat.chatsdk.internal.utils.constants.Constants.UIParams.ToolBar_Tittle;
import static com.anachat.chatsdk.internal.utils.constants.Constants.UIParams.ToolBar_Tittle_Desc;


public class AnaChatBuilder {

    private Activity activity;
    private Intent launchIntent;
    private Bundle bundle;
    private String businessId = "";
    private String baseUrl = "";

    public AnaChatBuilder(@NonNull Activity activity) {
        this.activity = activity;
        launchIntent = new Intent(activity, AnaChatActivity.class);
        bundle = new Bundle();
    }

    public AnaChatBuilder setBusinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }

    public AnaChatBuilder setToolBarTittle(String val) {
        bundle.putString(ToolBar_Tittle, val);
        return this;
    }

    public AnaChatBuilder setThemeColor(int color) {
        bundle.putInt(Theme_color, color);
        return this;
    }

    public AnaChatBuilder setToolBarDescription(String val) {
        bundle.putString(ToolBar_Tittle_Desc, val);
        return this;
    }

    public AnaChatBuilder setToolBarLogo(int drawable) {
        bundle.putInt(ToolBar_Image, drawable);
        return this;
    }

    public AnaChatBuilder registerLocationSelectListener(LocationPickListener locationPickListener) {
        AnaCore.addLocationPickListener(locationPickListener);
        return this;
    }

    public AnaChatBuilder registerCustomMethodListener(CustomMethodListener customMethodListener) {
        AnaCore.addCustomMethodListener(customMethodListener);
        return this;
    }

    public AnaChatBuilder setBaseUrl(String url) {
        this.baseUrl = url;
        return this;
    }

    public AnaChatBuilder setFlowId(@NonNull String flowId) {
        if (!flowId.isEmpty()) {
            PreferencesManager.getsInstance(activity).setFlowId(flowId);
        }
        return this;
    }

    public AnaChatBuilder setEvents(HashMap<String, String> events) {
        PreferencesManager.getsInstance(activity).setEventsData(new JSONObject(events).toString());
        return this;
    }

    public AnaChatBuilder openUrlInBrowser(Boolean val) {
        PreferencesManager.getsInstance(activity).setUrlStatus(val);
        return this;
    }

    public void start() {
        if (businessId.isEmpty() ||
                PreferencesManager.getsInstance(activity).getFlowId().isEmpty())
            throw new IllegalArgumentException("Business Id Or FlowId can't be empty");

        if (baseUrl.isEmpty())
            throw new IllegalArgumentException("BaseUrl can't be empty please setBaseUrl");
        if (PreferencesManager.getsInstance(activity).getUserName().isEmpty()) {
            if (!PreferencesManager.getsInstance(activity).getFcmToken().isEmpty()) {
                ApiCalls.updateToken(activity.getApplicationContext(), null);
            }
            Log.d("User Not Registered", "Please wait..");
            return;
        }
        PreferencesManager.getsInstance(activity).setBusinessId(businessId);
        PreferencesManager.getsInstance(activity).setBaseUrl(baseUrl);
        launchIntent.putExtras(bundle);
        activity.startActivity(launchIntent);
    }
}