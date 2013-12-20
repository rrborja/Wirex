package net.wirex;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
final class WirexSystemTray {

    private static final Logger LOG = LoggerFactory.getLogger(WirexSystemTray.class.getSimpleName());

    private static WirexSystemTray instance;

    public static void init(ImageIcon icon) {
        if (instance == null) {
            instance = new WirexSystemTray(icon);
        }
    }
    
    private static void populate(PopupMenu popup) {
        MenuItem aboutItem = new MenuItem("About");
        MenuItem consoleItem = new MenuItem("Console");
        MenuItem exitItem = new MenuItem("Exit");
        popup.add(consoleItem);
        popup.addSeparator();
        popup.add(aboutItem);
        popup.add(exitItem);
    }

    private WirexSystemTray(ImageIcon icon) {
        EventQueue.invokeLater(() -> {
            if (!java.awt.SystemTray.isSupported()) {
                LOG.warn("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();
            final TrayIcon trayIcon = new TrayIcon(icon.getImage());
            final SystemTray tray = SystemTray.getSystemTray();
            
            populate(popup);
            trayIcon.setPopupMenu(popup);
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                LOG.error("TrayIcon could not be added.");
            }
            
        });
    }

}
