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
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;

/**
 *
 * @author RBORJA
 */
public class PhoneApp {

    public static void main(String[] args) {
        try {
            //<editor-fold defaultstate="collapsed" desc="Initialize Look and feel setting code in UBO Version 7">
            try {
                String OS = System.getProperty("os.name").toLowerCase();
                LOOP:
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    switch (info.getName()) {
                        case "Windows":
                            if (OS.indexOf("win") >= 0) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break LOOP;
                            }
                            break;
                        case "Nimbus":
                            if (OS.indexOf("nux") >= 0) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break LOOP;
                            }
                            break;
                    }
                }
            } catch (Exception e) {
            }
            //</editor-fold>

            ApplicationControllerFactory.connect("http://10.0.1.46:8080/g7");
            MVP app = ApplicationControllerFactory.prepare(PhoneView.class);
            app.display(JFrame.class, true);
        } catch (ViewClassNotBindedException ex) {
            Logger.getLogger(PhoneApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongComponentException ex) {
            Logger.getLogger(PhoneApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
