package com.anachat.chatsdk.uimodule;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anachat.chatsdk.AnaChatSDKConfig;
import com.anachat.chatsdk.AnaCore;
import com.anachat.chatsdk.MessageListener;
import com.anachat.chatsdk.internal.MessengerCoreMethods;
import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.MessageResponse;
import com.anachat.chatsdk.internal.model.inputdata.Address;
import com.anachat.chatsdk.internal.model.inputdata.Input;
import com.anachat.chatsdk.internal.model.inputdata.Time;
import com.anachat.chatsdk.internal.utils.ListenerManager;
import com.anachat.chatsdk.internal.utils.NFChatUtils;
import com.anachat.chatsdk.internal.utils.concurrent.ApiExecutorFactory;
import com.anachat.chatsdk.internal.utils.constants.Constants;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.chatuikit.commons.ImageLoader;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessageHolders;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessagesList;
import com.anachat.chatsdk.uimodule.chatuikit.messages.MessagesListAdapter;
import com.anachat.chatsdk.uimodule.chatuikit.utils.RangeTimePickerDialog;
import com.anachat.chatsdk.uimodule.ui.MediaPreviewActivity;
import com.anachat.chatsdk.uimodule.ui.VideoViewerActivity;
import com.anachat.chatsdk.uimodule.ui.adapter.InputListOptionsAdapter;
import com.anachat.chatsdk.uimodule.ui.adapter.OptionsAdapter;
import com.anachat.chatsdk.uimodule.utils.AppUtils;
import com.anachat.chatsdk.uimodule.utils.ImagesCache;
import com.anachat.chatsdk.uimodule.utils.InputIntents;
import com.anachat.chatsdk.uimodule.utils.LruCache;
import com.anachat.chatsdk.uimodule.utils.PathUtil;
import com.anachat.chatsdk.uimodule.utils.SlideInLeftAnimator;
import com.anachat.chatsdk.uimodule.utils.TouchImageView;
import com.anachat.chatsdk.uimodule.viewholder.BlankMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.carousel.IncomingCarouselMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.carousel.OutComingCarouselTextMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.input.DefaultStringInputViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.input.OutcomingInputEmailMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.input.OutcomingInputLocationMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.input.OutcomingInputMediaMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.input.OutcomingInputNumericMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.input.OutcomingInputPhoneMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.simple.IncomingSimpleMediaMessageViewHolder;
import com.anachat.chatsdk.uimodule.viewholder.simple.IncomingSimpleMessageViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AnaChatActivity extends AppCompatActivity
        implements
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageListener
        , View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private MessagesList messagesList;
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private EditText edInput;
    private LruCache mMemoryCache;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    private Menu menu;
//    private int selectionCount;
    private Button btnAction;
    private RecyclerView rvOptions;
    private RelativeLayout input;
    private ImageView ivSend;
    private ImageView ivToolbarLogo;
    private TextView tvTittle;
    private TextView tvDesc;
    private ImageView ivOnline;
    private OptionsAdapter optionsAdapter;
    private Boolean isFirstHistoryLoaded = false;
    private static final int LOCATION_PERMISSION_REQUEST = 10;
    private BottomSheetDialog mBottomSheetDialog = null;

    //    private static final float SHAKE_THRESHOLD = 65; // m/S**2
