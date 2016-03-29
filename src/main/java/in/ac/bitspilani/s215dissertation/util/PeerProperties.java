package in.ac.bitspilani.s215dissertation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class PeerProperties {

    public static final String PEER_MONITOR_DIR="peer.monitor.dir";

    public static final String PEER_NAME="peer.name";

    public static final String PEER_TCP_PORT="peer.tcp.port";

    public static final String PEER_RENDEZVOUZ_PORT="peer.rendezvouz.port";

    public static final String PEER_RENDEZVOUZ="peer.rendezvouz";

    public static final Properties props = new Properties();

    public static void readProperties(){
        File configFile = getConfigFile();
        try {
            FileInputStream in = new FileInputStream(configFile);
            props.load(in);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found");
        } catch (IOException e) {
            System.out.println("IO exception occurred");
        }
    }

    public static String getProperty(String key){
        return props.getProperty(key);
    }

    private static File getConfigFile() {
        File configFile = new File("app.properties");
        if(!configFile.exists()){
            System.out.println("Config file not found. Looking for command line");
            /*URL resource = classLoader.getResource("src/main/resources/app.properties");
            ClassLoader classLoader = PeerProperties.class.getClassLoader();*/
            configFile = new File(System.getProperty("user.dir") + "/src/main/resources/app.properties");
        }
        return configFile;
    }

}
