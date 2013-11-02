package net.visp.mvpshortcut;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.wirex.AppEngine;
import net.wirex.MVP;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            AppEngine.connect("http://staging.userservices.net:8080/g8");
            MVP mvpController = AppEngine.prepare(MyView.class);
            mvpController.display(JFrame.class, true);
        } catch (ViewClassNotBindedException | WrongComponentException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
