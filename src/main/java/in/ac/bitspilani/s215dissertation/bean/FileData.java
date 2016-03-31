package in.ac.bitspilani.s215dissertation.bean;

import in.ac.bitspilani.s215dissertation.FileAction;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by vaibhavr on 30/03/16.
 */
public class FileData {

    @Getter @Setter
    private String fileName;

    @Getter @Setter
    private FileAction fileAction;

    @Getter @Setter
    private byte[] fileBytes;

}
