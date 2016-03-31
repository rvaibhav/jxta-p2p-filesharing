package in.ac.bitspilani.s215dissertation.listener;

import in.ac.bitspilani.s215dissertation.FileAction;
import in.ac.bitspilani.s215dissertation.bean.FileData;
import in.ac.bitspilani.s215dissertation.util.Constants;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipeListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by vaibhavr on 28/03/16.
 */
public class SendMessageListener implements OutputPipeListener {

    private FileData fileData;

    public SendMessageListener(FileData fileData){
        this.fileData = fileData;
    }

    public void outputPipeEvent(OutputPipeEvent outputPipeEvent) {
        try {

            // Retrieving the output pipe to the peer who connected to the input end
            OutputPipe outputPipe = outputPipeEvent.getOutputPipe();

            System.out.println("START Sending file to a user " + outputPipe.getName());

            Message myMessage = new Message();
            StringMessageElement action = new StringMessageElement(Constants.MESSAGE_ACTION, fileData.getFileAction().toString(), null);
            StringMessageElement fileName = new StringMessageElement(Constants.MESSAGE_FILE_NAME, fileData.getFileName(), null);
            myMessage.addMessageElement(Constants.NAMESPACE, action);
            myMessage.addMessageElement(Constants.NAMESPACE, fileName);
            if(FileAction.ADD.equals(fileData.getFileAction()) || FileAction.UPDATE.equals(fileData.getFileAction())) {
                ByteArrayMessageElement fileBytes = new ByteArrayMessageElement(Constants.MESSAGE_BYTES, MimeMediaType.AOS, fileData.getFileBytes(), null);
                myMessage.addMessageElement(Constants.NAMESPACE, fileBytes);
            }else if(FileAction.DELETE.equals(fileData.getFileAction())){
                System.out.println("Sending file delete message for " + fileName);
            }

            // Sending the message
            outputPipe.send(myMessage);

            System.out.println("END Sending file to a user " + outputPipe.getName());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