//    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 4000;
//    private long mLastShakeTime;
//    private SensorManager mSensorMgr;
//    private final SensorEventListener mSensorListener = new SensorEventListener() {
//
//        public void onSensorChanged(SensorEvent event) {
//            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                long curTime = System.currentTimeMillis();
//                if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
//
//                    float x = event.values[0];
//                    float y = event.values[1];
//                    float z = event.values[2];
//
//                    double acceleration = Math.sqrt(Math.pow(x, 2) +
//                            Math.pow(y, 2) +
//                            Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
//                    if (acceleration > SHAKE_THRESHOLD) {
//                        mLastShakeTime = curTime;
//                        try {
//                            if (MessageRepository.getInstance
//                                    (AnaChatActivity.this).getLastMessage() != null ||
//                                    MessageRepository.
//                                            getInstance(AnaChatActivity.this).getLastMessage().size() > 0) {
//                                ApiExecutor apiExecutor = ApiExecutorFactory.getHandlerExecutor();
//                                apiExecutor.runAsync(() -> {
//                                    MessageRepository.getInstance(AnaChatActivity.this).clearTables();
//                                    messagesAdapter.clearQueue();
//                                    ApiExecutor executor = ApiExecutorFactory.getHandlerExecutor();
//                                    executor.runOnUiThread(() -> {
//                                        hideBottomViews();
//                                        messagesAdapter.clearQueue();
//                                        messagesAdapter.notifyDataSetChanged();
//                                        AnaCore.loadInitialHistory(AnaChatActivity.this);
////                                                onConversationUpdate(new ArrayList<>());
//                                    });
//                                });
//                            }
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    "Refreshing Session..", Toast.LENGTH_SHORT);
//                            toast.show();
//                        } catch (Exception e) {
//
//                        }
//
//                    }
//                }
//            }
//
//        }
//
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        }
//    };
    private void showNoInternet() {
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) return;
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_no_internet, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);

        TextView tvCheckNetSettings = sheetView.findViewById(R.id.tv_line_3);
        ImageView ivSignal = sheetView.findViewById(R.id.iv_no_net);
        ivSignal.setColorFilter(Color.parseColor
                (PreferencesManager.getsInstance(imageLoader.getContext()).getThemeColor()));
        GradientDrawable drawable = (GradientDrawable) tvCheckNetSettings.getBackground();
        drawable.setColor(Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        drawable.setStroke(3,
                Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        tvCheckNetSettings.setBackground(drawable);
        tvCheckNetSettings.setOnClickListener(view -> {
            try {
                Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mBottomSheetDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            int colorCode = getIntent().getExtras().
                    getInt(Constants.UIParams.Theme_color, R.color.ana_primary);
            String value = "#" + Integer.toHexString(getResources().getColor(colorCode));
            PreferencesManager.getsInstance(this).setThemeColor(value);
        }
        setContentView(R.layout.activity_ana_chat);
        initViews();
        setCache();
        installAna();
        initViews();
        initImageLoader();
        initAdapter();
        if (getIntent().getExtras() != null) {
            int logo = getIntent().getExtras().
                    getInt(Constants.UIParams.ToolBar_Image, R.drawable.ic_placeholder);
            ivToolbarLogo.setBackgroundResource(R.color.white);
            ivToolbarLogo.setImageResource(logo);
            String tittle = getIntent().getExtras().
                    getString(Constants.UIParams.ToolBar_Tittle, "ANAChat");
            tvTittle.setText(tittle);
            String desc = getIntent().getExtras().
                    getString(Constants.UIParams.ToolBar_Tittle_Desc, "ANA Intelligence");
            tvDesc.setText(desc);
        }
    }

    private void setCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache(cacheSize);
    }

    private void installAna() {
        AnaChatSDKConfig anaChatSDKConfig = AnaCore
                .config()
                .context(this)
                .build();
        AnaCore.install
                (anaChatSDKConfig, this);
    }

//    private void initSensors() {
//        // Get a sensor manager to listen for shakes
//        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
//
//        // Listen for shakes
//        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (accelerometer != null) {
//            mSensorMgr.registerListener(mSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver,
                new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

//        initSensors();
    }

    @Override
    protected void onPause() {
//        if (mSensorMgr != null)
//            mSensorMgr.unregisterListener(mSensorListener);
        ApiExecutorFactory.getHandlerExecutor().restart();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);

    }

    private void initViews() {
        RelativeLayout toolbar = findViewById(R.id.rl_toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        toolbar.setBackgroundColor(
                Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        messagesList = findViewById(R.id.messagesList);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.
                parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        swipeRefreshLayout.setEnabled(false);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        messagesList.setItemAnimator(animator);
        btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(this);
        rvOptions = findViewById(R.id.rv_options);
        rvOptions.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));


        StateListDrawable drawable = (StateListDrawable) btnAction.getBackground();
        DrawableContainer.DrawableContainerState drawableContainerState =
                (DrawableContainer.DrawableContainerState) drawable.getConstantState();
        LayerDrawable selectedItem = (LayerDrawable) drawableContainerState.getChildren()[0];
        GradientDrawable gradientDrawable = (GradientDrawable) selectedItem
                .findDrawableByLayerId(R.id.bubble);
        gradientDrawable.setColor(Color.parseColor
                (PreferencesManager.getsInstance(this).getThemeColor()));
        ViewCompat.setBackground(btnAction, drawable);
        ivToolbarLogo = findViewById(R.id.iv_toolbar);
        tvTittle = findViewById(R.id.tv_tittle_name);
        tvDesc = findViewById(R.id.tv_desc);
        ivOnline = findViewById(R.id.iv_online);
        input = findViewById(R.id.input);
        edInput = findViewById(R.id.messageInput);
        edInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    ivSend.setColorFilter(Color.parseColor
                            (PreferencesManager.getsInstance(AnaChatActivity.this).getThemeColor()));
                } else {
                    ivSend.setColorFilter(ContextCompat.getColor(AnaChatActivity.this,
                            R.color.grey_300));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ivSend = findViewById(R.id.iv_send);
        ivSend.setOnClickListener(view -> {
            Boolean hide = onSubmit(edInput.getText());
            if (hide) {
                edInput.setText("");
            }
        });
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(view -> onBackPressed());
        RelativeLayout rlRoot = findViewById(R.id.rl_root);
        rlRoot.getBackground().setAlpha(180);
    }

    private void initImageLoader() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                if (url.startsWith("http")) {
                    Glide.with(AnaChatActivity.this)
                            .load(url)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_placeholder)
                                    .centerCrop()
                                    .timeout(20000)
                                    .dontTransform())
                            .into(imageView);
