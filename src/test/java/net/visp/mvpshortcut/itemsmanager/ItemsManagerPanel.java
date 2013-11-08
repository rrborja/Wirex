/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut.itemsmanager;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.wirex.AppEngine;
import net.wirex.MVP;

import net.wirex.annotations.Bind;
import net.wirex.annotations.Data;
import net.wirex.annotations.Event;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
//import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author jvallar
 */
@Bind(model = ItemModel.class, presenter = ItemPresenter.class)
public class ItemsManagerPanel extends JPanel {

    /**
     * Creates new form EquipmentManagerPanel
     */
    public ItemsManagerPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbType = new javax.swing.JComboBox();
        cmbRecurs = new javax.swing.JComboBox();
        cmbActive = new javax.swing.JComboBox();
        cmbTax = new javax.swing.JComboBox();
        txfFind = (JTextField)net.wirex.AppEngine.checkout("find");
        cmbReports = new javax.swing.JComboBox();
        scrItemManager = new javax.swing.JScrollPane();
        tblItemManager = (JTable) net.wirex.AppEngine.checkout("table");
        jButton1 = new javax.swing.JButton();

        cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DISCOUNT", "FEE", "EQUIPMENT", "Wireless" }));
        cmbType.setName("cmbType"); // NOI18N

        cmbRecurs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Non-Recurring", "Monthly", "Quarterly", "Annuallyl" }));
        cmbRecurs.setName("cmbRecurs"); // NOI18N

        cmbActive.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Y", "N" }));
        cmbActive.setName("cmbActive"); // NOI18N

        cmbTax.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "[tax]", "Add New..." }));
        cmbTax.setName("cmbTax"); // NOI18N

        cmbReports.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Reports" }));
        cmbReports.setName("cmbReports"); // NOI18N

        scrItemManager.setName("scrItemManager"); // NOI18N

        scrItemManager.setViewportView(tblItemManager);

        jButton1.setText("Load");
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrItemManager, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txfFind, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbReports, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbReports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrItemManager, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbActive;
    private javax.swing.JComboBox cmbRecurs;
    private javax.swing.JComboBox cmbReports;
    private javax.swing.JComboBox cmbTax;
    private javax.swing.JComboBox cmbType;
    @Event("update")
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane scrItemManager;
    @Data("table")
    private javax.swing.JTable tblItemManager;
    @Data("find")
    private javax.swing.JTextField txfFind;
    // End of variables declaration//GEN-END:variables

    public static void main(String args[]) {
        try {
            MVP mvpController = AppEngine.prepare(ItemsManagerPanel.class);
            mvpController.display(JFrame.class, true);
        } catch (ViewClassNotBindedException ex) {
            Logger.getLogger(ItemsManagerPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongComponentException ex) {
            Logger.getLogger(ItemsManagerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JTable getTblItems() {
        return tblItemManager;
    }
}
//class DatePickerTableCell extends AbstractCellEditor implements TableCellEditor {
////    private JXDatePicker datePicker;
//
//    public DatePickerTableCell() {
////	this.datePicker = new JXDatePicker(new Date(System.currentTimeMillis()));
//    }
//
//    @Override
//    public Object getCellEditorValue() {
//	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
////	return formatter.format(datePicker.getDate());
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
////	return datePicker;
//    }
//}
