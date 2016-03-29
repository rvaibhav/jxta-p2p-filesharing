package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.listener.SendMessageListener;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.impl.protocol.PipeAdv;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeService;
import net.jxta.util.JxtaBiDiPipe;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.file.*;
import java.util.Map;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class DirectoryWatcher {

    private PeerManagement context;

    public DirectoryWatcher(PeerManagement context) {
        this.context = context;
    }

    public void watchDirectoryChanges(Object callBack){
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(System.getProperty("user.dir") + "/" + PeerProperties.props.getProperty(PeerProperties.PEER_MONITOR_DIR));
            /*File dirPath = new File(dir.toUri());*/
            if(!Files.exists(dir)){
                Files.createDirectory(dir);
            }
            WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            while(true){
                try {
                    key = watcher.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();
                        Path child = dir.resolve(fileName);
                        System.out.println("Event type:" + kind);
                        System.out.println("FileName:" + child.toString());
                        if(StandardWatchEventKinds.ENTRY_CREATE.equals(kind)){
                            communicateCreateToPeers(child);
                        }
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void communicateCreateToPeers(Path filePath){
        Map<String, PipeAdv> pipes = AppObjects.getPipes();
        Message message = new Message();
        String peerName = PeerProperties.getProperty(PeerProperties.PEER_NAME);
        StringMessageElement stringMessageElement = new StringMessageElement("MyStringElement", "New file added by " + peerName, null);
        message.addMessageElement(stringMessageElement);
        for(PipeAdv pipeAdv:pipes.values()){
            try {
                PeerGroup peerGroup = context.getPeerGroup();
                PipeService pipeService = peerGroup.getPipeService();
                pipeService.createOutputPipe(pipeAdv, new SendMessageListener());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
