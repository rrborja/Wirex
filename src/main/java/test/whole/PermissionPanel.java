/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.whole;

import javax.swing.JCheckBox;
import net.wirex.AppEngine;
import net.wirex.annotations.Bind;
import net.wirex.annotations.Data;

/**
 *
 * @author ritchie
 */
@Bind(model = MyPermissionLevel.class, presenter = PermissionPresenter.class)
public class PermissionPanel extends javax.swing.JPanel {

    /**
     * Creates new form PermissionPanel
     */
    public PermissionPanel() {
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

        jCheckBox1 = AppEngine.checkout(JCheckBox.class, "username");
        jCheckBox2 = AppEngine.checkout(JCheckBox.class, "password");
        jCheckBox3 = AppEngine.checkout(JCheckBox.class, "email");
        jCheckBox4 = AppEngine.checkout(JCheckBox.class, "phoneNumber");

        jCheckBox1.setText("Username");

        jCheckBox2.setText("Password");

        jCheckBox3.setText("Email");

        jCheckBox4.setText("Phone Number");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    @Data("username")
    private javax.swing.JCheckBox jCheckBox1;
    @Data("password")
    private javax.swing.JCheckBox jCheckBox2;
    @Data("email")
    private javax.swing.JCheckBox jCheckBox3;
    @Data("phoneNumber")
    private javax.swing.JCheckBox jCheckBox4;
    // End of variables declaration//GEN-END:variables
}
