/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author ritchie
 */
public class TablePresenter extends Presenter {

    public TablePresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