//                    ApiExecutor apiExecutor = ApiExecutorFactory.getHandlerExecutor();
//                    apiExecutor.submitToPool(() -> DownloadImageFromPath(url, imageView));

                } else {
                    Uri uri = Uri.fromFile(new File(url));
                    imageView.setImageURI(uri);
                    Glide.with(AnaChatActivity.this)
                            .load(uri)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_placeholder)
                                    .centerCrop()
                                    .dontTransform())
                            .into(imageView);
                }
            }

            public void DownloadImageFromPath(String path, ImageView iv) {
                ImagesCache imagesCache = ImagesCache.getInstance();
                imagesCache.initializeCache();
                Bitmap bitmap = imagesCache.getImageFromWarehouse(path);
                if (bitmap != null) {
                    iv.setImageBitmap(bitmap);
                    return;
                }
                InputStream in = null;
                Bitmap bmp = null;
                int responseCode = -1;
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.connect();
                    responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //download
                        in = con.getInputStream();
                        bmp = BitmapFactory.decodeStream(in);
                        in.close();
                        iv.setImageBitmap(bmp);
                        imagesCache.addImageToWarehouse(path, bmp);
//                        mMemoryCache.put(path, bmp);
                    }

                } catch (Exception ex) {
                    Log.e("Exception", ex.toString());
                }
            }


            @Override
            public void openMedia(String url, int type) {
                if (type == Constants.MediaType.IMAGE) {
                    loadPhotoDialog(url);
                }
                if (type == Constants.MediaType.VIDEO)
                    startActivity(VideoViewerActivity.startIntent(AnaChatActivity.this, url));
                //TODO add audio here
            }

            @Override
            public Context getContext() {
                return AnaChatActivity.this;
            }

            @Override
            public Boolean isPreviousMessageHasSameAuthor(String id, int position) {
                return messagesAdapter.isPreviousSameAuthor(id, position);
            }

