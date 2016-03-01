package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.tools.PopUp;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

import java.io.IOException;

/**
 * Created by vaibhavr on 01/03/16.
 */
public class StartStopExample {


    public static final String name = "Example 100";

    public static void main(String[] args){

        try {
            NetworkManager networkManager = new NetworkManager(NetworkManager.ConfigMode.EDGE, name);
            PopUp.info(name, "Starting jxta network");

            PeerGroup connectedVia = networkManager.getNetPeerGroup();
            PopUp.info(name, "Connected via peer group: " + connectedVia.getPeerGroupName());

            PopUp.info(name, "Stopping jxta network");
            networkManager.stopNetwork();
        } catch (IOException e) {
            PopUp.error(name, e.toString());
        }

    }
}
