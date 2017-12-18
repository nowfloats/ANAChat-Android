package com.anachat.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */

import android.os.Build.VERSION;
import android.util.Log;

import com.anachat.chatsdk.internal.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;

public class AndroidHTTPTransport implements HTTPTransport {
    private static final String TAG = "Ana_HTTPTrnsport";

    public AndroidHTTPTransport() {
    }

    public Response makeRequest(Request request) {
        return request instanceof UploadRequest ? this.upload((UploadRequest) UploadRequest.class.cast(request)) : this.makePostOrGetRequest(request);
    }

    private Response makePostOrGetRequest(Request request) {
        Object connection = null;
        boolean var26 = false;

        Response var11;
        label226:
        {
            Response response1;
            try {
                NetworkException networkException;
                try {
                    var26 = true;
                    connection = (new URL(request.url)).openConnection();
                    if (request.url.startsWith("https"))
                        this.fixSSLSocketProtocols((HttpsURLConnection) connection);
                    ((HttpURLConnection) connection).setRequestMethod(request.method.name());
                    ((HttpURLConnection) connection).setConnectTimeout(request.timeout);
                    Iterator e = request.headers.iterator();

                    while (e.hasNext()) {
                        KeyValuePair networkException4 = (KeyValuePair) e.next();
                        ((HttpURLConnection) connection).setRequestProperty(networkException4.key, networkException4.value);
                    }

                    if (request instanceof POSTRequest && request.method.name().equals("POST")) {
                        ((HttpURLConnection) connection).setDoOutput(true);
                        OutputStream e2 = ((HttpURLConnection) connection).getOutputStream();
                        BufferedWriter networkException5 = new BufferedWriter(new OutputStreamWriter(e2, "UTF-8"));
                        networkException5.write((POSTRequest.class.cast(request)).query);
                        networkException5.flush();
                        networkException5.close();
                        e2.flush();
                        e2.close();
                    }

                    int e3 = ((HttpURLConnection) connection).getResponseCode();
                    ArrayList networkException6 = new ArrayList();
                    Map headers = ((HttpURLConnection) connection).getHeaderFields();
                    Iterator response = headers.keySet().iterator();

                    while (response.hasNext()) {
                        String e1 = (String) response.next();
                        if (!StringUtils.isEmpty(e1)) {
                            networkException6.add(new KeyValuePair(e1, (String) ((List) headers.get(e1)).get(0)));
                        }
                    }

                    if (e3 >= 200 && e3 < 300) {
                        Object response2 = new BufferedInputStream(((HttpURLConnection) connection).getInputStream());
                        Iterator e4 = headers.keySet().iterator();

                        while (e4.hasNext()) {
                            String networkException1 = (String) e4.next();
                            if (!StringUtils.isEmpty(networkException1) && networkException1.equals("Content-Encoding") && ((String) ((List) headers.get(networkException1)).get(0)).equals("gzip")) {
                                response2 = new GZIPInputStream((InputStream) response2);
                            }
                        }

                        InputStreamReader e5 = new InputStreamReader((InputStream) response2);
                        BufferedReader networkException7 = new BufferedReader(e5);
                        StringBuilder responseStr = new StringBuilder();

                        String line;
                        while ((line = networkException7.readLine()) != null) {
                            responseStr.append(line);
                        }

                        ((InputStream) response2).close();
                        e5.close();
                        var11 = new Response(e3, responseStr.toString(), networkException6);
                        var26 = false;
                        break label226;
                    }

                    Log.d("Anachat_HTTPTrnsport", "Api : " + request.url + " \t Status : " + e3 + "\t Thread : " + Thread.currentThread().getName());
                    response1 = new Response(e3, (String) null, networkException6);
                    var26 = false;
                } catch (UnknownHostException var30) {
                    networkException = NetworkException.UNKNOWN_HOST;
                    networkException.route = request.url;
                    throw RootAPIException.wrap(var30, networkException, "Network error");
                } catch (SocketException var31) {
                    networkException = NetworkException.NO_CONNECTION;
                    networkException.route = request.url;
                    throw RootAPIException.wrap(var31, networkException, "Network error");
                } catch (SSLPeerUnverifiedException var32) {
                    networkException = NetworkException.SSL_PEER_UNVERIFIED;
                    networkException.route = request.url;
                    throw RootAPIException.wrap(var32, networkException, "Network error");
                } catch (SSLHandshakeException var33) {
                    networkException = NetworkException.SSL_HANDSHAKE;
                    networkException.route = request.url;
                    throw RootAPIException.wrap(var33, networkException, "Network error");
                } catch (IOException var34) {
                    networkException = NetworkException.GENERIC;
                    networkException.route = request.url;
                    throw RootAPIException.wrap(var34, networkException, "Network error");
                }
            } finally {
                if (var26) {
                    try {
                        if (connection != null) {
                            ((HttpURLConnection) connection).disconnect();
                        }
                    } catch (Exception var29) {
                        NetworkException networkException3 = NetworkException.GENERIC;
                        networkException3.route = request.url;
                        throw RootAPIException.wrap(var29, networkException3, "Network error");
                    }

                }
            }

            try {
                if (connection != null) {
                    ((HttpURLConnection) connection).disconnect();
                }

                return response1;
            } catch (Exception var28) {
                NetworkException networkException8 = NetworkException.GENERIC;
                networkException8.route = request.url;
                throw RootAPIException.wrap(var28, networkException8, "Network error");
            }
        }

        try {
            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }

            return var11;
        } catch (Exception var27) {
            NetworkException networkException2 = NetworkException.GENERIC;
            networkException2.route = request.url;
            throw RootAPIException.wrap(var27, networkException2, "Network error");
        }
    }


