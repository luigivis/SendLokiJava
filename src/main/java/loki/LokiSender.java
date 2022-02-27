package loki;

import loki.interfaces.LokiMonitorInterface;
import loki.utilities.Base64Coder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LokiSender {
    final LokiSenderSettings settings;
    private LokiMonitorInterface logMonitor = null;
    private final URL url;

    public LokiSender(LokiSenderSettings settings) {
        this.settings = settings;

        try {
            this.url = new URL(settings.getUrl());
        } catch (MalformedURLException var3) {
            throw new RuntimeException("Failed to initialize URL with: [" + settings.getUrl() + "].", var3);
        }
    }

    public LokiSenderSettings getSettings() {
        return this.settings;
    }

    public LokiMonitorInterface getLogMonitor() {
        return this.logMonitor;
    }

    public void setLogMonitor(LokiMonitorInterface logMonitor) {
        this.logMonitor = logMonitor;
    }

    void send(byte[] message) {
        this.logMonitor.send(message);
        OutputStream outputStream = null;

        try {
            HttpURLConnection connection = (HttpURLConnection)this.url.openConnection();
            connection.setConnectTimeout(this.settings.getConnectTimeout());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "close");
            connection.setRequestProperty("Content-Type", this.settings.getContentType());
            connection.setRequestProperty("Content-Length", Integer.toString(message.length));
            String responseMessage;
            if (this.settings.getUser() != null && this.settings.getPassword() != null) {
                String authHeaderContentString = this.settings.getUser() + ":" + this.settings.getPassword();
                responseMessage = Base64Coder.encodeString(authHeaderContentString);
                connection.setRequestProperty("Authorization", "Basic " + responseMessage);
            }

            connection.setAllowUserInteraction(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            outputStream = connection.getOutputStream();
            outputStream.write(message);
            outputStream.close();
            outputStream = null;
            int responseCode = connection.getResponseCode();
            if (responseCode != 200 && responseCode != 204) {
                responseMessage = connection.getResponseMessage();
                this.logMonitor.sendErr(responseCode, responseMessage);
            } else {
                this.logMonitor.sendOk(responseCode);
            }
        } catch (IOException var13) {
            throw new RuntimeException("Failed to send logs.", var13);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException var12) {
                    this.logMonitor.onException(var12);
                }
            }

        }

    }
}