package in.ac.bitspilani.s215dissertation;

/**
 * Created by vaibhavr on 19/03/16.
 */

import in.ac.bitspilani.s215dissertation.bean.Peer;
import in.ac.bitspilani.s215dissertation.listener.PeerDiscoveryListener;
import in.ac.bitspilani.s215dissertation.threads.DiscoveryThread;
import in.ac.bitspilani.s215dissertation.tools.PopUp;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.credential.AuthenticationCredential;
import net.jxta.credential.Credential;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.XMLElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.impl.access.pse.PSEAccessService;
import net.jxta.impl.content.ContentServiceImpl;
import net.jxta.impl.membership.pse.PSEMembershipService;
import net.jxta.impl.membership.pse.StringAuthenticator;
import net.jxta.impl.peergroup.CompatibilityUtils;
import net.jxta.impl.peergroup.StdPeerGroup;
import net.jxta.impl.peergroup.StdPeerGroupParamAdv;
import net.jxta.membership.MembershipService;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.NetPeerGroupFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.ModuleSpecID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;


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

    public static final String name = "Vaibhav";

    public static final File configurationFile = new File("." + System.getProperty("file.separator") + name);

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
        PeerGroup peerGroup = jxtaGroup.joinGroup();
        final DiscoveryService discoveryService = peerGroup.getDiscoveryService();
        PeerDiscoveryListener peerDiscoveryListener = new PeerDiscoveryListener(this);
        discoveryService.addDiscoveryListener(peerDiscoveryListener);
        discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 5);
        DiscoveryThread discoveryThread = new DiscoveryThread(this, discoveryService);
    }

    public void populatePeers(Peer peer){
        peerList.addElement(peer.getName());
        list1.setModel(peerList);
    }

}

