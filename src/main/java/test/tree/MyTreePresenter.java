/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.structures.XList;
import net.wirex.structures.XNode;
import test.table.TablePresenter;

/**
 *
 * @author Ritchie Borja
 */
public class MyTreePresenter extends Presenter {

    public MyTreePresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    public void add() {
        TablePresenter presenter = AppEngine.access(TablePresenter.class);
        presenter.hello();
        
        MyTreeModel model = (MyTreeModel) super.getModel();
        model.getTree().add(new XNode(new String[] {"haha"}, "boo"));
        XList tree = model.getTree();
        tree.add(new XNode(new String[]{"betacleversoft.net", "billing", "set"}, "hi"));
        tree.add(new XNode(new String[]{"betacleversoft.net", "billing", "get"}, "hello"));
        tree.add(new XNode(new String[]{"betacleversoft.net", "mail", "yes"}, "how r u?"));
        tree.add(new XNode(new String[]{"betacleversoft.net", "mail", "yes"}, "good"));
    }
    
    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
