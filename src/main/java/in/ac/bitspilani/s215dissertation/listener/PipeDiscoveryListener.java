package in.ac.bitspilani.s215dissertation.listener;

import in.ac.bitspilani.s215dissertation.AppObjects;
import in.ac.bitspilani.s215dissertation.Context;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.impl.protocol.PipeAdv;
import net.jxta.protocol.DiscoveryResponseMsg;

import java.util.Enumeration;

/**
 * Created by vaibhavr on 27/03/16.
 */
public class PipeDiscoveryListener implements DiscoveryListener{

    private Context context;

    public PipeDiscoveryListener(Context context) {
        this.context = context;
    }

    public void discoveryEvent(DiscoveryEvent discoveryEvent) {
        System.out.println("Pipe Discovery Event ");
        DiscoveryResponseMsg response = discoveryEvent.getResponse();
        Enumeration<Advertisement> advertisements = response.getAdvertisements();
        while(advertisements.hasMoreElements()){
            Advertisement advertisement = advertisements.nextElement();
            System.out.println("Adverstisement type is " + advertisement.getClass().getName());
            if(PipeAdv.class.getName().equals(advertisement.getClass().getName())){
                PipeAdv pipeAdv = (PipeAdv) advertisement;
                System.out.println("Pipe Name is " + pipeAdv.getName());
                AppObjects.insertPipe(context, pipeAdv);
            }
        }
    }
}
