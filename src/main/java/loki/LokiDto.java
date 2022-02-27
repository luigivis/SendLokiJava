package loki;

import loki.interfaces.LokiCollectorInterface;
import loki.interfaces.LokiMonitorInterface;
import loki.utilities.Labels;

import java.util.Map;

public class LokiDto {
    public LokiDto() {
    }

    /**
     * Funcion para enviar logs a Grafana Loki
     * @param url se usa para conocer la instancia del server
     * @param user Opcional
     * @param pass Opcional
     * @return retorna LokiController
     * @see LokiController
     */
    public static LokiController createAndStart(String url, String user, String pass) {
        return createAndStart(url, user, pass, 5000);
    }

    /**
     * Funcion para enviar logs a Grafana Loki
     * @param url se usa para conocer la instancia del server
     * @param user Opcional
     * @param pass Opcional
     * @param connectTimeout Opcional Tiempo de espera
     * @return
     */
    public static LokiController createAndStart(String url, String user, String pass, int connectTimeout) {
        return createAndStart(url, user, pass, connectTimeout, new JsonLokiCollector(), new ErrorLokiMonitor());
    }

    public static LokiController createAndStart(String url, String user, String pass, LokiCollectorInterface logCollector, LokiMonitorInterface logMonitor) {
        return createAndStart(url, user, pass, 5000, logCollector, logMonitor);
    }

    public static LokiController createAndStart(String url, String user, String pass, int connectTimeout, LokiCollectorInterface logCollector, LokiMonitorInterface logMonitor) {
        LokiSenderSettings logSenderSettings = LokiSenderSettings.create().setUrl(url);
        logSenderSettings.setUser(user);
        logSenderSettings.setPassword(pass);
        logSenderSettings.setConnectTimeout(connectTimeout);
        return (new LokiController(logCollector, new LokiSender(logSenderSettings), logMonitor)).start();
    }

    public static Labels l(String labelName, String labelValue) {
        return (new Labels()).l(labelName, labelValue);
    }

    public static Labels l(Map<String, String> map) {
        return (new Labels()).l(map);
    }

    public static Labels l(Labels other) {
        return new Labels(other);
    }

    public static Labels critical() {
        return (new Labels()).critical();
    }

    public static Labels fatal() {
        return (new Labels()).fatal();
    }

    public static Labels warning() {
        return (new Labels()).warning();
    }

    public static Labels info() {
        return (new Labels()).info();
    }

    public static Labels debug() {
        return (new Labels()).debug();
    }

    public static Labels verbose() {
        return (new Labels()).verbose();
    }

    public static Labels trace() {
        return (new Labels()).trace();
    }

    public static Labels unknown() {
        return (new Labels()).unknown();
    }
}
