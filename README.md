
ANAChat Android
===================


The Powerful **ANAchat**  android SDK allows you to integrate ANA to your app in two simple steps. Customise the UI according to your App Theme and you are all set. It is that simple!

----------

Getting started
===============


Add below mandatory dependencies in your app level build.gradle.
```ruby
    dependencies {
    ...
    compile 'com.kitsune:anachatsdk:1.18.5@aar'
    compile 'com.android.support:design:26.1.0'
    compile 'com.j256.ormlite:ormlite-android:5.0'
    compile 'com.google.code.gson:gson:2.8.1'
    compile 'com.github.bumptech.glide:glide:4.1.1'
    ...
    }
```
**FCM configuration** is required to use this SDK please check the documentation [here](https://firebase.google.com/docs/cloud-messaging/android/client) to configure.

After successful configuration of FCM, Modify below classes:

In `FirebaseInstanceIdService`
    public class AnaChatBotInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //If(user is not registered)
        AnaCore.saveFcmToken(this, refreshedToken);
        else
        AnaCore.saveFcmToken(this, refreshedToken,"user_id");
	    }
    }

In `FirebaseMessagingService`
    public class AnaChatBotMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> mapResult = remoteMessage.getData();
        if (mapResult.containsKey("payload")) {
            AnaCore.handlePush(this, mapResult.get("payload"));
        }

    }
    }

 **Register user** after your login flow:
 `AnaCore.registerUser(context, "your_user_id", YOUR_BUSINESSID, YOUR_BASE_URL);`

 To unregister user:
`AnaCore.logoutUser(context);`
```ruby
**Add permissions In manifest**

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />


 **Start BOT**

             new AnaChatBuilder(activity)
                .setBusinessId("your_businessId")
                .setBaseUrl("your_base_url")
                .setThemeColor(R.color.primary)
                .setToolBarDescription("Your Toolbar desc")
                .setToolBarTittle("Your Tittle")
                .setToolBarLogo(R.drawable.ic_your_logo)
          (optional).registerLocationSelectListener(this)
                .setFlowId(YOUR_FLOW_ID)
                .start();
```

`Note`: Pass Valid businessId and BaseUrl in  builder.

OPTIONAL :-
To enable **Location** support in SDK follow below steps:

1. Register or Login [here](https://developers.google.com/places/android-api/signup) to get places api key.
2. add google places key in manifest file under application tag.
	` <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="your_key" />`
3. add`compile'com.google.android.gms:play-services-places:11.6.0'`in app level gradle file.
4. implement **LocationPickListener** in your fragment/activity.
2. add **registerLocationSelectListener(this)** in  AnaChatBuilder.
4. Override methods and Paste below code :-

   ```ruby
   @Override
    public Intent pickLocation(Activity activity) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent placePickerIntent = builder.build(activity);
            placePickerIntent.putExtra("primary_color",
                    Color.parseColor(PreferencesManager.getsInstance(activity).getThemeColor()));
            placePickerIntent.putExtra("primary_color_dark",
                    ContextCompat.getColor(activity, R.color.gray_light));
            return placePickerIntent;
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendLocation(Intent data) {
        Place place = PlacePicker.getPlace(this, data);
        LatLng latLng = place.getLatLng();
        AnaCore.sendLocation(latLng.latitude, latLng.longitude, this);
    }
   ```
**Proguard** :-
Please check & paste configuration from [here](https://github.com/Kitsune-tools/ANAChat-Android/blob/master/app/proguard-rules.pro)

License
=======

ANA Conversation Suite is available under the [GNU GPLv3 license](https://www.gnu.org/licenses/gpl-3.0.en.html).