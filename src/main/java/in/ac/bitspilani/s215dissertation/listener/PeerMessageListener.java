package in.ac.bitspilani.s215dissertation.listener;

import in.ac.bitspilani.s215dissertation.util.Constants;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;

/**
 * Created by vaibhavr on 27/03/16.
 */
public class PeerMessageListener implements PipeMsgListener {

    public void pipeMsgEvent(PipeMsgEvent pipeMsgEvent) {
        String text = pipeMsgEvent.getMessage().getMessageElement(Constants.NAMESPACE, "HelloElement").toString();
        System.out.println("DISSERTATION - Message string is " + text);
    }

}
