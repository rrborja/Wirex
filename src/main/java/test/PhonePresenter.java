/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import net.wirex.annotations.Form;
import net.wirex.annotations.GET;
import net.wirex.annotations.POST;
import net.wirex.annotations.Path;
import net.wirex.annotations.Type;
import net.wirex.enums.Media;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author RBORJA
 */
public class PhonePresenter extends Presenter {
    
    public PhonePresenter(Model model, JPanel view) {
        super(model, view);
        System.out.println(((PhoneModel)model).getPhoneNumbers());
    }
    
    @Path("phonelabel/get/{id}")
    @Type(Media.JSON)
    @Form(PhoneModel.class)
    @GET
    public void retrieve() {
        PhoneModel phoneModel = (PhoneModel)super.getModel();
        PhoneView view = (PhoneView)super.getPanel();
        Map<String, String> args = new HashMap<>();
        args.put("id", phoneModel.getId());
        super.call(args);
        System.out.println(phoneModel.getPhoneNumbers());
        
    }
    
    @Path("phonelabel/add")
    @Type(Media.JSON)
    @Form(PhoneModel.class)
    @POST
    public void submit() {
        super.call();
    }
    
}
