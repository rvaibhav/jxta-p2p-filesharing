package in.ac.bitspilani.s215dissertation.tools;

import net.jxta.platform.NetworkManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by vaibhavr on 01/03/16.
 */
public class CommonUtil {

    public static void checkForExistingConfigurationDeletion(String Name, File ConfigurationFile) throws IOException {

        if (JOptionPane.YES_OPTION==PopUp.confirmDialog(Name, "Do you want to delete the existing configuration in:\n\n"
                + ConfigurationFile.getCanonicalPath())) {

            NetworkManager.RecursiveDelete(ConfigurationFile);

        }

    }
}
