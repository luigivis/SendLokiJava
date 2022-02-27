package loki.interfaces;

import java.util.Map;

public interface LokiCollectorInterface {
    LokiStreamInterface createStream(Map<String, String> var1);

    byte[] collect();

    String contentType();

    int waitForLogs(long var1) throws InterruptedException;
}
