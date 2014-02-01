/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import net.wirex.structures.XRenderer;

/**
 *
 * @author ritchie
 */
public class TableRenderer extends DefaultTableCellRenderer implements XRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color bgColor = Color.RED;
        setBackground(bgColor);
        return this;
    }

    @Override
    public Class type() {
        return TableCellRenderer.class;
    }

}
