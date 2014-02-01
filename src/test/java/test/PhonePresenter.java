/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.annotations.Form;
import net.wirex.annotations.GET;
import net.wirex.annotations.POST;
import net.wirex.annotations.Path;
import net.wirex.annotations.Type;
import net.wirex.enums.Media;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import test.another.LoginPanelPresenter;

/**
 *
 * @author Ritchie Borja
 */
public class PhonePresenter extends Presenter {
    
//    public @Access PhoneModel phoneModel;
    public PhonePresenter(Model model, JPanel view) {
        super(model, view);
    }

    @Path("phonelabel/get")
    @Type(Media.JSON)
    @Form(PhoneModel.class)
    @POST
    public void retrieve() {
//        PhoneModel phoneModel = (PhoneModel)super.getModel();
//        PhoneView view = (PhoneView)super.getPanel();
////        phoneModel.setLabel("heyyyy");
//        Map<String, String> args = new ConcurrentHashMap<>();
//        args.put("id", phoneModel.getId());
//        phoneModel.getPhoneNumbers().clear();
//        super.call(args);
//        LoginPanelPresenter presenter = AppEngine.access(LoginPanelPresenter.class);
//        presenter.cancel();
//        touch(JButton.class, "hi").addActionListener(null);
        call();
//        submit(AppEngine.form(this));
    }

    @Path("phonelabel/add")
    @Type(Media.JSON)
//    @Form(PhoneModel.class)
    @POST
    public void submit2() {
        call();
    }

    public void test() {
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
//        XList list = new XList();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        phoneModel.getPhoneNumbers().addAll(list);
    }

}
