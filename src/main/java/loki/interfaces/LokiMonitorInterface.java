package loki.interfaces;

public interface LokiMonitorInterface {
    void send(byte[] var1);

    void sendOk(int var1);

    void sendErr(int var1, String var2);

    void onException(Exception var1);

    void onWorkerThreadExit(boolean var1);
}
