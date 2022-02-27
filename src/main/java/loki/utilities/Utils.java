package loki.utilities;

public class Utils {
    public Utils() {
    }

    public static void escapeJsonString(StringBuilder b, CharSequence text) {
        int i = 0;

        for(int length = text.length(); i < length; ++i) {
            char c = text.charAt(i);
            switch(c) {
                case '"':
                    b.append("\\\"");
                    break;
                case '\\':
                    b.append("\\\\");
                    break;
                default:
                    if (c > 31) {
                        b.append(c);
                    } else {
                        b.append("\\u");
                        String hex = "000" + Integer.toHexString(c);
                        b.append(hex.substring(hex.length() - 4));
                    }
            }
        }

    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException var3) {
            throw new RuntimeException("Thread.sleep() has failed.", var3);
        }
    }
}