//            @Override
//            public void disableCarousels() {
//                messagesAdapter.disableCarousels();
//                AnaCore.disableCarousel(AnaChatActivity.this);
//            }

            @Override
            public void disableOptions() {
                hideOptions();
            }

            @Override
            public Boolean isCarouselEnable(Integer position) {
                return messagesAdapter.isCarouselEnabled(position);
            }
        };
    }

    private void loadPhotoDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().
                    setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View dialogLayout = inflater.inflate(R.layout.activity_picture_viewer, null);
        dialog.setView(dialogLayout);
        TouchImageView image = dialogLayout.findViewById(R.id.image);
        ImageView close = dialogLayout.findViewById(R.id.close_page);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imageLoader.loadImage(image, url);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public boolean onSubmit(CharSequence input) {
        String text = input.toString().trim();
        if (text.isEmpty()) {
            return false;
        }
        Message message
                = getLastMessage();
        return validateTextString(message, text);
    }

    private Boolean validateTextString(Message message, String text) {
        switch (message.getMessageInput().getInputType()) {
            case Constants.InputType.TEXT:
                if (message.getMessageInput().getMandatory() ==
                        Constants.FCMConstants.MANDATORY_TRUE) {
                    int minLen = message.getMessageInput()
                            .getInputTypeText().getTextInputAttr().getMinLength();
                    int maxLen = message.getMessageInput()
                            .getInputTypeText().getTextInputAttr().getMaxLength();
                    int multiLineLen = message.getMessageInput()
                            .getInputTypeText().getTextInputAttr().getMultiLine();

                    if (minLen > 0 && !AppUtils.checkStringMinLen
                            (text, minLen)) {
                        AppUtils.showToast(this, "Minimum " + minLen + " characters required.", false);
                        return false;
                    }
                    if (maxLen > 0 && !AppUtils.checkStringMaxLen
                            (text, maxLen)) {
                        AppUtils.showToast(this, "Maximum " + maxLen + " characters allowed.", false);
                        return false;
                    }
                    if (multiLineLen > 0 && !AppUtils.checkMultiLineLen(
                            getLineCount(), multiLineLen)) {
                        AppUtils.showToast(this, "Maximum " + maxLen + "lines allowed.", false);
                        return false;
                    }
                }
                sendTextInputMessage(text, message);
                break;
            case Constants.InputType.EMAIL:
                if (message.getMessageInput().getMandatory()
                        == Constants.FCMConstants.MANDATORY_TRUE) {
                    if (!AppUtils.checkEmailString(text)) {
                        AppUtils.showToast(
                                this, "Please enter a valid email", false
                        );
                        return false;
                    }
                }
                sendTextInputMessage(text, message);
                break;
            case Constants.InputType.PHONE:
                if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE) {
                    if (!AppUtils.checkPhoneString(text)) {
                        AppUtils.showToast(
                                this, "Please enter a valid phone number", false
                        );
                        return false;
                    }
                }
                sendTextInputMessage(text, message);
                break;
            case Constants.InputType.NUMERIC:
                if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE) {
                    if (!AppUtils.checkNumberString(text)) {
                        AppUtils.showToast(
                                this, "Please enter a valid number.", false
                        );
                        return false;
                    }
                }
                sendTextInputMessage(text, message);
                break;
            case Constants.InputType.OPTIONS:
                sendTextInputMessage(text, message);
                break;
        }
        return true;
    }

    private void sendTextInputMessage(String text, Message message) {
        MessageResponse.MessageResponseBuilder responseBuilder
                = new MessageResponse.MessageResponseBuilder(AnaChatActivity.this);
        responseBuilder.
                inputTextString(text, message)
                .build().send();
    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        switch (type) {
            case Constants.MessagesTypeForUI.TEXT:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeText() != null
                        && message.getMessageInput().getInputTypeText().getInput() != null;
            case Constants.MessagesTypeForUI.LOCATION:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeLocation() != null
                        && message.getMessageInput().getInputTypeLocation().getInput() != null;
            case Constants.MessagesTypeForUI.DATE:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeDate() != null
                        && message.getMessageInput().getInputTypeDate().getInput() != null;
            case Constants.MessagesTypeForUI.NUMERIC:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeNumeric() != null
                        && message.getMessageInput().getInputTypeNumeric().getInput() != null;
            case Constants.MessagesTypeForUI.PHONE:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypePhone() != null
                        && message.getMessageInput().getInputTypePhone().getInput() != null;
            case Constants.MessagesTypeForUI.ADDRESS:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeAddress() != null
                        && message.getMessageInput().getInputTypeAddress().getInput() != null;
            case Constants.MessagesTypeForUI.TIME:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeTime() != null
                        && message.getMessageInput().getInputTypeTime().getInput() != null;
            case Constants.MessagesTypeForUI.EMAIL:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeEmail() != null
                        && message.getMessageInput().getInputTypeEmail().getInput() != null;
            case Constants.MessagesTypeForUI.MEDIA:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeMedia() != null
                        && message.getMessageInput().getInputTypeMedia().getInput() != null;
            case Constants.MessagesTypeForUI.OPTIONS:
                return message.getMessageInput() != null
                        && message.getMessageInput().getOptionsForeignCollection() != null
                        && message.getMessageInput().getInputForOptions() != null;
            case Constants.MessagesTypeForUI.SIMPLE_MESSAGE_TEXT:
                return message.getMessageSimple() != null &&
                        message.getMessageSimple().getMedia() == null
                        && message.getMessageSimple().getText() != null;
            case Constants.MessagesTypeForUI.SIMPLE_MESSAGE_MEDIA:
                return message.getMessageSimple() != null
                        && message.getMessageSimple().getMedia() != null;
            case Constants.MessagesTypeForUI.CAROUSEL_MESSAGE_FOR_INPUT:
                return message.getMessageCarousel() != null
                        && message.getMessageCarousel().getInput() != null;
            case Constants.MessagesTypeForUI.CAROUSEL_MESSAGE:
                return message.getMessageCarousel() != null
                        && message.getMessageCarousel().getItems() != null
                        && message.getMessageCarousel().getInput() == null;
            case Constants.MessagesTypeForUI.LIST:
                return message.getMessageInput() != null
                        && message.getMessageInput().getInputTypeList() != null
                        && message.getMessageInput().getInputTypeList().getInput() != null;
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    private void launchMediaIntent(int type) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                askPermission(type);
            }
            return;
        }
        switch (type) {
            case Constants.MediaType.IMAGE:
                startActivityForResult(Intent.createChooser(InputIntents.pickImageFromGallery(),
                        "Select Picture"), InputIntents.REQUEST_TAKE_GALLERY_IMAGE);
                break;
            case Constants.MediaType.VIDEO:
                startActivityForResult(Intent.createChooser(InputIntents.pickVideoFromGallery(),
                        "Select Video"), InputIntents.REQUEST_TAKE_GALLERY_VIDEO);
                break;
            case Constants.MediaType.FILE:
                startActivityForResult(Intent.createChooser(InputIntents.pickFileFromGallery(),
                        "Select File"), InputIntents.REQUEST_TAKE_FILE);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void askPermission(int code) {
        if (code == LOCATION_PERMISSION_REQUEST) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    code);
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE},
                code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MediaType.IMAGE:
                if (grantResults.length > 0) launchMediaIntent(Constants.MediaType.IMAGE);
                break;
            case Constants.MediaType.VIDEO:
                if (grantResults.length > 0) launchMediaIntent(Constants.MediaType.VIDEO);
                break;
            case Constants.MediaType.FILE:
                if (grantResults.length > 0) launchMediaIntent(Constants.MediaType.FILE);
                break;
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0) locationPlacesIntent();
                break;
        }
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(
                PreferencesManager.getsInstance(this).getUserName()
                , registerMessagesTypes(), imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
//        messagesAdapter.setHasStableIds(true);
        messagesList.setAdapter(messagesAdapter);
    }

    private MessageHolders registerMessagesTypes() {
        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        (byte) Constants.MessagesTypeForUI.TEXT,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        DefaultStringInputViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.ADDRESS,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        DefaultStringInputViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.EMAIL,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        OutcomingInputEmailMessageViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.LOCATION,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        OutcomingInputLocationMessageViewHolder.class,
                        R.layout.item_outcoming_input_media_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.MEDIA,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        OutcomingInputMediaMessageViewHolder.class,
                        R.layout.item_outcoming_input_media_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.NUMERIC,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        OutcomingInputNumericMessageViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.PHONE,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        OutcomingInputPhoneMessageViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.OPTIONS,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        DefaultStringInputViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.LIST,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        DefaultStringInputViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.TIME,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        DefaultStringInputViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.DATE,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        DefaultStringInputViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.CAROUSEL_MESSAGE,
                        IncomingCarouselMessageViewHolder.class,
                        R.layout.item_incoming_carousel_message,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.CAROUSEL_MESSAGE_FOR_INPUT,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        OutComingCarouselTextMessageViewHolder.class,
                        R.layout.item_outcoming_text_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.SIMPLE_MESSAGE_MEDIA,
                        IncomingSimpleMediaMessageViewHolder.class,
                        R.layout.item_incoming_simple_media_message,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.SIMPLE_MESSAGE_TEXT,
                        IncomingSimpleMessageViewHolder.class,
                        R.layout.item_incoming_text_message,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        this)
                .registerContentType((byte) Constants.MessagesTypeForUI.BLANK_MESSAGE,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        BlankMessageViewHolder.class,
                        R.layout.item_blank_message,
                        this);

        return holders;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.menu = menu;
