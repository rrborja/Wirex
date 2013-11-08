/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.another;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
import test.PhoneView;

/**
 *
 * @author RBORJA
 */
public class LoginPanelPresenter extends Presenter {

    public LoginPanelPresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    @Path("user/login")
    @Type(Media.JSON)
    @POST
    @Fire(view = PhoneView.class, type = JDialog.class)
    @Dispose
    public void login() throws EventInterruptionException {
        LoginPanelModel model = (LoginPanelModel) getModel();
        Map<String, String> args = new ConcurrentHashMap<>();
        args.put("username", model.getUsername());
        args.put("password", model.getPassword());
//        super.call(args);
//        if

        ServerResponse resource = super.call(args);
        System.out.println(resource.getMessage());
        String message = resource.getMessage().toString();
        
        if (!message.equals("-1")) {
            System.out.println(message);
        } else {
            JOptionPane.showMessageDialog(getPanel(), "Invalid username and password. Try again.", "Invalid login", JOptionPane.ERROR_MESSAGE);
            super.interrupt("Password Invalid");
        }
        
    }

    @Dispose
    public void cancel() {
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
    }
}
