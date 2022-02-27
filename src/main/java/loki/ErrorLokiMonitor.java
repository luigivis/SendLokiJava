package loki;


import loki.interfaces.LokiMonitorInterface;

public class ErrorLokiMonitor implements LokiMonitorInterface {
    public ErrorLokiMonitor() {
    }

    public void send(byte[] message) {
    }

    public void sendOk(int status) {
    }

    public void sendErr(int status, String message) {
        System.err.println("Unexpected server response status: " + status + ": " + message);
    }

    public void onException(Exception exception) {
        exception.printStackTrace();
    }

    public void onWorkerThreadExit(boolean isSoft) {
        if (isSoft) {
            System.out.println("Worker thread exited correctly.");
        } else {
            System.err.println("Worker thread exited by interrupting.");
        }

    }
}
