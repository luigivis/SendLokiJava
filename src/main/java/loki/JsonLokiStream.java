package loki;

import loki.interfaces.LokiStreamInterface;
import loki.utilities.Utils;

import java.util.Iterator;
import java.util.Map;

public class JsonLokiStream implements LokiStreamInterface {
    private final JsonLokiCollector collector;
    private final StringBuilder b = new StringBuilder("{\"stream\":{");
    private final String initialSequenceWithHeaders;
    private int cachedLogsCount = 0;

    public JsonLokiStream(JsonLokiCollector collector, Map<String, String> labels) {
        this.collector = collector;
        boolean isFirst = true;
        Iterator var4 = labels.entrySet().iterator();

        while (var4.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var4.next();
            if (isFirst) {
                isFirst = false;
            } else {
                this.b.append(',');
            }

            this.b.append('"');
            this.b.append((String) entry.getKey());
            this.b.append('"');
            this.b.append(':');
            this.b.append('"');
            Utils.escapeJsonString(this.b, (CharSequence) entry.getValue());
            this.b.append('"');
        }

        this.b.append("},\"values\":[");
        this.initialSequenceWithHeaders = this.b.toString();
    }

    public void log(long timestampMs, String line) {
        synchronized (this) {
            if (this.cachedLogsCount != 0) {
                this.b.append(',');
            }

            ++this.cachedLogsCount;
            this.b.append("[\"");
            this.b.append(timestampMs);
            this.b.append("000000\",\"");
            Utils.escapeJsonString(this.b, line);
            this.b.append("\"]");
        }

        this.collector.logOccurred();
    }

    public void release() {
        this.collector.onStreamReleased(this);
    }

    public void closeStreamsEntryTag() {
        this.b.append("]}");
    }

    public StringBuilder getStringBuilder() {
        return this.b;
    }

    public void clear() {
        this.b.setLength(0);
        this.b.append(this.initialSequenceWithHeaders);
        this.cachedLogsCount = 0;
    }

    public synchronized String flush() {
        if (this.cachedLogsCount == 0) {
            return null;
        } else {
            this.closeStreamsEntryTag();
            String result = this.b.toString();
            this.clear();
            return result;
        }
    }
}
