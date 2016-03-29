package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.bean.Peer;
import in.ac.bitspilani.s215dissertation.listener.PeerMessageListener;
import in.ac.bitspilani.s215dissertation.listener.SendMessageListener;
import net.jxta.impl.protocol.PipeAdv;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeService;
import net.jxta.util.JxtaBiDiPipe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class AppObjects {

    private static Map<String, Peer> peers = new HashMap<String, Peer>();

    private static Map<String, PipeAdv> pipes = new HashMap<String, PipeAdv>();

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

    public static void insertPipe(PeerManagement context, PipeAdv pipeAdv){
        /*JxtaBiDiPipe jxtaBiDiPipe = null;*/
        OutputPipe outputPipe = null;
        if(!pipes.containsKey(pipeAdv.getName())){
            /*try {
                *//*jxtaBiDiPipe = new JxtaBiDiPipe(context.getPeerGroup(), pipeAdv, peerMessageListener);*//*
                *//*PipeService pipeService = context.getPeerGroup().getPipeService();
                pipeService.createOutputPipe(pipeAdv, new SendMessageListener());*//*
            } catch (IOException e) {
                e.printStackTrace();
            }
            *//*if(outputPipe != null) {
                pipes.put(pipeAdv.getName(), outputPipe);
            }*/
            pipes.put(pipeAdv.getName(), pipeAdv);
        }
    }

    public static Map<String, PipeAdv> getPipes(){
        return pipes;
    }





}
