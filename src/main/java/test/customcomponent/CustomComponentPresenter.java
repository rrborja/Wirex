/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.customcomponent;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author ritchie
 */
public class CustomComponentPresenter extends Presenter {

    public CustomComponentPresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void go() {
        CustomComponentModel model = (CustomComponentModel)getModel();
//        model.setText33("asdasd");
        System.out.println(model.getMac());
    }
    
    public void play() {
        CustomComponentModel model = (CustomComponentModel)getModel();
        
        model.setIpAddress(model.getText33());
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
