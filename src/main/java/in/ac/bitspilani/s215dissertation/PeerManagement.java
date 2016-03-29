package in.ac.bitspilani.s215dissertation;

/**
 * Created by vaibhavr on 19/03/16.
 */

import in.ac.bitspilani.s215dissertation.bean.Peer;
import in.ac.bitspilani.s215dissertation.listener.PeerDiscoveryListener;
import in.ac.bitspilani.s215dissertation.listener.PeerMessageListener;
import in.ac.bitspilani.s215dissertation.listener.PipeDiscoveryListener;
import in.ac.bitspilani.s215dissertation.threads.MonitorDirectory;
import in.ac.bitspilani.s215dissertation.threads.PeerDiscovery;
import in.ac.bitspilani.s215dissertation.threads.PipeDiscovery;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Pipe;


/**
 * Created by vaibhavr on 18/03/16.
 */
public class PeerManagement {

    private static PeerGroupID peerGroupId = null;

    private JPanel panel1;
    private JList list1;
    private JList list2;

    private JPanel leftPanel;

    private JPanel rightPanel;
    private JLabel leftHeader;
    private JLabel rightHeader;

    private DefaultListModel peerList= new DefaultListModel();

    private JSplitPane splitPane;

    private PeerGroup peerGroup;

    private PeerMessageListener peerMessageListener;

    private InputPipe myPipe;

    public PeerGroup getPeerGroup(){
        return peerGroup;
    }

    public JSplitPane getSplitPane(){
        return splitPane;
    }

    private void createSplitPane() {
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, rightPanel);
        leftHeader = new JLabel("Peers");
        list1 = new JList();
        list2 = new JList();
        list1.setLayoutOrientation(JList.VERTICAL);
        list2.setLayoutOrientation(JList.VERTICAL);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        leftPanel.add(leftHeader);
        leftPanel.add(list1);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        list1.setBackground(Color.WHITE);
        list2.setBackground(Color.WHITE);
        Dimension minimumSize = new Dimension(100, 50);
        leftPanel.setMinimumSize(minimumSize);
        rightPanel.setMinimumSize(minimumSize);
    }

    private void createAndShowGUI() throws Exception{
        JFrame frame = new JFrame("PeerManagement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.createSplitPane();
        frame.add(this.getSplitPane());
        frame.pack();
        frame.setVisible(true);
        Dimension minimumSize = new Dimension(200, 200);
        frame.setSize(minimumSize);
        PeerProperties.readProperties();
        joinPeerGroup();
    }

    public static void main(String[] args){
        final PeerManagement peerManagement = new PeerManagement();
        System.out.println("User dir is " + System.getProperty("user.dir"));
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    peerManagement.createAndShowGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void joinPeerGroup() throws Exception{
        JxtaGroup jxtaGroup = new JxtaGroup();
        peerGroup = jxtaGroup.joinGroup();
        /*System.out.println("Group Id conneted to is " + peerGroup.getPeerGroupID());*/
        startPeerDiscovery(peerGroup);
        publishPipeAdv(peerGroup);
        startPipeDiscovery(peerGroup);
        startDirectoryWatcher(peerGroup);
    }

    private void startPeerDiscovery(PeerGroup peerGroup) {
        final DiscoveryService discoveryService = peerGroup.getDiscoveryService();
        /*PeerDiscoveryListener peerDiscoveryListener = new PeerDiscoveryListener(this);*/
        /*discoveryService.addDiscoveryListener(peerDiscoveryListener);*/
        /*discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 5);*/
        PeerDiscovery peerDiscovery = new PeerDiscovery(this);
        Thread thread = new Thread(peerDiscovery);
        thread.start();
    }

    private void startPipeDiscovery(PeerGroup peerGroup) {
        final DiscoveryService discoveryService = peerGroup.getDiscoveryService();
        /*peerMessageListener = new PeerMessageListener();
        PipeDiscoveryListener pipeDiscoveryListener = new PipeDiscoveryListener(this);*/
        /*discoveryService.addDiscoveryListener(pipeDiscoveryListener);
        discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 1000);*/
        PipeDiscovery pipeDiscovery = new PipeDiscovery(this);
        Thread thread = new Thread(pipeDiscovery);
        thread.start();
    }

    private void startDirectoryWatcher(PeerGroup peerGroup){
        MonitorDirectory monitorDirectory = new MonitorDirectory(this);
        Thread thread = new Thread(monitorDirectory);
        thread.start();
    }

    private void publishPipeAdv(PeerGroup peerGroup){
        try {
            PipeAdvertisement pipeAdv = generatePipeAdv(peerGroup);
            peerMessageListener = new PeerMessageListener();
            /*myPipe = new JxtaBiDiPipe(peerGroup, pipeAdvertisement, peerMessageListener);*/
            PipeService pipeService = peerGroup.getPipeService();
            myPipe = pipeService.createInputPipe(pipeAdv, peerMessageListener);
            PipeAdvertisement pipeAdvertisement = myPipe.getAdvertisement();
            DiscoveryService discoveryService = peerGroup.getDiscoveryService();
            /*discoveryService.publish(pipeAdvertisement);*/
            discoveryService.remotePublish(pipeAdvertisement);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private PipeAdvertisement generatePipeAdv(PeerGroup peerGroup){
        // Creating a Pipe Advertisement
        String peerName = PeerProperties.getProperty(PeerProperties.PEER_NAME);
        PipeAdvertisement myPipeAdvertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        PipeID pipeId = IDFactory.newPipeID(peerGroup.getPeerGroupID(), peerName.getBytes());

        myPipeAdvertisement.setPipeID(pipeId);
        myPipeAdvertisement.setType(PipeService.PropagateType);
        myPipeAdvertisement.setName(peerGroup.getPeerID().toString());
        myPipeAdvertisement.setDescription("Created by " + peerName);
        return myPipeAdvertisement;
    }

    public void populatePeers(Peer peer){
        peerList.addElement(peer.getName());
        list1.setModel(peerList);
    }

}

