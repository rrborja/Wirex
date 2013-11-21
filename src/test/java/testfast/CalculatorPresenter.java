/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testfast;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author PHVISP
 */
public class CalculatorPresenter extends Presenter {

    public CalculatorPresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void add() {
        CalculatorModel model = (CalculatorModel) getModel();
        int first = Integer.parseInt(model.getFirst());
        int second = Integer.parseInt(model.getSecond());
        JOptionPane.showMessageDialog(null, first + second);
    }
    
    public void reset() {
        CalculatorModel model = (CalculatorModel) getModel();
        model.setFirst("");
        model.setSecond("");
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
