/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import net.wirex.annotations.PathNode;
import net.wirex.interfaces.Model;
import net.wirex.structures.XNode;

/**
 *
 * @author Ritchie Borja
 */
public class MyNodeModel<T> extends Model {

    
    private XNode data;
    private XNode data2;
    private XNode data3;
    private XNode data4;

    
    /*
     * betacleversoft.net
     *      billing
     *          set
     *              hi
     *          get
     *              hello
     *      mail
     *          yes
     *              how r u?
     */
    public MyNodeModel() {
        data = new XNode(new String[]{"betacleversoft.net", "billing", "set"}, "hi");
        data2 = new XNode(new String[]{"betacleversoft.net", "billing", "get"}, "hello");
        data3 = new XNode(new String[]{"betacleversoft.net", "mail", "yes"}, "how r u?");
        data4 = new XNode(new String[]{"betacleversoft.net", "mail", "yes"}, "good");
    }
    

    
    
}
