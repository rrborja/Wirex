/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.another;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.wirex.ApplicationControllerFactory;
import net.wirex.annotations.Bind;
import net.wirex.annotations.Data;
import net.wirex.annotations.Event;

/**
 *
 * @author RBORJA
 */
@Bind(model=LoginPanelModel.class, presenter=LoginPanelPresenter.class)
public class NewLoginPanel extends javax.swing.JPanel {

    /**
     * Creates new form NewLoginPanel
     */
    public NewLoginPanel() {
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

        lblLogo = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        txfUsername = ApplicationControllerFactory.checkout(JTextField.class, "username");
        lblPassword = new javax.swing.JLabel();
        pwdPassword = ApplicationControllerFactory.checkout(JPasswordField.class, "password");
        chkRemember = ApplicationControllerFactory.checkout("remember");
        btnCancel = new javax.swing.JButton();
        btnEnter = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(400, 200));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setName("lblLogo"); // NOI18N

        lblUsername.setText("Username:");
        lblUsername.setName("lblUsername"); // NOI18N

        txfUsername.setText("test");
        txfUsername.setName("txfUsername"); // NOI18N

        lblPassword.setText("Password:");
        lblPassword.setName("lblPassword"); // NOI18N

        pwdPassword.setText("test");
        pwdPassword.setName("pwdPassword"); // NOI18N

        chkRemember.setText("Remember my password");
        chkRemember.setName("chkRemember"); // NOI18N

        btnCancel.setText("Cancel");
        btnCancel.setName("btnCancel"); // NOI18N

        btnEnter.setText("Enter");
        btnEnter.setName("btnEnter"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txfUsername)
                            .addComponent(pwdPassword)
                            .addComponent(chkRemember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 143, Short.MAX_VALUE)
                                .addComponent(btnEnter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblPassword)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkRemember)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEnter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    @Event({"cancel"})
    private javax.swing.JButton btnCancel;
    @Event({"login"})
    private javax.swing.JButton btnEnter;
    @Data("remember")
    private javax.swing.JCheckBox chkRemember;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    @Data("password")
    private javax.swing.JPasswordField pwdPassword;
    @Data("username")
    private javax.swing.JTextField txfUsername;
    // End of variables declaration//GEN-END:variables
}