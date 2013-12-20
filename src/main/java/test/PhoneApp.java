/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Toolkit;
import java.util.logging.Level;
import javax.swing.JFrame;
import net.wirex.AppEngine;
import net.wirex.MVP;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import test.another.NewLoginPanel;

/**
 *
 * @author Ritchie Borja
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

            AppEngine.connect("http://staging.userservices.net:8080/g7");
            AppEngine.locateResource("http://10.0.1.46/~rborja/icons/");
            AppEngine.setTrayIcon("VispIcon_small");
            MVP app = AppEngine.prepare(NewLoginPanel.class);
            app.display(JFrame.class, true);
            Toolkit.getDefaultToolkit().beep();
        } catch (ViewClassNotBindedException | WrongComponentException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(PhoneApp.class.getName());
}
