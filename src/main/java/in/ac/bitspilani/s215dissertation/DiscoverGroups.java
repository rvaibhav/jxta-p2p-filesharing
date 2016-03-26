package in.ac.bitspilani.s215dissertation;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by vaibhavr on 19/03/16.
 */
public class DiscoverGroups implements DiscoveryListener{

    public void startDiscovery(){
        try {
            String name="Discoverer";
            PeerID pid = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, name.getBytes());
            NetworkManager networkManager = new NetworkManager(NetworkManager.ConfigMode.EDGE, "The pirate");
            NetworkConfigurator networkConfigurator = networkManager.getConfigurator();
            networkConfigurator.setName(name);
            networkConfigurator.setUseMulticast(true);
            networkManager.setPeerID(pid);
            networkManager.startNetwork();
            PeerGroup connectedVia = networkManager.getNetPeerGroup();
            boolean connected = networkManager.waitForRendezvousConnection(25000);
            DiscoveryService discoveryService = connectedVia.getDiscoveryService();
            discoveryService.addDiscoveryListener(this);
            discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, "Name", "Vaibhav", 5);
            discoveryService.getRemoteAdvertisements(null,DiscoveryService.ADV, null, null,5);
            while(true){
                System.out.println("VAIBHAV-Inside while loop");
                discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, "Name", "Vaibhav", 5);
                discoveryService.getRemoteAdvertisements(null,DiscoveryService.ADV, null, null,5);
                Thread.sleep(30000);
            }
        }catch (PeerGroupException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        DiscoverGroups discoverGroups = new DiscoverGroups();
        discoverGroups.startDiscovery();


    }

    public void discoveryEvent(DiscoveryEvent event) {
        System.out.println("Discovery event is " + event.getResponse());
    }
}
