/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.structures.XNode;

/**
 *
 * @author RBORJA
 */
public class MyTreePresenter extends Presenter {

    public MyTreePresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    public void add() {
        MyTreeModel model = (MyTreeModel) super.getModel();
        model.getTree().add(new XNode(new String[] {"haha"}, "boo"));
    }
    
    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
