package in.ac.bitspilani.s215dissertation.threads;

import in.ac.bitspilani.s215dissertation.DirectoryWatcher;
import in.ac.bitspilani.s215dissertation.Context;

/**
 * Created by vaibhavr on 27/03/16.
 */
public class MonitorDirectory implements Runnable{

    private Context context;

    public MonitorDirectory(Context context){
        this.context = context;
    }

    public void run() {
        DirectoryWatcher watcher = new DirectoryWatcher(context);
        watcher.watchDirectoryChanges(null);
    }
}
