/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.tree;

import net.wirex.annotations.PathNode;
import net.wirex.interfaces.Model;

/**
 *
 * @author RBORJA
 */
public class MyNodeModel<T> extends Model {

    @PathNode({"betacleversoft.net", "billing", "set"})
    private String data;
    @PathNode({"betacleversoft.net", "billing", "get"})
    private String data2;
    @PathNode({"betacleversoft.net", "mail", "yes"})
    private String data3;
    @PathNode({"betacleversoft.net", "mail", "yes"})
    private String data4;

    
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
        data = "hi";
        data2 = "hello";
        data3 = "how r u?";
        data4 = "good";
    }
    
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    
    
}
