package com.anachat.chatsdk.internal.network;

/**
 * Created by lookup on 09/10/17.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class AnaSSLSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory sslSocketFactory;
    private List<String> enableProtocols;
    private List<String> disableProtocols;

    public AnaSSLSocketFactory(SSLSocketFactory sslSocketFactory, List<String> enableProtocols, List<String> disableProtocols) {
        this.sslSocketFactory = sslSocketFactory;
        this.enableProtocols = enableProtocols;
        this.disableProtocols = disableProtocols;
    }

    public String[] getDefaultCipherSuites() {
        return this.sslSocketFactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.sslSocketFactory.getSupportedCipherSuites();
    }

    public Socket createSocket() throws IOException {
        Socket socket = this.sslSocketFactory.createSocket();
        return this.updateProtocols(socket);
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        Socket socket = this.sslSocketFactory.createSocket(s, host, port, autoClose);
        return this.updateProtocols(socket);
    }

    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = this.sslSocketFactory.createSocket(host, port);
        return this.updateProtocols(socket);
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        Socket socket = this.sslSocketFactory.createSocket(host, port, localHost, localPort);
        return this.updateProtocols(socket);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket socket = this.sslSocketFactory.createSocket(host, port);
        return this.updateProtocols(socket);
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        Socket socket = this.sslSocketFactory.createSocket(address, port, localAddress, localPort);
        return this.updateProtocols(socket);
    }

    Socket updateProtocols(Socket socket) {
        if(socket != null && socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket)socket;
            String[] enabledProtocolArray = sslSocket.getEnabledProtocols();
            if(enabledProtocolArray == null) {
                return sslSocket;
            } else {
                ArrayList enabledProtocols = new ArrayList(Arrays.asList(enabledProtocolArray));
                String[] supportedProtocolsArray = sslSocket.getSupportedProtocols();
                Object supportedProtocolsList = new ArrayList();
                if(supportedProtocolsArray != null) {
                    supportedProtocolsList = Arrays.asList(supportedProtocolsArray);
                }

                ArrayList protocolsToEnable = new ArrayList();
                if(this.enableProtocols != null && this.enableProtocols.size() > 0) {
                    Iterator var8 = this.enableProtocols.iterator();

                    while(var8.hasNext()) {
                        String protocol = (String)var8.next();
                        if(!enabledProtocols.contains(protocol) && ((List)supportedProtocolsList).contains(protocol)) {
                            protocolsToEnable.add(protocol);
                        }
                    }
                }

                enabledProtocols.addAll(protocolsToEnable);
                if(this.disableProtocols != null && this.disableProtocols.size() > 0) {
                    enabledProtocols.removeAll(this.disableProtocols);
                }

                sslSocket.setEnabledProtocols((String[])enabledProtocols.toArray(new String[enabledProtocols.size()]));
                return socket;
            }
        } else {
            return socket;
        }
    }
}
