/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import net.wirex.interfaces.Model;
import net.wirex.structures.XList;
import net.wirex.structures.XNode;

/** 
 *
 * @author RBORJA
 */
public class MyTreeModel extends Model {

    private XList<XNode> tree;

    public MyTreeModel() {
        tree = new XList<>();
        tree.add(new XNode(new String[]{"betacleversoft.net", "billing", "set"}, "hi"));
        tree.add(new XNode(new String[]{"betacleversoft.net", "billing", "get"}, "hello"));
        tree.add(new XNode(new String[]{"betacleversoft.net", "mail", "yes"}, "how r u?"));
        tree.add(new XNode(new String[]{"betacleversoft.net", "mail", "yes"}, "good"));
    }
    
    public XList getTree() {
        return tree;
    }

    public void setTree(XList tree) {
        this.tree = tree;
    }
    
    
    
}
