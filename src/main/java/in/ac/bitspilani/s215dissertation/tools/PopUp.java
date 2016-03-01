package in.ac.bitspilani.s215dissertation.tools;

import javax.swing.*;

/**
 * Created by vaibhavr on 01/03/16.
 */
public class PopUp {

    public static void info(String name, String message) {

        JOptionPane.showMessageDialog(null, message, name, JOptionPane.INFORMATION_MESSAGE);

    }

    public static void error(String name, String message) {

        JOptionPane.showMessageDialog(null, message, name, JOptionPane.ERROR_MESSAGE);

    }

    public static int confirmDialog(String Name, String Question) {

        return JOptionPane.showConfirmDialog(null, Question, Name, JOptionPane.YES_NO_OPTION);

    }

}
