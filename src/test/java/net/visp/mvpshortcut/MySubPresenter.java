/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.visp.mvpshortcut;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.annotations.Access;
import net.wirex.annotations.Retrieve;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author Ritchie Borja
 */
public class MySubPresenter extends Presenter {

    private @Access MyModel myModel;
    
    public MySubPresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void test() {
        MySubModel model = (MySubModel)super.getModel();
        model.setText("wahahaha");
        myModel.setNewText("haha");
    }

    @Override
    public void run(@Retrieve({"test"}) ConcurrentHashMap<String, Invoker> methods) {
        MySubModel model = (MySubModel)super.getModel();
        System.out.println("hi");
        model.setText("imagination");
    }

    
    
}
