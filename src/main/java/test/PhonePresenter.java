/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
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

//    public @Access PhoneModel phoneModel;
    
    public PhonePresenter(Model model, JPanel view) {
        super(model, view);
    }
    
    @Path("phonelabel/get/{id}")
    @Type(Media.JSON)
    @Form(PhoneModel.class)
    @GET
    public void retrieve() {
        PhoneModel phoneModel = (PhoneModel)super.getModel();
        PhoneView view = (PhoneView)super.getPanel();
//        phoneModel.setLabel("heyyyy");
        Map<String, String> args = new ConcurrentHashMap<>();
        args.put("id", phoneModel.getId());
        phoneModel.getPhoneNumbers().clear();
        super.call(args);
   
        
    }
    
    @Path("phonelabel/add")
    @Type(Media.JSON)
    @Form(PhoneModel.class)
    @POST
    public void submit() {
        super.call();
    }
    
    public void test() {}

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
//        XList list = new XList();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        phoneModel.getPhoneNumbers().addAll(list);
    }
    
}