//    public Response makeNetworkRequest(Request request){
//        try {
//
//            URL url = new URL(request.url); // here is your URL path
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(15000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod(request.method.name());
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            Iterator e = request.headers.iterator();
//            while (e.hasNext()) {
//                KeyValuePair networkException4 = (KeyValuePair) e.next();
//                conn.setRequestProperty(networkException4.key, networkException4.value);
//            }
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getPostDataString(postDataParams));
//
//            writer.flush();
//            writer.close();
//            os.close();
//
//            int responseCode=conn.getResponseCode();
//
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                BufferedReader in=new BufferedReader(new
//                        InputStreamReader(
//                        conn.getInputStream()));
//
//                StringBuffer sb = new StringBuffer("");
//                String line="";
//
//                while((line = in.readLine()) != null) {
//
//                    sb.append(line);
//                    break;
//                }
//
//                in.close();
//                return sb.toString();
//
//            }
//            else {
//                return new String("false : "+responseCode);
//            }
//        }
//        catch(Exception e){
//            return new String("Exception: " + e.getMessage());
//        }
//    }

    private void fixSSLSocketProtocols(HttpsURLConnection connection) {
        if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
            ArrayList enableProtocols = new ArrayList();
            enableProtocols.add("TLSv1.2");
            ArrayList disableProtocols = new ArrayList();
            disableProtocols.add("SSLv3");
            SSLSocketFactory sslSocketFactory = connection.getSSLSocketFactory();
            connection.setSSLSocketFactory(new AnaSSLSocketFactory(sslSocketFactory, enableProtocols, disableProtocols));
        }

    }

    private boolean isInValidKeyForHeader(String key) {
        return "screenshot".equals(key) || "originalFileName".equals(key);
    }

    private Response upload(UploadRequest uploadRequest) {
        Response response = new Response(500, "", null);
        try {
            MultipartUtility multipartUtility = new MultipartUtility(uploadRequest.url);
            multipartUtility.addFilePart("file", new File(uploadRequest.data.get("file")));
            return multipartUtility.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }
}