//        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
//        onSelectionChanged(0);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_delete:
//                messagesAdapter.deleteSelectedMessages();
//                break;
//            case R.id.action_copy:
//                messagesAdapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
//                AppUtils.showToast(this, R.string.copied_message, true);
//                break;
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        PushConsumer.getInstance(this).clearQueue();
        ListenerManager.getInstance().removeChatMessageListener(this);
//        if (selectionCount == 0) {
//            super.onBackPressed();
//        } else {
//            messagesAdapter.unselectAllItems();
//        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (page == 0 || totalItemsCount < 20) return;
        swipeRefreshLayout.setRefreshing(true);
//        swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 8000);
        AnaCore.loadMoreMessages(this, totalItemsCount, page,
                AnaCore.getOldestTimeStamp(this));
    }

    @Override
    public void onSelectionChanged(int count) {
//        this.selectionCount = count;
//        menu.findItem(R.id.action_delete).setVisible(count > 0);
//        menu.findItem(R.id.action_copy).setVisible(count > 0);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return message -> {
            String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a",
                    Locale.getDefault())
                    .format(new Date(message.getTimestamp()));
            //TODO UPDATE HERE FOR SELECTIONS
            String text = null;
            if (text == null) text = "[attachment]";

            return String.format(Locale.getDefault(), "%s: %s (%s)",
                    "NFBOT_USER", text, createdAt);
        };
    }

    @Override
    public void onMessageUpdated(Message message, long oldTimestamp) {
//        messagesAdapter.update(message, oldTimestamp);
        messagesAdapter.updateTimeStamp(oldTimestamp, message.getTimestamp());
        checkLastMessage();
    }

    @Override
    public void onMessageInserted(Message message) {
        if (!isFirstHistoryLoaded) return;
        if (message.getSenderType() != Constants.SenderType.USER &&
                message.getMessageType() != Constants.MessageType.INPUT) {
            messagesAdapter.addLoadingIndicator();
            Handler handler = new Handler();
            handler.postDelayed(() -> messagesAdapter.addToStart(message, true), 1000);
        } else {
            messagesAdapter.addToStart(message, true);
        }
        checkLastMessage();
    }

    @Override
    public void onMessageDeleted(Message message) {
        messagesAdapter.delete(message);
    }

    @Override
    public void onConversationUpdate(List<Message> messages) {
        swipeRefreshLayout.setRefreshing(false);
        if (messages != null && messages.size() > 0) {
            messagesAdapter.addToEnd(messages, false);
        }
        checkLastMessage();
        if (messages != null && messages.size() == 0 && messagesAdapter.getItemCount() == 0)
            addGetStartedMessage();

        isFirstHistoryLoaded = true;
    }

    @Override
    public void onHistoryLoaded(List<Message> messages) {
        swipeRefreshLayout.setRefreshing(false);
        if (!isFirstHistoryLoaded) return;
        if (messages != null && messages.size() > 0) {
            messagesAdapter.addToEnd(messages, false);
        }
    }

    private void addGetStartedMessage() {
//        AnaCore.addWelcomeMessage(this);
        showActionButton("GET STARTED");
    }

    private void checkLastMessage() {
        Message lastMessage = getLastMessage();
        if (lastMessage != null && lastMessage.getMessageType() == Constants.MessageType.INPUT)
            updateBottomUIForInputType(lastMessage);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.InputType.LOCATION) {
            ListenerManager.getInstance().notifySendLocation(data);
            return;
        }
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filePath = PathUtil.getPath(this, uri);
            if (filePath == null)
                return;
            int type = 0;
            if (requestCode == InputIntents.REQUEST_TAKE_GALLERY_VIDEO) {
                type = Constants.MediaType.VIDEO;
            }
            if (requestCode == InputIntents.REQUEST_TAKE_GALLERY_IMAGE) {
                type = Constants.MediaType.IMAGE;
            }
            if (requestCode == InputIntents.REQUEST_TAKE_FILE) {
                type = Constants.MediaType.FILE;
            }
            Message message = getLastMessage();
            startActivity(MediaPreviewActivity.startIntent(this, filePath, type,
                    message.getMId(), message.getMessageInput().getMandatory(),
                    message.getSessionId()));
        }
    }

