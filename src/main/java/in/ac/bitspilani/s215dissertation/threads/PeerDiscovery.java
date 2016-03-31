package in.ac.bitspilani.s215dissertation.threads;

import in.ac.bitspilani.s215dissertation.Context;
import in.ac.bitspilani.s215dissertation.listener.PeerDiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.PeerGroup;

import java.io.IOException;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class PeerDiscovery implements Runnable{

    private Context context;

    public PeerDiscovery(Context context){
        this.context = context;
    }

    public void run() {
        PeerGroup peerGroup = context.getPeerGroup();
        DiscoveryService discoveryService = peerGroup.getDiscoveryService();
        PeerDiscoveryListener peerDiscoveryListener = new PeerDiscoveryListener(context);
        discoveryService.addDiscoveryListener(peerDiscoveryListener);
        while (true)
        {
            System.out.println("Fetching peers");
            try {
                discoveryService.getLocalAdvertisements(DiscoveryService.PEER, null, null);
                discoveryService.getRemoteAdvertisements(null, net.jxta.discovery.DiscoveryService.PEER, null, null, 5);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
