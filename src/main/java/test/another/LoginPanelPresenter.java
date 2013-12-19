/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.another;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.ServerResponse;
import net.wirex.annotations.Dispose;
import net.wirex.annotations.Fire;
import net.wirex.annotations.POST;
import net.wirex.annotations.Path;
import net.wirex.annotations.Type;
import net.wirex.enums.Media;
import net.wirex.exceptions.EventInterruptionException;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import org.springframework.http.HttpStatus;
import test.PhoneView;

/**
 *
 * @author Ritchie Borja
 */
public class LoginPanelPresenter extends Presenter {
    
    public LoginPanelPresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    @Path("user/login")
    @Type(Media.JSON)
    @POST
    @Fire(view = PhoneView.class, type = JDialog.class, depends = true, dynamic = false)
//    @Dispose
    public void login() throws EventInterruptionException {
        LoginPanelModel model = (LoginPanelModel) getModel();
        Map<String, String> args = new ConcurrentHashMap<>();
        args.put("username", model.getUsername());
        args.put("password", model.getPassword());
        ServerResponse resource = super.call(args);
        HttpStatus status = resource.getStatus();

        if (status.equals(HttpStatus.ACCEPTED)) {

        } else if (status.equals(HttpStatus.FORBIDDEN)) {
            JOptionPane.showMessageDialog(getPanel(), "Invalid username and password. Try again.", "Invalid login", JOptionPane.ERROR_MESSAGE);
            super.interrupt("Password Invalid");
        } else {
            JOptionPane.showMessageDialog(getPanel(), "Something's wrong with the server.", "Server Error", JOptionPane.ERROR_MESSAGE);
//            super.interrupt("Unknown Error");
        }

    }

//    @Dispose
    public void cancel() {
        System.out.println(touch(JCheckBox.class, "chkRemember").isSelected());
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
    }
}
