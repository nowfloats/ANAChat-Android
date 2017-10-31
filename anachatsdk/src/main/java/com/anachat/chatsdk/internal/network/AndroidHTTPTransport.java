package com.anachat.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */

import android.os.Build.VERSION;
import android.util.Log;

import com.anachat.chatsdk.internal.utils.StringUtils;
import com.anachat.chatsdk.internal.utils.constants.NetworkConstants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private static final String TAG = "Nf_HTTPTrnsport";

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
                    if ("https://".equals(NetworkConstants.scheme)) {
                        connection = (new URL(request.url)).openConnection();
                        this.fixSSLSocketProtocols((HttpsURLConnection) connection);
                    } else {
                        connection = (new URL(request.url)).openConnection();
                    }

                    ((HttpURLConnection) connection).setRequestMethod(request.method.name());
                    ((HttpURLConnection) connection).setConnectTimeout(request.timeout);
                    Iterator e = request.headers.iterator();

                    while (e.hasNext()) {
                        KeyValuePair networkException4 = (KeyValuePair) e.next();
                        ((HttpURLConnection) connection).setRequestProperty(networkException4.key, networkException4.value);
                    }

                    if (request instanceof POSTRequest) {
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

                    Log.d("Helpshift_HTTPTrnsport", "Api : " + request.url + " \t Status : " + e3 + "\t Thread : " + Thread.currentThread().getName());
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

    private void fixSSLSocketProtocols(HttpsURLConnection connection) {
        if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
            ArrayList enableProtocols = new ArrayList();
            enableProtocols.add("TLSv1.2");
            ArrayList disableProtocols = new ArrayList();
            disableProtocols.add("SSLv3");
            SSLSocketFactory sslSocketFactory = connection.getSSLSocketFactory();
            connection.setSSLSocketFactory(new NowFloatsSSLSocketFactory(sslSocketFactory, enableProtocols, disableProtocols));
        }

    }

    private boolean isInValidKeyForHeader(String key) {
        return "screenshot".equals(key) || "originalFileName".equals(key);
    }

    private Response upload(UploadRequest uploadRequest) {
        Object connection = null;
        boolean var41 = false;

        Response var26;
        NetworkException networkException1;
        label216:
        {
            try {
                NetworkException networkException;
                try {
                    var41 = true;
                    URL e = new URL(uploadRequest.url);
                    String networkException3 = "--";
                    String boundary = "*****";
                    String lineEnd = "\r\n";
                    if ("https://".equals(NetworkConstants.scheme)) {
                        connection = (HttpsURLConnection) e.openConnection();
                        this.fixSSLSocketProtocols((HttpsURLConnection) connection);
                    } else {
                        connection = (HttpURLConnection) e.openConnection();
                    }

                    ((HttpURLConnection) connection).setDoInput(true);
                    ((HttpURLConnection) connection).setDoOutput(true);
                    ((HttpURLConnection) connection).setUseCaches(false);
                    ((HttpURLConnection) connection).setRequestMethod(uploadRequest.method.name());
                    ((HttpURLConnection) connection).setConnectTimeout(uploadRequest.timeout);
                    ((HttpURLConnection) connection).setReadTimeout(uploadRequest.timeout);
                    List headers = uploadRequest.headers;
                    Iterator outputStream = headers.iterator();

                    while (outputStream.hasNext()) {
                        KeyValuePair data = (KeyValuePair) outputStream.next();
                        ((HttpURLConnection) connection).setRequestProperty(data.key, data.value);
                    }

                    DataOutputStream outputStream1 = new DataOutputStream(((HttpURLConnection) connection).getOutputStream());
                    outputStream1.writeBytes(networkException3 + boundary + lineEnd);
                    Map data1 = uploadRequest.data;
                    ArrayList mapKeys = new ArrayList(data1.keySet());
                    Iterator screenshotFile = mapKeys.iterator();

                    String fileName;
                    while (screenshotFile.hasNext()) {
                        fileName = (String) screenshotFile.next();
                        if (!this.isInValidKeyForHeader(fileName)) {
                            String fileInputStream = (String) data1.get(fileName);
                            outputStream1.writeBytes("Content-Disposition: form-data; name=\"" + fileName + "\"; " + lineEnd);
                            outputStream1.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
                            outputStream1.writeBytes("Content-Length: " + fileInputStream.length() + lineEnd);
                            outputStream1.writeBytes(lineEnd);
                            outputStream1.writeBytes(fileInputStream + lineEnd);
                            outputStream1.writeBytes(networkException3 + boundary + lineEnd);
                        }
                    }

                    File screenshotFile1 = new File((String) data1.get("file"));
                    fileName = (String) data1.get("originalFileName");
                    if (fileName == null) {
                        fileName = screenshotFile1.getName();
                    }

                    FileInputStream fileInputStream1 = new FileInputStream(screenshotFile1);
                    outputStream1.writeBytes(networkException3 + boundary + lineEnd);
                    outputStream1.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + lineEnd);
                    outputStream1.writeBytes("Content-Type: " + uploadRequest.mimeType + lineEnd);
                    outputStream1.writeBytes("Content-Length: " + screenshotFile1.length() + lineEnd);
                    outputStream1.writeBytes(lineEnd);
                    int maxBufferSize = 1048576;
                    int bytesAvailable = fileInputStream1.available();
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];

                    for (int bytesRead = fileInputStream1.read(buffer, 0, bufferSize); bytesRead > 0; bytesRead = fileInputStream1.read(buffer, 0, bufferSize)) {
                        outputStream1.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream1.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    }

                    outputStream1.writeBytes(lineEnd);
                    outputStream1.writeBytes(networkException3 + boundary + networkException3 + lineEnd);
                    fileInputStream1.close();
                    outputStream1.flush();
                    outputStream1.close();
                    int status = ((HttpURLConnection) connection).getResponseCode();
                    StringBuilder responseStr = new StringBuilder();
                    InputStream responseStream = ((HttpURLConnection) connection).getInputStream();
                    InputStreamReader inputStream = new InputStreamReader(responseStream);
                    BufferedReader rd = new BufferedReader(inputStream);

                    String line;
                    while ((line = rd.readLine()) != null) {
                        responseStr.append(line);
                    }

                    String response = responseStr.toString();
                    if (status >= 200 && status < 300) {
                        var26 = new Response(status, response, (List) null);
                        var41 = false;
                        break label216;
                    }

                    var26 = new Response(status, (String) null, (List) null);
                    var41 = false;
                } catch (UnknownHostException var45) {
                    networkException = NetworkException.UNKNOWN_HOST;
                    networkException.route = uploadRequest.url;
                    throw RootAPIException.wrap(var45, networkException, "Upload error");
                } catch (SocketException var46) {
                    networkException = NetworkException.NO_CONNECTION;
                    networkException.route = uploadRequest.url;
                    throw RootAPIException.wrap(var46, networkException, "Upload error");
                } catch (SSLPeerUnverifiedException var47) {
                    networkException = NetworkException.SSL_PEER_UNVERIFIED;
                    networkException.route = uploadRequest.url;
                    throw RootAPIException.wrap(var47, networkException, "Upload error");
                } catch (SSLHandshakeException var48) {
                    networkException = NetworkException.SSL_HANDSHAKE;
                    networkException.route = uploadRequest.url;
                    throw RootAPIException.wrap(var48, networkException, "Upload error");
                } catch (Exception var49) {
                    networkException = NetworkException.GENERIC;
                    networkException.route = uploadRequest.url;
                    throw RootAPIException.wrap(var49, networkException, "Upload error");
                }
            } finally {
                if (var41) {
                    try {
                        if (connection != null) {
                            ((HttpURLConnection) connection).disconnect();
                        }
                    } catch (Exception var44) {
                        NetworkException networkException2 = NetworkException.GENERIC;
                        networkException2.route = uploadRequest.url;
                        throw RootAPIException.wrap(var44, networkException2, "Network error");
                    }

                }
            }

            try {
                if (connection != null) {
                    ((HttpURLConnection) connection).disconnect();
                }

                return var26;
            } catch (Exception var43) {
                networkException1 = NetworkException.GENERIC;
                networkException1.route = uploadRequest.url;
                throw RootAPIException.wrap(var43, networkException1, "Network error");
            }
        }

        try {
            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }

            return var26;
        } catch (Exception var42) {
            networkException1 = NetworkException.GENERIC;
            networkException1.route = uploadRequest.url;
            throw RootAPIException.wrap(var42, networkException1, "Network error");
        }
    }
}
