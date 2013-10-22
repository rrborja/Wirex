/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.wirex.ApplicationControllerFactory;
import net.wirex.MVP;

/**
 *
 * @author RBORJA
 */
public class PhoneApp {

    public static void main(String[] args) {
        try {
            ApplicationControllerFactory.connect("http://10.0.1.46:8080/g7");
            MVP app = ApplicationControllerFactory.prepare(PhoneView.class);
            app.display(JFrame.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PhoneApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
