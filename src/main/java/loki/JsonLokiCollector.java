package loki;

import loki.interfaces.LokiCollectorInterface;
import loki.interfaces.LokiStreamInterface;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

    public class JsonLokiCollector implements LokiCollectorInterface {
        private final List<JsonLokiStream> streams = new ArrayList();
        private int logEntriesCount = 0;
        private final Object logEntriesLock = new Object();

        public JsonLokiCollector() {
        }

        public synchronized LokiStreamInterface createStream(Map<String, String> labels) {
            JsonLokiStream stream = new JsonLokiStream(this, labels);
            this.streams.add(stream);
            return stream;
        }

        public synchronized void onStreamReleased(LokiStreamInterface stream) {
            this.streams.remove((JsonLokiStream) stream);
        }

        public byte[] collect() {
            String collectedAsString = this.collectAsString();
            return collectedAsString == null ? null : collectedAsString.getBytes(StandardCharsets.UTF_8);
        }

        public synchronized String collectAsString() {
            StringBuilder b = new StringBuilder("{\"streams\":[");
            boolean isFirst = true;
            boolean anyStreamNotEmpty = false;
            Iterator var4 = this.streams.iterator();

            while(var4.hasNext()) {
                JsonLokiStream stream = (JsonLokiStream)var4.next();
                String streamData = stream.flush();
                if (streamData != null) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        b.append(',');
                    }

                    b.append(streamData);
                    anyStreamNotEmpty = true;
                }
            }

            if (!anyStreamNotEmpty) {
                return null;
            } else {
                b.append("]}");
                return b.toString();
            }
        }

        public String contentType() {
            return "application/json";
        }

        void logOccurred() {
            synchronized(this.logEntriesLock) {
                ++this.logEntriesCount;
                this.logEntriesLock.notify();
            }
        }

        public int waitForLogs(long timeout) throws InterruptedException {
            synchronized(this.logEntriesLock) {
                if (this.logEntriesCount == 0) {
                    this.logEntriesLock.wait(timeout);
                }

                int logEntriesCountCopy = this.logEntriesCount;
                this.logEntriesCount = 0;
                return logEntriesCountCopy;
            }
        }
    }

