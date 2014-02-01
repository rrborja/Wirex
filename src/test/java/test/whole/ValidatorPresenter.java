/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.whole;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.annotations.Access;
import net.wirex.annotations.POST;
import net.wirex.annotations.Path;
import net.wirex.annotations.Type;
import net.wirex.enums.Media;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author ritchie
 */
public class ValidatorPresenter extends Presenter {

    @Access
    public ValidatorModel model1;
    @Access
    public ValidatorModel model2;

    public ValidatorPresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    public void submit2() {
//        int x = 1 / 0;
        getModel().store();
    }

    @Path("user/login")
    @Type(Media.JSON)
    @POST
    public void reset() {
        System.out.println(submit(AppEngine.form(this)));
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
