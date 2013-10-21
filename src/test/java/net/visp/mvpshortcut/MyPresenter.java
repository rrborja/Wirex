/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import net.visp.wirex.annotations.Form;
import net.visp.wirex.annotations.GET;
import net.visp.wirex.annotations.Path;
import net.visp.wirex.annotations.POST;
import net.visp.wirex.annotations.Type;
import net.visp.wirex.enums.Media;
import net.visp.wirex.interfaces.Model;
import net.visp.wirex.interfaces.Presenter;

/**
 *
 * @author RBORJA
 */
public class MyPresenter extends Presenter {

    public MyPresenter(Model model, JPanel view) {
        super(model, view);
    }

    @Path("phonelabel/add")
    @Type(Media.JSON)
    @Form(MyModel.class)
    @POST
    public void submit() {
        MyModel model = (MyModel) super.getModel();
        model.setNewText(model.getName() + " " + model.getLastname());
        super.call();
    }

    @Path("phonelabel/get/{id}")
    @Type(Media.JSON)
    @Form(MyModel.class)
    @GET
    public void display() {
        MyModel model = (MyModel) super.getModel();
        
        Map<String, String> args = new HashMap<>();
        args.put("id", model.getLastname());
        
        System.err.println(super.call(args).getEntity(String.class));
        
    }

    public void update() {
//        MyModel model = (MyModel) super.getModel();
//        model.setNewText(model.getLastname());
    }

    public void update2() {
//        MyModel model = (MyModel) super.getModel();
//        model.setNewText(model.getLastname());
    }

    public void update3() {
//        MyModel model = (MyModel) super.getModel();
//        model.setNewText(model.getLastname());
    }
}
