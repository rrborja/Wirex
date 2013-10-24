/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.annotations.Form;
import net.wirex.annotations.GET;
import net.wirex.annotations.Path;
import net.wirex.annotations.POST;
import net.wirex.annotations.Retrieve;
import net.wirex.annotations.Type;
import net.wirex.enums.Media;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

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
        
        Map<String, String> args = new ConcurrentHashMap<>();
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

    @Override
    public void run(@Retrieve({"update"}) ConcurrentHashMap<String, Invoker> methods) {
        MyModel model = (MyModel) super.getModel();
        List list = new ArrayList();
        list.add("Hi");
        list.add("lost");
        list.add("voices");
        model.setCombo(list);
        System.out.println("asdasd");
    }
}
