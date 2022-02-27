package loki;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configure {

    static Properties props = new Properties();

    public static void loadProperties() {

        try {
            props.load(new FileInputStream("resources/conf.properties"));
        } catch (IOException e) {
            System.err.println("Error al leer archivo de configuracion");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String getValueString(String key) {

        String val = props.getProperty(key);
        System.out.println(val);
        if (val == null) {
            return "";
        }
        return val;
    }

    public static String getValueString(String key, String defaultValue) {

        String val = props.getProperty(key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    public static int getValueInt(String key) {
        try {
            return Integer.parseInt(props.getProperty(key));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getValueInt(String key, int defecto) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(defecto)));
        } catch (Exception e) {
            return defecto;
        }
    }

}
