package loki;

import loki.interfaces.LokiCollectorInterface;
import loki.interfaces.LokiMonitorInterface;
import loki.interfaces.LokiStreamInterface;
import loki.utilities.Labels;

import java.util.Map;

public class LokiController {
    private static final long LOG_WAIT_TIME = 100L;
    private static final long DEFAULT_SOFT_STOP_WAIT_TIME = 2000L;
    private static final long DEFAULT_HARD_STOP_WAIT_TIME = 1000L;
    private final LokiCollectorInterface logCollector;
    private final LokiSender logSender;
    private final LokiMonitorInterface logMonitor;
    private Thread workerThread = null;
    private boolean softFinishing = false;
    private boolean softExit = false;

    public LokiController(LokiCollectorInterface logCollector, LokiSender logSender, LokiMonitorInterface logMonitor) {
        this.logCollector = logCollector;
        this.logSender = logSender;
        this.logMonitor = logMonitor;
        this.logSender.getSettings().setContentType(logCollector.contentType());
        this.logSender.setLogMonitor(logMonitor);
    }

    public LokiStreamInterface createStream(Map<String, String> labels) {
        return this.logCollector.createStream(labels);
    }

    public LokiStreamInterface createStream(Labels labels) {
        return this.logCollector.createStream(labels.getMap());
    }

    public LokiController start() {
        this.workerThread = new Thread("LokiController.workerThread") {
            public void run() {
                LokiController.this.workerLoop();
            }
        };
        this.workerThread.start();
        return this;
    }

    public synchronized LokiController softStopAsync() {
        this.softFinishing = true;
        return this;
    }

    public boolean isHardStopped() {
        return this.workerThread.getState() == Thread.State.TERMINATED;
    }

    /**
     * Funcion para destruir lo que no se ha enviado
     * @param interruptTimeout Opciona Tiempo de espera hasta que se destruya
     * @return
     */
    public LokiController hardStop(long interruptTimeout) {
        try {
            this.softStopAsync();
            this.workerThread.interrupt();
            this.workerThread.join(interruptTimeout);
        } catch (InterruptedException var4) {
            this.logMonitor.onException(var4);
        }

        return this;
    }

    public LokiController hardStop() {
        return this.hardStop(1000L);
    }

    public LokiController softStop(long softTimeout) {
        try {
            this.softStopAsync();
            this.workerThread.join(softTimeout);
        } catch (InterruptedException var4) {
            this.logMonitor.onException(var4);
        }

        return this;
    }

    /**
     * Funcion que espera que se envien todos los datos
     * @return
     */
    public LokiController softStop() {
        return this.softStop(2000L);
    }

    public boolean isSoftStopped() {
        synchronized(this) {
            return this.softExit;
        }
    }

    public void workerLoop() {
        while(true) {
            try {
                boolean doLastCheck = false;
                synchronized(this) {
                    if (this.softFinishing) {
                        doLastCheck = true;
                    }
                }

                int anyLogs;
                if (doLastCheck) {
                    anyLogs = this.logCollector.waitForLogs(1L);
                } else {
                    anyLogs = this.logCollector.waitForLogs(100L);
                }

                if (anyLogs > 0) {
                    byte[] logs = this.logCollector.collect();
                    if (logs != null) {
                        this.logSender.send(logs);
                    }
                }

                if (doLastCheck) {
                    synchronized(this) {
                        this.softExit = true;
                    }

                    this.logMonitor.onWorkerThreadExit(true);
                    return;
                }
            } catch (InterruptedException var7) {
                this.logMonitor.onException(var7);
                this.logMonitor.onWorkerThreadExit(false);
                return;
            } catch (Exception var8) {
                this.logMonitor.onException(var8);
            }
        }
    }
}