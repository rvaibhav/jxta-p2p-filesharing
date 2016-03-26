package in.ac.bitspilani.s215dissertation;

import in.ac.bitspilani.s215dissertation.util.PeerProperties;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by vaibhavr on 25/03/16.
 */
public class DirectoryWatcher {

    public void watchDirectoryChanges(Object callBack){
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(PeerProperties.props.getProperty(PeerProperties.PEER_MONITOR_DIR));
            WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            while(true){
                /*WatchKey key = null;*/
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

}
