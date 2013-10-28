/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import net.wirex.ApplicationControllerFactory;
import net.wirex.MVP;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;

/**
 *
 * @author RBORJA
 */
public class MyTreeApp {

    public static void main(String[] args) {
        try {
            MVP app = ApplicationControllerFactory.prepare(MyTreeView.class);
            app.display(JDialog.class, true);
        } catch (ViewClassNotBindedException ex) {
            Logger.getLogger(MyTreeApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongComponentException ex) {
            Logger.getLogger(MyTreeApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
