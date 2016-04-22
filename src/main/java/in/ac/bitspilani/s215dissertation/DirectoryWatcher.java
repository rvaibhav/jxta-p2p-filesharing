package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.bean.FileData;
import in.ac.bitspilani.s215dissertation.listener.SendMessageListener;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.impl.protocol.PipeAdv;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class DirectoryWatcher {

    private Context context;

    public DirectoryWatcher(Context context) {
        this.context = context;
    }

    public void watchDirectoryChanges(Object callBack){
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(System.getProperty("user.dir") + "/" + PeerProperties.props.getProperty(PeerProperties.PEER_MONITOR_DIR));
            if(!Files.exists(dir)){
                Files.createDirectory(dir);
            }
            populateAllPresentFiles(dir);
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
                            if(!AppObjects.contains(child.getFileName().toString())){
                                communicateToPeers(child, FileAction.ADD);
                            }
                        }else if(StandardWatchEventKinds.ENTRY_DELETE.equals(kind)){
                            if(AppObjects.contains(child.getFileName().toString())) {
                                communicateToPeers(child, FileAction.DELETE);
                            }
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

    private void communicateToPeers(Path filePath, FileAction fileAction){
        String fileName = filePath.getFileName().toString();
        context.showFiles();
        if(AppObjects.ignore(fileName)){
            System.out.println("Ignoring file " + fileName);
            return;
        }
        Map<String, PipeAdv> pipes = AppObjects.getPipes();
        FileData fileData = new FileData();
        fileData.setFileName(fileName);
        fileData.setFileAction(fileAction);
        if(FileAction.ADD.equals(fileAction) || FileAction.UPDATE.equals(fileAction)) {
            byte[] fileBytes = convertFileToByteArray(filePath);
            fileData.setFileBytes(fileBytes);
        }
        for(PipeAdv pipeAdv:pipes.values()){
            try {
                PeerGroup peerGroup = context.getPeerGroup();
                PipeService pipeService = peerGroup.getPipeService();
                pipeService.createOutputPipe(pipeAdv, new SendMessageListener(fileData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] convertFileToByteArray(Path path){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File file = null;
        FileInputStream fis = null;
        byte[] bytes = null;
        try{
            file = new File(path.toUri());
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
            }
            bytes = bos.toByteArray();
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    private void populateAllPresentFiles(Path path){
        File folder = new File(path.toUri());
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                System.out.println("File " + listOfFiles[i].getName());
                AppObjects.insertFile(fileName, listOfFiles[i]);
            }
        }
    }
}