//    private void sendLocation(LatLng latLng) {
//        Message message
//                = getLastMessage();
//        Input input = new Input();
//        input.setLocation(new DefaultLocation(BigDecimal.valueOf(latLng.latitude),
//                BigDecimal.valueOf(latLng.longitude)));
//        MessageResponse.MessageResponseBuilder responseBuilder
//                = new MessageResponse.MessageResponseBuilder(AnaChatActivity.this);
//        responseBuilder.
//                inputLocation(message, input)
//                .build().send();
//    }

    @Override
    public void onClick(View view) {
        switch (btnAction.getText().toString()) {
            case "SELECT DATE":
                showDateDialog();
                break;
            case "SEND ADDRESS":
                showAddressDialog();
                break;
            case "SELECT VIDEO":
                launchMediaIntent(Constants.MediaType.VIDEO);
                break;
            case "SELECT IMAGE":
                launchMediaIntent(Constants.MediaType.IMAGE);
                break;
            case "SELECT AUDIO":
                launchMediaIntent(Constants.MediaType.AUDIO);
                break;
            case "SELECT FILE":
                launchMediaIntent(Constants.MediaType.FILE);
                break;
            case "SELECT TIME":
                showTimeDialog();
                break;
            case "GET STARTED":
                AnaCore.addWelcomeMessage(AnaChatActivity.this);
                break;
            case "SEND LOCATION":
                locationPlacesIntent();
//                InputIntents.pickLocation(this, this);
                break;
            case "SELECT FROM LIST":
                showListDialog();
                break;
        }
    }

    private void showDateDialog() {
        Message message
                = getLastMessage();
        Integer minVal = 0;
        DatePickerDialog datePickerDialog;
        if (message.getMessageInput()
                .getInputTypeDate().getDateRange() != null) {
            datePickerDialog = new DatePickerDialog(
                    this, AnaChatActivity.this, Integer.valueOf(message.getMessageInput()
                    .getInputTypeDate().getDateRange().getMin().getYear()),
                    Integer.valueOf(message.getMessageInput().getInputTypeDate().getDateRange().getMin().getMonth()),
                    Integer.valueOf(message.getMessageInput().getInputTypeDate().getDateRange().getMin().getMday()));
            String string_date = message.getMessageInput().getInputTypeDate()
                    .getDateRange().getMin().getMday() + "-" + message.getMessageInput()
                    .getInputTypeDate().getDateRange().getMin().getMonth() + "-" +
                    message.getMessageInput().getInputTypeDate().getDateRange().getMin().getYear();
            String ending_date = message.getMessageInput().getInputTypeDate()
                    .getDateRange().getMax().getMday() + "-" + message.getMessageInput()
                    .getInputTypeDate().getDateRange().getMax().getMonth() + "-" +
                    message.getMessageInput().getInputTypeDate().getDateRange().getMax().getYear();
            if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE)
                setDateRangeOnPicker(string_date, ending_date, datePickerDialog);
        } else {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(this,
                    AnaChatActivity.this, year, month, day);
        }

        datePickerDialog.show();
    }

    private void setDateRangeOnPicker(String string_date, String ending_date, DatePickerDialog datePickerDialog) {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date start = f.parse(string_date);
            Date end = f.parse(ending_date);
            datePickerDialog.getDatePicker().setMinDate(start.getTime());
            datePickerDialog.getDatePicker().setMaxDate(end.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showTimeDialog() {
        Message message
                = getLastMessage();
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        RangeTimePickerDialog rangeTimePickerDialog =
                new RangeTimePickerDialog(this, this, hour, minute, true);
        if (message.getMessageInput().getInputTypeTime().getTimeRange() != null &&
                message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE) {
            rangeTimePickerDialog.setMin(Integer.valueOf(message.
                    getMessageInput().getInputTypeTime().getTimeRange()
                    .getMin().getHour()), Integer.valueOf(message.
                    getMessageInput().getInputTypeTime().getTimeRange()
                    .getMin().getMinute()));
        }
        rangeTimePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Input input = new Input();
        Time time =
                new Time(
                        String.valueOf(timePicker.getHour()),
                        String.valueOf(timePicker.getMinute()), "0");
        input.setTime(time);
        MessageResponse.MessageResponseBuilder responseBuilder
                = new MessageResponse.MessageResponseBuilder(AnaChatActivity.this);
        responseBuilder.
                inputTime(getLastMessage(), input)
                .build().send();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Input input = new Input();
        com.anachat.chatsdk.internal.model.inputdata.Date date =
                new com.anachat.chatsdk.internal.model.inputdata.Date(
                        String.valueOf(datePicker.getYear()),
                        String.valueOf(datePicker.getMonth()), String.valueOf(datePicker.getDayOfMonth()));
        input.setDate(date);
        MessageResponse.MessageResponseBuilder responseBuilder
                = new MessageResponse.MessageResponseBuilder(AnaChatActivity.this);
        responseBuilder.
                inputDate(getLastMessage(), input)
                .build().send();

    }

    private void updateBottomUIForInputType(Message message) {

        switch (message.getMessageInput().getInputType()) {
            case Constants.InputType.LOCATION:
                if (message.getMessageInput().getInputTypeLocation().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hide();
                hideOptionView();
                showActionButton(getResources().getString(R.string.action_button_location));
                break;
            case Constants.InputType.DATE:
                if (message.getMessageInput().getInputTypeDate().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hide();
                hideOptionView();
                showActionButton(getResources().getString(R.string.action_button_date));
                break;
            case Constants.InputType.NUMERIC:
                if (message.getMessageInput().getInputTypeNumeric().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hideOptionView();
                hideActionButton();
//                input.updateHint(getResources().getString(R.string.hint_number));
                updateHint("");
                updateInputTypeOfKeyboard(message.getMessageInput().getInputType());
                show();
                break;
            case Constants.InputType.PHONE:
                if (message.getMessageInput().getInputTypePhone().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hideOptionView();
                hideActionButton();
//                input.updateHint(getResources().getString(R.string.hint_phone));
                updateHint("");
                updateInputTypeOfKeyboard(message.getMessageInput().getInputType());
                show();
                break;
            case Constants.InputType.ADDRESS:
                if (message.getMessageInput().getInputTypeAddress().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hide();
                hideOptionView();
                showActionButton(getResources().getString(R.string.action_button_address));
                break;
            case Constants.InputType.MEDIA:
                if (message.getMessageInput().getInputTypeMedia().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hide();
                hideOptionView();
                showActionButton(getMediaNameForType(message));
                break;
            case Constants.InputType.TEXT:
                if (message.getMessageInput().getInputTypeText().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hideOptionView();
                hideActionButton();
//                input.updateHint(getResources().getString(R.string.hint_text));
                updateHint(message.getMessageInput().
                        getInputTypeText().getTextInputAttr().getPlaceHolder());
                updateInputTypeOfKeyboard(message.getMessageInput().getInputType());
                show();
                break;
            case Constants.InputType.TIME:
                if (message.getMessageInput().getInputTypeTime().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hide();
                hideOptionView();
                showActionButton(getResources().getString(R.string.action_button_time));
                break;
            case Constants.InputType.EMAIL:
                if (message.getMessageInput().getInputTypeEmail().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hideOptionView();
                hideActionButton();
                //  updateHint(getResources().getString(R.string.hint_email));
                updateInputTypeOfKeyboard(message.getMessageInput().getInputType());
                show();
                break;
            case Constants.InputType.OPTIONS:
                if (message.getMessageInput().getInputForOptions() != null) {
                    hideBottomViews();
                    return;
                }
                if (message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE) {
                    hide();
                } else {
                    updateHint("");
                    updateInputTypeOfKeyboard(0);
                    show();
                }
                hideActionButton();
                showOptionsView(message);
                break;
            case Constants.InputType.LIST:
                if (message.getMessageInput().getInputTypeList().getInput() != null) {
                    hideBottomViews();
                    return;
                }
                hide();
                hideOptionView();
                showActionButton(getResources().getString(R.string.action_button_list));
                break;
        }

    }

    private void hideBottomViews() {
        hideKeyPad();
        hide();
        hideOptionView();
        hideActionButton();
    }

    private void hideActionButton() {
        if (btnAction.getVisibility() != GONE) {
            btnAction.setVisibility(GONE);
        }
    }

    private void showActionButton(String btnText) {
        if (btnAction.getVisibility() != VISIBLE) {
            btnAction.setVisibility(VISIBLE);
        }
        btnAction.setText(btnText);
    }

    private void hideOptionView() {
        if (rvOptions.getVisibility() != GONE) {
            rvOptions.setVisibility(GONE);
        }
    }

    private void hideOptions() {
        rvOptions.setVisibility(GONE);
    }

    private void showOptionsView(Message message) {
        if (rvOptions.getVisibility() != VISIBLE) {
            rvOptions.setVisibility(VISIBLE);
        }
        if (optionsAdapter == null) {
            optionsAdapter = new OptionsAdapter(imageLoader);
            rvOptions.setAdapter(optionsAdapter);
        }
        optionsAdapter.setData(message);
    }

    private void showAddressDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View addressDialogView = factory.inflate(R.layout.dialog_address, null);
        final AlertDialog addressDialog = new AlertDialog.Builder(this).create();
        addressDialog.setView(addressDialogView);
        final TextInputLayout tiLine = addressDialogView.findViewById(R.id.input_layout_address_line);
        final TextInputLayout tiArea = addressDialogView.findViewById(R.id.input_layout_locality);
        final TextInputLayout tiCity = addressDialogView.findViewById(R.id.input_layout_city);
        final TextInputLayout tiState = addressDialogView.findViewById(R.id.input_layout_state);
        final TextInputLayout tiCountry = addressDialogView.findViewById(R.id.input_layout_country);
        final TextInputLayout tiPinCode = addressDialogView.findViewById(R.id.input_layout_pin);
        final TextView tvTittle = addressDialogView.findViewById(R.id.tv_title);
        final TextView tvSend = addressDialogView.findViewById(R.id.tv_send);
        tvTittle.setBackgroundColor(
                Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        GradientDrawable drawable = (GradientDrawable) tvSend.getBackground();
        drawable.setColor(Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        tvSend.setBackground(drawable);
        tvSend.setOnClickListener(view -> {
            Message message
                    = getLastMessage();
            Address address
                    = new Address(tiLine.getEditText().getText().toString().trim(),
                    tiArea.getEditText().getText().toString().trim(),
                    tiCity.getEditText().getText().toString().trim(),
                    tiState.getEditText().getText().toString().trim(),
                    tiCountry.getEditText().getText().toString().trim(),
                    tiPinCode.getEditText().getText().toString().trim());
            Boolean isInputValid = true;
            if (message.getMessageInput().getInputTypeAddress().getRequiredFields() != null &&
                    message.getMessageInput().getMandatory() == Constants.FCMConstants.MANDATORY_TRUE) {
                for (String field :
                        message.getMessageInput().getInputTypeAddress().getRequiredFields()) {
                    switch (field) {
                        case "area":
                            if (address.getArea() == null || address.getArea().isEmpty()) {
                                tiArea.setError("Invalid Address");
                                isInputValid = false;
                            }
                            break;
                        case "country":
                            if (address.getCountry() == null ||
                                    address.getCountry().isEmpty()) {
                                tiCountry.setError("Invalid Country");
                                isInputValid = false;
                            }
                            break;
                        case "pin":
                            if (address.getPin() == null || address.getPin().isEmpty()) {
                                tiPinCode.setError("Invalid Pin");
                                isInputValid = false;
                            }
                            break;
                        case "city":
                            if (address.getCity() == null || address.getCity().isEmpty()) {
                                tiCity.setError("Invalid City");
                                isInputValid = false;
                            }
                            break;
                        case "state":
                            if (address.getState() == null || address.getState().isEmpty()) {
                                tiState.setError("Invalid State");
                                isInputValid = false;
                            }
                            break;
                        case "line1":
                            if (address.getLine1() == null || address.getLine1().isEmpty()) {
                                tiLine.setError("Invalid Line1");
                                isInputValid = false;
                            }
                            break;
                    }
                }
            }
            if (isInputValid) {
                sendAddress(message, address);
                addressDialog.dismiss();
            }


        });
        addressDialog.show();
    }

    private void showListDialog() {
        Message message
                = getLastMessage();
        LayoutInflater factory = LayoutInflater.from(this);
        final View listDialogView = factory.inflate(R.layout.dialog_list_input, null);
        final AlertDialog listDialog = new AlertDialog.Builder(this).create();
        listDialog.setView(listDialogView);
        final RecyclerView rvList = listDialogView.findViewById(R.id.input_list);
        final TextView tvSend = listDialogView.findViewById(R.id.tv_send);
        final TextView tvTittle = listDialogView.findViewById(R.id.tv_title);
        tvTittle.setBackgroundColor(
                Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        GradientDrawable drawable = (GradientDrawable) tvSend.getBackground();
        drawable.setColor(Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        rvList.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        InputListOptionsAdapter inputListOptionsAdapter =
                new InputListOptionsAdapter(this, message);
        rvList.setAdapter(inputListOptionsAdapter);
        tvSend.setOnClickListener(view -> {
            if (inputListOptionsAdapter.getValues().isEmpty()) {
                Toast.makeText(this, "Please make Select from List",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            listDialog.dismiss();
            sendTextInputMessage(inputListOptionsAdapter.getValues(), message);

        });
        listDialog.show();
    }

    private void sendAddress(Message message, Address address) {
        Input input = new Input();
        input.setAddress(address);
        MessageResponse.MessageResponseBuilder responseBuilder
                = new MessageResponse.MessageResponseBuilder(AnaChatActivity.this);
        responseBuilder.
                inputAddress(message, input)
                .build().send();
    }

    private String getMediaNameForType(Message message) {
        switch (message.getMessageInput().getInputTypeMedia().
                getMediaType()) {
            case Constants.MediaType.VIDEO:
                return getResources().getString(R.string.action_button_video);
            case Constants.MediaType.IMAGE:
                return getResources().getString(R.string.action_button_image);
            case Constants.MediaType.AUDIO:
                return getResources().getString(R.string.action_button_audio);
            case Constants.MediaType.FILE:
                return getResources().getString(R.string.action_button_file);
            default:
                return "NO MEDIA";
        }
    }

    private void hideKeyPad() {
        try {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message getLastMessage() {
        return AnaCore.getLastMessage(this);
    }

    public int getLineCount() {
        return edInput.getLineCount();
    }

    public void updateHint(String hint) {
        edInput.setHint(hint);
    }

    public void updateInputTypeOfKeyboard(int i) {
        switch (i) {
            case Constants.InputType.PHONE:
                edInput.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case Constants.InputType.NUMERIC:
                edInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case Constants.InputType.TEXT:
                edInput.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case Constants.InputType.EMAIL:
                edInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            default:
                edInput.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    public void hide() {
        if (input.getVisibility() != GONE)
            input.setVisibility(GONE);
    }

    public void show() {
        if (input.getVisibility() != VISIBLE)
            input.setVisibility(VISIBLE);
    }

    private void locationPlacesIntent() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                askPermission(LOCATION_PERMISSION_REQUEST);
            }
            return;
        }
        ListenerManager.getInstance().notifyPickLocation(AnaChatActivity.this);
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NFChatUtils.isNetworkConnected(context)) {
                tvDesc.setText(getString(R.string.no_internet_line_available));
                ivOnline.setColorFilter(ContextCompat.getColor(context, R.color.red_800));
            } else {
                tvDesc.setText(getString(R.string.internet_line_available));
                ivOnline.setColorFilter(ContextCompat.getColor(context, R.color.green_online));
                MessengerCoreMethods.syncMessages(AnaChatActivity.this);
            }
        }
    };

    @Override
    public void onRefresh() {

    }
}