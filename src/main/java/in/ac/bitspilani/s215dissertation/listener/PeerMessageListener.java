package in.ac.bitspilani.s215dissertation.listener;

import in.ac.bitspilani.s215dissertation.AppObjects;
import in.ac.bitspilani.s215dissertation.FileAction;
import in.ac.bitspilani.s215dissertation.Context;
import in.ac.bitspilani.s215dissertation.util.Constants;
import in.ac.bitspilani.s215dissertation.util.PeerProperties;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;

import javax.swing.*;
import java.io.*;

/**
 * Created by vaibhavr on 27/03/16.
 */
public class PeerMessageListener implements PipeMsgListener {

    public Context context;

    public PeerMessageListener(Context context){
        this.context = context;
    }

    public void pipeMsgEvent(PipeMsgEvent pipeMsgEvent) {
        Message message = pipeMsgEvent.getMessage();
        String action = message.getMessageElement(Constants.NAMESPACE, Constants.MESSAGE_ACTION).toString();
        String fileName = message.getMessageElement(Constants.NAMESPACE, Constants.MESSAGE_FILE_NAME).toString();

        if(FileAction.ADD.equals(FileAction.valueOf(action)) || FileAction.UPDATE.equals(FileAction.valueOf(action))){
            MessageElement fileBytes = message.getMessageElement(Constants.NAMESPACE, Constants.MESSAGE_BYTES);
            try {
                InputStream inputStream = fileBytes.getStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                byte[] buf = new byte[1024];
                for (int readNum; (readNum = inputStream.read(buf)) != -1;) {
                    buffer.write(buf, 0, readNum); //no doubt here is 0
                }
                byte[] bytes = buffer.toByteArray();
                File newFile = new File(System.getProperty("user.dir") + "/" + PeerProperties.getProperty(PeerProperties.PEER_MONITOR_DIR) + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(bytes);
                fos.flush();
                fos.close();

                AppObjects.insertFile(fileName, newFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(FileAction.DELETE.equals(FileAction.valueOf(action))){
            System.out.println("Received a DELETE message for file " + fileName);
            File deleteFile = new File(System.getProperty("user.dir") + "/" + PeerProperties.getProperty(PeerProperties.PEER_MONITOR_DIR) + "/" + fileName);
            String deleteMessage = new StringBuffer("Do you want to delete ").append(fileName).append(" from ").append(System.getProperty("user.dir")).append("/").append(PeerProperties.getProperty(PeerProperties.PEER_MONITOR_DIR)).toString();
            int buttonPressed = JOptionPane.showConfirmDialog(null, deleteMessage, PeerProperties.getProperty(PeerProperties.PEER_NAME), JOptionPane.YES_NO_OPTION);
            if(buttonPressed== JOptionPane.YES_OPTION){
                AppObjects.deleteFile(fileName);
                if(deleteFile != null && deleteFile.exists()){
                    deleteFile.delete();
                }
            }
        }
        context.showFiles();
    }

}
