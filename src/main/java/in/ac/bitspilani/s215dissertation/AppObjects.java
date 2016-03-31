package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.bean.Peer;
import net.jxta.impl.protocol.PipeAdv;
import net.jxta.pipe.OutputPipe;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class AppObjects {

    private static Map<String, Peer> peers = new HashMap<String, Peer>();

    private static Map<String, PipeAdv> pipes = new HashMap<String, PipeAdv>();

    private static Map<String, File> files = new HashMap<String, File>();

    private static Set<String> ignoreFiles = new HashSet<String>();
    static {
        ignoreFiles.add(".DS_Store");
    }

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

    public static void insertPipe(Context context, PipeAdv pipeAdv){
        OutputPipe outputPipe = null;
        if(!pipes.containsKey(pipeAdv.getName())){
            pipes.put(pipeAdv.getName(), pipeAdv);
        }
    }

    public static Map<String, PipeAdv> getPipes(){
        return pipes;
    }

    public static void insertFile(String fileName, File f){
        if(!files.containsKey(fileName)){
            files.put(fileName, f);
        }
    }

    public static void deleteFile(String fileName){
        if(files.containsKey(fileName)){
            files.remove(fileName);
        }
    }

    public static boolean contains(String fileName){
        return files.containsKey(fileName);
    }

    public static boolean ignore(String filename){
        return ignoreFiles.contains(filename);
    }

}
