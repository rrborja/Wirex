/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wirex;

import java.util.logging.Level;
import javax.swing.JPanel;
import static net.wirex.AppEngine.components;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author RBORJA
 */
public class PrepareWorker extends Thread {
    
    private static final Logger LOG = LoggerFactory.getLogger(PrepareWorker.class.getName());
    
    private final String viewName;
    private final Class<? extends JPanel> viewClass;
    
    public PrepareWorker(String viewName, Class<? extends JPanel> viewClass) {
        super("Wirex");
        this.viewName = viewName;
        this.viewClass = viewClass;
    }
    
    @Override
    public void run() {
        LOG.info("Preparing subview {}", viewName);
        try {
            MVP mvp = AppEngine.prepare(viewClass);
            components.put(viewName, mvp.getView());
            sleep(50);
        } catch (ViewClassNotBindedException | WrongComponentException ex) {
            LOG.debug("PrepareWorker experienced problems in preparing {}", viewName);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(PrepareWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
