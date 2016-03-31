package in.ac.bitspilani.s215dissertation.listener;

import in.ac.bitspilani.s215dissertation.AppObjects;
import in.ac.bitspilani.s215dissertation.Context;
import in.ac.bitspilani.s215dissertation.bean.Peer;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.impl.protocol.PeerAdv;
import net.jxta.protocol.DiscoveryResponseMsg;

import java.util.Enumeration;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class PeerDiscoveryListener implements DiscoveryListener {

    private Context context;

    public PeerDiscoveryListener(Context context) {
        this.context = context;
    }

    public void discoveryEvent(DiscoveryEvent discoveryEvent) {
        /*System.out.println("Discovery Event response is " + discoveryEvent.getResponse().toString());*/
        System.out.println("Peer discovered at " + System.currentTimeMillis());
        DiscoveryResponseMsg response = discoveryEvent.getResponse();
        Enumeration<Advertisement> advertisements = response.getAdvertisements();
        while(advertisements.hasMoreElements()){
            Advertisement advertisement = advertisements.nextElement();
            if(PeerAdv.class.getName().equals(advertisement.getClass().getName())){
                PeerAdv peerAdv = (PeerAdv) advertisement;
                String jxtaId = peerAdv.getPeerID().toString();
                if(!AppObjects.isPeerAdded(peerAdv.getPeerID().toString())){
                    Peer newPeer = new Peer(jxtaId, peerAdv.getName());
                    AppObjects.insertPeer(newPeer);
                    context.populatePeers(newPeer);
                }
            }
        }
    }
}
