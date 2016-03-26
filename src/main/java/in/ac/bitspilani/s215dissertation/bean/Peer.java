package in.ac.bitspilani.s215dissertation.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by vaibhavr on 26/03/16.
 */
public class Peer {

    @Getter @Setter
    private String jxtaId;

    @Getter @Setter
    private String name;

    public Peer(String jxtaId, String name){
        this.jxtaId = jxtaId;
        this.name = name;
    }

}
