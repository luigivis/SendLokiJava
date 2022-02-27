package loki.interfaces;

public interface LokiStreamInterface {
    void log(long var1, String var3);

    default void log(String line) {
        this.log(System.currentTimeMillis(), line);
    }

    void release();
}
