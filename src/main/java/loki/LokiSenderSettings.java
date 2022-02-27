package loki;


    public class LokiSenderSettings {
        public static final int DEFAULT_CONNECT_TIMEOUT = 5000;
        private String url = null;
        private String user = null;
        private String password = null;
        private String contentType = "application/json";
        private int connectTimeout = 5000;

        public LokiSenderSettings() {
        }

        public static LokiSenderSettings create() {
            return new LokiSenderSettings();
        }

        public String getUrl() {
            return this.url;
        }

        public LokiSenderSettings setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getUser() {
            return this.user;
        }

        public LokiSenderSettings setUser(String user) {
            this.user = user;
            return this;
        }

        public String getPassword() {
            return this.password;
        }

        public LokiSenderSettings setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getContentType() {
            return this.contentType;
        }

        public LokiSenderSettings setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public LokiSenderSettings setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public int getConnectTimeout() {
            return this.connectTimeout;
        }
    }

