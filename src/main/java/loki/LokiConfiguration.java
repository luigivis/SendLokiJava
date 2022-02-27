package loki;

import loki.interfaces.LokiStreamInterface;

public class LokiConfiguration{

    static String lokiURL;
    static String lokiCustomLabel;

    public static void LoadLokiConfiguration() {
        Configure.loadProperties();
        lokiURL = Configure.getValueString("lokiURL");
        lokiCustomLabel = Configure.getValueString("lokiCustomLabel");
    }

    public static LokiController getLokiConfiguration(){
        LoadLokiConfiguration();
        return LokiDto.createAndStart(lokiURL, "loki", "$0Noc#$espA");
    }
    public static LokiStreamInterface info(String ubication){
        LoadLokiConfiguration();
        return getLokiConfiguration().createStream(LokiDto.info().l("service", lokiCustomLabel).l("ubicacion", ubication));
    }
    public static LokiStreamInterface error(String ubication){
        LoadLokiConfiguration();
        return getLokiConfiguration().createStream(LokiDto.critical().l("service", lokiCustomLabel).l("ubicacion", ubication));
    }
    public static void main(String[] args) {


        LokiController lokiController = getLokiConfiguration();
        LokiStreamInterface error = error(LokiConfiguration.class.getName());
        LokiStreamInterface info = info(LokiConfiguration.class.getName());

        info.log("LUIGI");
        info.log("Hola Mundo");
        info.log("mensaje 2");
        for (int i = 0; i<=50; i++){
            info.log("Mensaje "+i);
            if(i == 5){
                error.log("ERROR "+i);
            }
            System.out.println(i);
        }

        lokiController.softStop();
    }

}
