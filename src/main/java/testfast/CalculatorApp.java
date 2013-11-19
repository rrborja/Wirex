/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testfast;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.wirex.AppEngine;
import net.wirex.MVP;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;

/**
 *
 * @author PHVISP
 */
public class CalculatorApp {
    public static void main(String[] args) {
        MVP mvp;
        try {
            mvp = AppEngine.prepare(CalculatorPanel.class);
            mvp.display(JFrame.class, Boolean.TRUE);
        } catch (ViewClassNotBindedException | WrongComponentException ex) {
            Logger.getLogger(CalculatorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
