package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.tools.PopUp;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by vaibhavr on 01/03/16.
 */
public class LocalConfigurationExample {

    public static final String name = "LocalConfig";

    public static void main(String[] args){

        try {
            String localPath = "." + System.getProperty("file.separator") + "Config";
            File configFile = new File(localPath);
            PopUp.info(name, "Removing any existing local configuration");
            NetworkManager.RecursiveDelete(configFile);

            NetworkManager networkManager = new NetworkManager(NetworkManager.ConfigMode.EDGE, name, configFile.toURI());
            PopUp.info(name, "Setting peer name");
            NetworkConfigurator networkConfigurator = networkManager.getConfigurator();
            networkConfigurator.setName("My peer name");
            PopUp.info(name, "Saving local config in:" + configFile.getCanonicalPath());
            networkConfigurator.save();
        } catch (IOException e) {
            PopUp.error(name, e.toString());
        }

    }
}
