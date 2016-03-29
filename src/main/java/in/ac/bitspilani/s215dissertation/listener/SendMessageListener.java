package in.ac.bitspilani.s215dissertation.listener;

import in.ac.bitspilani.s215dissertation.util.Constants;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipeListener;

import java.io.IOException;

/**
 * Created by vaibhavr on 28/03/16.
 */
public class SendMessageListener implements OutputPipeListener {

    public void outputPipeEvent(OutputPipeEvent outputPipeEvent) {
        try {

            // Retrieving the output pipe to the peer who connected to the input end
            OutputPipe outputPipe = outputPipeEvent.getOutputPipe();

            System.out.println("START Sending message to a user " + outputPipe.getName());

            // Creating a Hello message !!!
            Message myMessage = new Message();
            StringMessageElement MyStringMessageElement = new StringMessageElement("HelloElement", "Hello from " + PeerProperties.getProperty(PeerProperties.PEER_NAME), null);
            myMessage.addMessageElement(Constants.NAMESPACE, MyStringMessageElement);

            // Sending the message
            outputPipe.send(myMessage);

            System.out.println("END Sending message to a user " + outputPipe.getName());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
