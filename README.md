ANAChat Android
===================


The Powerful **ANAchat**  android SDK allows you to integrate ANA to your app in two simple steps. Customise the UI according to your App Theme and you are all set. It is that simple!

----------

Getting started
===============


Add below mandatory dependencies in your app level build.gradle.

    dependencies {
    ...
    compile 'com.kitsune:anachatsdk:1.4@aar'
    compile 'com.android.support:design:26.1.0'
    compile 'com.j256.ormlite:ormlite-android:5.0'
    compile 'com.google.code.gson:gson:2.8.1'
    compile 'com.github.bumptech.glide:glide:4.1.1'
    ...
    }

**FCM configuration** is required to use this SDK please check the documentation [here](https://firebase.google.com/docs/cloud-messaging/android/client) to configure.

After successful configuration of FCM, Modify below classes:

In `FirebaseInstanceIdService`

    public class NfChatBotInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        NfCore.updateToken(this, refreshedToken,"your_base_url","your_business_id");
	    }
    }

In `FirebaseMessagingService`

    public class NfChatBotMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> mapResult = remoteMessage.getData();
        if (mapResult.containsKey("payload")) {
            NfCore.handlePush(this, mapResult.get("payload"));
        }

    }
    }

#### <i class="icon-file"></i> add permissions In manifest






    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

#### <i class="icon-book"></i> Start BOT
     new AnaChatBuilder(activity)
			    .setBusinessId("your_businessId")
                .setBaseUrl("your_base_url")
                .setThemeColor(R.color.primary)
                .setToolBarDescription("Your Toolbar desc")
                .setToolBarTittle("Your Tittle")
                .setBussinessId("add_your_business_id")
                .setToolBarLogo(R.drawable.ic_your_logo)
                .start();

`Note`: Pass Valid businessId and BaseUrl in  builder.

License
=======

    Copyright 2017 ana.chat

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.