package net.wirex.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.annotations.Dispose;
import net.wirex.annotations.Retrieve;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author Ritchie Borja
 */
public class DetailPresenter extends Presenter {

    public DetailPresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void viewScreenshot() {
        JDialog dialog = new JDialog();
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(AppEngine.getScreenshot()));
        panel.add(label);
        panel.setBackground(Color.WHITE);
        dialog.add(panel);
        dialog.pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - dialog.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - dialog.getHeight()) / 2);
        dialog.setLocation(x, y);
        dialog.setVisible(true);
    }
    
    @Dispose
    public void close() {
        
    }

    @Override
    public void run(@Retrieve({}) ConcurrentHashMap<String, Invoker> methods) {
        DetailModel myModel = (DetailModel)getModel();
        myModel.setReporter(AppEngine.getErrorMessage());
    }
    
    
}
