package in.ac.bitspilani.s215dissertation.threads;

import in.ac.bitspilani.s215dissertation.PeerManagement;
import net.jxta.discovery.DiscoveryService;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class DiscoveryThread implements Runnable{

    private PeerManagement context;

    private DiscoveryService discoveryService;

    public DiscoveryThread(PeerManagement context, DiscoveryService discoveryService){
        this.context = context;
        this.discoveryService = discoveryService;
    }

    public void run() {
        while (true)

        {
            System.out.println("Fetching peers");
            discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 5);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
