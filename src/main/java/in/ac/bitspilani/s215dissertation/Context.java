package in.ac.bitspilani.s215dissertation;

/**
 * Created by vaibhavr on 19/03/16.
 */

import in.ac.bitspilani.s215dissertation.bean.Peer;
import in.ac.bitspilani.s215dissertation.listener.PeerMessageListener;
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

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by vaibhavr on 18/03/16.
 */
public class Context {

    private static PeerGroupID peerGroupId = null;

    private JPanel panel1;
    private JList list1;
    private JList list2;

    private JPanel leftPanel;

    private JPanel rightPanel;
    private JLabel leftHeader;
    private JLabel rightHeader;

    private DefaultListModel peerList= new DefaultListModel();

    private DefaultListModel filesList= new DefaultListModel();

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
        decorateLeftPaneHeader();
        decorateRightPaneHeader();
        list1 = new JList();
        list2 = new JList();
        list1.setLayoutOrientation(JList.VERTICAL);
        list2.setLayoutOrientation(JList.VERTICAL);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        initializeLeftPanel();
        initializeRightPanel();
        list1.setBackground(Color.WHITE);
        list2.setBackground(Color.WHITE);
        Dimension minimumSize = new Dimension(100, 50);
        leftPanel.setMinimumSize(minimumSize);
        rightPanel.setMinimumSize(minimumSize);
    }

    private void initializeLeftPanel() {
        leftPanel.add(leftHeader);
        leftHeader.setAlignmentX(JLabel.CENTER);
        leftHeader.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(list1);
        list1.setAlignmentX(JList.LEFT_ALIGNMENT);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    }

    private void initializeRightPanel(){
        rightPanel.add(rightHeader);
        rightHeader.setAlignmentX(JLabel.CENTER);
        rightHeader.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(list2);
        list2.setAlignmentX(JList.LEFT_ALIGNMENT);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    }

    private void decorateLeftPaneHeader() {
        leftHeader = new JLabel("PeerList");
        leftHeader.setForeground(Color.BLUE);
        leftHeader.setBackground(Color.WHITE);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD,15);
        leftHeader.setFont(font);
        leftHeader.setOpaque(true);

    }

    private void decorateRightPaneHeader(){
        rightHeader = new JLabel("List of Files");
        rightHeader.setForeground(Color.BLUE);
        rightHeader.setBackground(Color.WHITE);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD,15);
        rightHeader.setFont(font);
        rightHeader.setOpaque(true);
    }

    private void createAndShowGUI() throws Exception{
        PeerProperties.readProperties();
        JFrame frame = new JFrame(PeerProperties.getProperty(PeerProperties.PEER_NAME));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.createSplitPane();
        frame.add(this.getSplitPane());
        frame.pack();
        frame.setVisible(true);
        Dimension minimumSize = new Dimension(200, 200);
        frame.setSize(minimumSize);
        joinPeerGroup();
        showFiles();
    }

    public static void main(String[] args){
        final Context context = new Context();
        System.out.println("User dir is " + System.getProperty("user.dir"));
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    context.createAndShowGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void joinPeerGroup() throws Exception{
        JxtaGroup jxtaGroup = new JxtaGroup();
        peerGroup = jxtaGroup.joinGroup();
        startPeerDiscovery(peerGroup);
        publishPipeAdv(peerGroup);
        startPipeDiscovery(peerGroup);
        startDirectoryWatcher(peerGroup);
    }

    private void startPeerDiscovery(PeerGroup peerGroup) {
        PeerDiscovery peerDiscovery = new PeerDiscovery(this);
        Thread thread = new Thread(peerDiscovery);
        thread.start();
    }

    private void startPipeDiscovery(PeerGroup peerGroup) {
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
            peerMessageListener = new PeerMessageListener(this);
            PipeService pipeService = peerGroup.getPipeService();
            myPipe = pipeService.createInputPipe(pipeAdv, peerMessageListener);
            PipeAdvertisement pipeAdvertisement = myPipe.getAdvertisement();
            DiscoveryService discoveryService = peerGroup.getDiscoveryService();
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
        if(!PeerProperties.getProperty(PeerProperties.PEER_NAME).equals(peer.getName())) {
            peerList.addElement(peer.getName());
            list1.setModel(peerList);
        }
    }

    public void showFiles(){
        Path dir = Paths.get(System.getProperty("user.dir") + "/" + PeerProperties.props.getProperty(PeerProperties.PEER_MONITOR_DIR));
        if(!Files.exists(dir)){
            System.out.println("Directory for synchronization does not exist!");
            return;
        }
        File folder = new File(dir.toUri());
        File[] listOfFiles = folder.listFiles();
        filesList.clear();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                if(!AppObjects.ignore(fileName)) {
                    filesList.addElement(fileName);
                }
            }
        }
        list2.setModel(filesList);
    }

}

