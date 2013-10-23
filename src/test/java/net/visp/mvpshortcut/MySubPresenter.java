/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.visp.mvpshortcut;

import java.util.HashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.annotations.Access;
import net.wirex.annotations.Retrieve;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author RBORJA
 */
public class MySubPresenter extends Presenter {

    private @Access MyModel myModel;
    
    public MySubPresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void test() {
        MySubModel model = (MySubModel)super.getModel();
        System.out.println("hayyzzzz");
        model.setText("wahahaha");
        myModel.setNewText("haha");
    }

    @Override
    public void run(@Retrieve({"test"}) HashMap<String, Invoker> methods) {
        System.out.println("LASTNAME: " + myModel.getLastname());
    }

    
    
}
