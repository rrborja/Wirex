/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.wirex.WirexCore;
import net.wirex.WirexCore.MyObject;
import net.wirex.structures.XList;

/**
 *
 * @author Ritchie Borja
 */
public class TestPointer {

    public static void main(String[] args) {
        XList list = new XList();
        
        EventList rows = list;
        TableFormat tf = GlazedLists.tableFormat(MyObject.class, new String[] {"value"}, new String[] {"value"});

        JTable table = new JTable(new EventTableModel(rows, tf));
        
        JFrame f = new JFrame("tester");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new JScrollPane(table));
        f.pack();
        f.setVisible(true);
        
//        list.add(new WirexCore.MyObject("hello"));
        
        XList list2 = new XList(8);
        
//        list2.add(new MyObject("this"));
        
        list.setList(list2);
    }
    
    public static class MyTable extends JTable {
        
    }
}
