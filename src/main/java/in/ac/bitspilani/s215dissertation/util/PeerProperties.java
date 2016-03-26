package in.ac.bitspilani.s215dissertation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class PeerProperties {

    public static final String PEER_MONITOR_DIR="peer.monitor.dir";

    public static final String PEER_NAME="peer.name";

    public static final String PEER_TCP_PORT="peer.tcp.port";

    public static final String PEER_RENDEZVOUZ_PORT="peer.rendezvouz.port";

    public static final Properties props = new Properties();

    public static void readProperties(){
        File configFile = new File(PeerProperties.class.getClassLoader().getResource("app.properties").getFile());
        try {
            FileInputStream in = new FileInputStream(configFile);
            props.load(in);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found");
        } catch (IOException e) {
            System.out.println("IO exception occurred");
        }
    }

}
