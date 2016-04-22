package in.ac.bitspilani.s215dissertation.threads;

import in.ac.bitspilani.s215dissertation.Context;
import in.ac.bitspilani.s215dissertation.listener.PipeDiscoveryListener;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.PeerGroup;

import java.io.IOException;

/**
 * Created by vaibhavr on 27/03/16.
 */
public class PipeDiscovery implements Runnable{

    private Context context;

    public PipeDiscovery(Context context){
        this.context = context;
    }

    public void run() {
        PeerGroup peerGroup = context.getPeerGroup();
        DiscoveryService discoveryService = peerGroup.getDiscoveryService();
        PipeDiscoveryListener pipeDiscoveryListener = new PipeDiscoveryListener(context);
        discoveryService.addDiscoveryListener(pipeDiscoveryListener);
        boolean rendezvous = Boolean.valueOf(PeerProperties.getProperty(PeerProperties.PEER_RENDEZVOUZ)).booleanValue();
        while (true) {
            System.out.println("Fetching pipes");
            try {
                if(rendezvous) {
                    discoveryService.getLocalAdvertisements(DiscoveryService.ADV, null, null);
                }
                discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 10);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
