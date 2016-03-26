package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class JxtaGroup {

    public PeerGroup joinGroup(){
        PeerGroup connectedVia = null;
        try {
            Properties props = PeerProperties.props;
            String name = props.getProperty(PeerProperties.PEER_NAME);
            PeerID pid = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, name.getBytes());
            NetworkManager networkManager = new NetworkManager(NetworkManager.ConfigMode.EDGE, name);
            NetworkConfigurator networkConfigurator = networkManager.getConfigurator();
            configureRendezvouz(props, networkConfigurator);
            configureNetworkConfigurator(props, name, networkConfigurator);
            networkManager.setPeerID(pid);
            networkManager.startNetwork();
            connectedVia = networkManager.getNetPeerGroup();
            connectedVia.getRendezVousService().setAutoStart(false);
            boolean connected = networkManager.waitForRendezvousConnection(25000);
            if(connected){
                System.out.println("Connected to Rendevouz");
            }
        } catch (PeerGroupException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectedVia;
    }

    private void configureNetworkConfigurator(Properties props, String name, NetworkConfigurator networkConfigurator) {
        networkConfigurator.setName(name);
        networkConfigurator.setUseMulticast(false);
        networkConfigurator.setTcpPort(Integer.valueOf(props.getProperty(PeerProperties.PEER_TCP_PORT)));
        networkConfigurator.setTcpEnabled(true);
        networkConfigurator.setTcpIncoming(true);
        networkConfigurator.setTcpOutgoing(true);
        networkConfigurator.setUseMulticast(false);
    }

    private void configureRendezvouz(Properties props, NetworkConfigurator networkConfigurator) throws UnknownHostException {
        networkConfigurator.clearRendezvousSeeds();
        String theSeed = "tcp://" + InetAddress.getLocalHost().getHostAddress() + ":" + props.getProperty(PeerProperties.PEER_RENDEZVOUZ_PORT);
        URI rendezvouzUri = URI.create(theSeed);
        networkConfigurator.addSeedRendezvous(rendezvouzUri);
    }

}
