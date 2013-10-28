/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import net.wirex.interfaces.Model;

/**
 *
 * @author RBORJA
 */
public class MyTreeModel extends Model {

    private MyNodeModel tree;

    public MyTreeModel() {
        tree = new MyNodeModel();
    }
    
    public MyNodeModel getTree() {
        return tree;
    }

    public void setTree(MyNodeModel tree) {
        this.tree = tree;
    }
    
    
    
}
