package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.bean.Peer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class AppObjects {

    private static Map<String, Peer> peers = new HashMap<String, Peer>();

    public static void insertPeer(Peer peer){
        if(!peers.containsKey(peer.getJxtaId())){
            peers.put(peer.getJxtaId(), peer);
        }
    }

    public static boolean isPeerAdded(String jxtaId){
        return peers.containsKey(jxtaId);
    }

    public static Map<String, Peer> getPeers(){
        return peers;
    }

}
