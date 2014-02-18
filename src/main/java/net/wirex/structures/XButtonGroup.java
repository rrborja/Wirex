/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wirex.structures;

import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;


/**
 *
 * @author ritchie
 */
public class XButtonGroup extends ButtonGroup {
    
    public void addMediatorListener(ActionListener e) {
        e.actionPerformed(null);
    }
    
}
