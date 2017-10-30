//package com.nowfloats.chatsdk.internal.network;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.nowfloats.chatsdk.internal.database.MessageRepository;
//import com.nowfloats.chatsdk.internal.model.MessageResponse;
//import com.nowfloats.chatsdk.internal.utils.constants.Constants;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.http.Body;
//import retrofit2.http.Headers;
//import retrofit2.http.Multipart;
//import retrofit2.http.POST;
//import retrofit2.http.Part;
//
///**
// * Created by lookup on 04/09/17.
// */
//
//@SuppressWarnings("ALL")
//public interface NFChatService {
//
//    String ENDPOINT = "https://chat.withfloats.com/";
//
//    @POST("api")
//    @Headers("Content-type: application/json")
//    Call<MessageResponse> sendMessage(@Body MessageResponse messageResponse);
//
//    @Multipart
//    @POST("files")
//    Call<ResponseBody> uploadFile(@Part MultipartBody.Part filePart);
//
//    /******** Helper class that sets up a new services *******/
//    class Creator {
//        public static NFChatService newNFAPIService() {
//            return RetrofitClient
//                    .getClient(NFChatService.ENDPOINT)
//                    .create(NFChatService.class);
//        }
//
//        public static void startSyncing(final MessageResponse messageResponse, final Context context) {
//            if (messageResponse.isFileUpload()) {
//                NFChatService.Creator.uploadFile(messageResponse, context);
//                return;
//            }
//            ApiCalls.sendMessage(messageResponse);
////            String json =new Gson().toJson(messageResponse);
//            NFChatService.Creator.newNFAPIService().sendMessage(messageResponse).
//                    enqueue(new Callback<MessageResponse>() {
//                        @Override
//                        public void onResponse(Call<MessageResponse> call,
//                                               Response<MessageResponse> response) {
//                            if (response.isSuccessful() && response.body() != null &&
//                                    response.body().getMessage() != null) {
//                                response.body().getMessage().setMessageType(
//                                        response.body().getData().getType());
//                                response.body().getMessage().setTimestamp(
//                                        messageResponse.getMessage().getTimestamp());
//                                response.body().getMessage().setSyncWithServer(true);
//                                response.body().setOnlyUpdate(true);
//                                MessageRepository.getInstance(context).
//                                        handleMessageResponse(response.body());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<MessageResponse> call, Throwable t) {
//                            Log.d("NFChatService", "Error in API!" + t.getMessage());
//                        }
//                    });
//        }
//
//        public static void uploadFile(final MessageResponse messageResponse, final Context context) {
//            File file = new File(messageResponse.getData().getContent().getInput().
//                    getMedia().get(0).getPreviewUrl());
//            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),
//                    RequestBody.create(MediaType.parse("multipart/form-data"), file));
//            NFChatService.Creator.newNFAPIService().uploadFile(filePart).
//                    enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call,
//                                               Response<ResponseBody> response) {
//                            if (response.isSuccessful()) {
//                                try {
//                                    messageResponse.setFileUpload(false);
//                                    JSONObject responseJson = new
//                                            JSONObject(response.body().string());
//                                    if (responseJson.has(Constants.FCMConstants.LINKS)) {
//                                        JSONArray jsonArray = new
//                                                JSONArray(responseJson.getString
//                                                (Constants.FCMConstants.LINKS)
//                                        );
//                                        JSONObject jsonLink = jsonArray.getJSONObject(0);
//                                        String url = jsonLink.
//                                                getString(Constants.FCMConstants.HREF);
//                                        messageResponse.getData().
//                                                getContent().getInput().getMedia().get(0).
//                                                setUrl(url);
//                                        messageResponse.getData().getContent().getInput().
//                                                getMedia().get(0).setPreviewUrl(url);
//                                        startSyncing(messageResponse, context);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.d("NFChatService", "Error in API!" + t.getMessage());
//                        }
//                    });
//        }
//    }
//
//
//}
