/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.menu;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import net.wirex.annotations.Bind;
import net.wirex.annotations.Draw;
import net.wirex.annotations.Event;
import static test.menu.Icons.ICON_ACCOUNT_MANAGER_16;
import static test.menu.Icons.ICON_ADDSUBSCRIBER_16;
import static test.menu.Icons.ICON_BUG_RED;
import static test.menu.Icons.ICON_CANCEL_SMALL;
import static test.menu.Icons.ICON_CASHIER_16;
import static test.menu.Icons.ICON_CHART_SMALL;
import static test.menu.Icons.ICON_COPY;
import static test.menu.Icons.ICON_CURRENCY_DOLLAR_SMALL;
import static test.menu.Icons.ICON_CUT;
import static test.menu.Icons.ICON_DISK_BLUE_SMALL;
import static test.menu.Icons.ICON_DOCUMENT_16;
import static test.menu.Icons.ICON_DOCUMENT_VIEW;
import static test.menu.Icons.ICON_EARTH_LOCATION_16;
import static test.menu.Icons.ICON_EMAIL_SUBSCRIBER_SMALL;
import static test.menu.Icons.ICON_FUNNEL;
import static test.menu.Icons.ICON_GEARS_16;
import static test.menu.Icons.ICON_HELP2_16;
import static test.menu.Icons.ICON_HOSTIN2;
import static test.menu.Icons.ICON_LOCK;
import static test.menu.Icons.ICON_LOGINSCHECK_16;
import static test.menu.Icons.ICON_MAILBOX_FULL_SMALL;
import static test.menu.Icons.ICON_MAIL_ATTACHMENT_SMALL;
import static test.menu.Icons.ICON_MONEY_ENVELOPE_SMALL;
import static test.menu.Icons.ICON_NEWS;
import static test.menu.Icons.ICON_NEWS_CHANGE;
import static test.menu.Icons.ICON_NOTE_ADD;
import static test.menu.Icons.ICON_PASTE;
import static test.menu.Icons.ICON_POPS_16;
import static test.menu.Icons.ICON_PRINTER;
import static test.menu.Icons.ICON_PRINTINSTRUCTIONS_16;
import static test.menu.Icons.ICON_RADIUS_LOGS_16;
import static test.menu.Icons.ICON_REFRESH;
import static test.menu.Icons.ICON_REFRESH_16;
import static test.menu.Icons.ICON_REPORTS16;
import static test.menu.Icons.ICON_SERVER_MAIL_UPLOAD_SMALL;
import static test.menu.Icons.ICON_SHELF_16;
import static test.menu.Icons.ICON_SPAM16X16;
import static test.menu.Icons.ICON_STOP_16;
import static test.menu.Icons.ICON_SUBSCRIBERCURRENT_16;
import static test.menu.Icons.ICON_SUBSCRIBERDUE_16;
import static test.menu.Icons.ICON_SUBSCRIBERPASTDUE_16;
import static test.menu.Icons.ICON_SUBSCRIBERSUSPENDED_16;
import static test.menu.Icons.ICON_SUBSCRIBERUNACTIVE_16;
import static test.menu.Icons.ICON_SUBSCRIBER_DELETE_16;
import static test.menu.Icons.ICON_TICKET_ADD_16;
import static test.menu.Icons.ICON_TICKET_MANAGER_16;
import static test.menu.Icons.ICON_USAGEBASED_BILLING_16;
import static test.menu.Icons.ICON_USER_MONITOR16;
import static test.menu.Icons.ICON_VIEW;
import static test.menu.Icons.ICON_VISPICON_SMALL;

/**
 *
 * @author jsy
 */
@Bind(model = MainMenuBarModel.class, presenter = MainMenuBarPresenter.class)
public class MainMenuBar extends JMenuBar {

    MainMenuBarPresenter presenter;

    public final static String FILE = "file";
    public final static String EDIT = "edit";
    public final static String VIEW = "view";
    public final static String SUBSCRIBER = "customer";
    public final static String TOOLS = "tools";
    public final static String REPORTS = "reports";
    public final static String HELP = "help";
    public final static String NEW_SUBSCRIBER = "new_customer";
    public final static String ISP_CONFIGURATION = "settings";
    public final static String UPDATE_PASSWORD = "update_password";
    public final static String DELETE_SUBSCRIBER = "delete_customer";
    public final static String PRINT = "print";
    public final static String EXIT = "exit";

    /**
     * Creates new form MainMenu
     */
    public MainMenuBar() {
	initComponents();
	presenter = new MainMenuBarPresenter(this);
	initListeners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuFile = new JMenu();
        itmNewSubscriber = new JMenuItem();
        itmISPConfiguration = new JMenuItem();
        itmUpdatePassword = new JMenuItem();
        sep1 = new JPopupMenu.Separator();
        itmDeleteSubscriber = new JMenuItem();
        sep2 = new JPopupMenu.Separator();
        itmPrint = new JMenuItem();
        sep3 = new JPopupMenu.Separator();
        itmExit = new JMenuItem();
        mnuEdit = new JMenu();
        itmSave = new JMenuItem();
        itmCancel = new JMenuItem();
        sep4 = new JPopupMenu.Separator();
        itmCut = new JMenuItem();
        itmCopy = new JMenuItem();
        itmPaste = new JMenuItem();
        sep5 = new JPopupMenu.Separator();
        itmSelectAll = new JMenuItem();
        sep6 = new JPopupMenu.Separator();
        itmFind = new JMenuItem();
        mnuView = new JMenu();
        chkToolBar = new JCheckBoxMenuItem();
        chkStatusBar = new JCheckBoxMenuItem();
        itmChooseColumns = new JMenuItem();
        mnuFilter = new JMenu();
        itmPaidUp = new JMenuItem();
        itmDue = new JMenuItem();
        itmPastDue = new JMenuItem();
        itmSuspended = new JMenuItem();
        itmInactive = new JMenuItem();
        itmRefresh = new JMenuItem();
        mnuSubscriber = new JMenu();
        mnuAdd = new JMenu();
        itmNote = new JMenuItem();
        itmTicket = new JMenuItem();
        mnuAccounting = new JMenu();
        itmEditInvoice = new JMenuItem();
        itmStatementPreview = new JMenuItem();
        itmReceivePayment = new JMenuItem();
        itmCreditDebitMemo = new JMenuItem();
        mnuActions = new JMenu();
        mnuEmailSubscriber = new JMenu();
        itmUsingUBO = new JMenuItem();
        itmDefaultMailSoftware = new JMenuItem();
        mnuEmailReminder = new JMenu();
        mnuSubscriberTools = new JMenu();
        itmAccountManager = new JMenuItem();
        mnuCheckEmail = new JMenu();
        mnuSpamFilter = new JMenu();
        itmDialupUsageShaping = new JMenuItem();
        itmCheckPOPs = new JMenuItem();
        mnuPrequalifySubscriber = new JMenu();
        itmSubscriberAddress = new JMenuItem();
        itmBillingAddress = new JMenuItem();
        mnuSubscriberReports = new JMenu();
        itmSubscriberInstructions = new JMenuItem();
        mnuServiceCallReport = new JMenu();
        itmAcceleratedDialup = new JMenuItem();
        itmAlias = new JMenuItem();
        itmAttDSL = new JMenuItem();
        itmCenturyLinkDSL = new JMenuItem();
        itmDialup = new JMenuItem();
        itmEmail = new JMenuItem();
        itmHosting = new JMenuItem();
        itmWireless = new JMenuItem();
        mnuCheckLogins = new JMenu();
        mnuCheckUsage = new JMenu();
        mnuTools = new JMenu();
        itmMassEmail = new JMenuItem();
        itmFilter = new JMenuItem();
        itmCheckOnlineUsers = new JMenuItem();
        itmEquipmentManager = new JMenuItem();
        itmTicketScheduleManager = new JMenuItem();
        itmMonitoringMapping = new JMenuItem();
        itmDomainHosting = new JMenuItem();
        itmRadiusLogs = new JMenuItem();
        mnuReports = new JMenu();
        itmReportCenter = new JMenuItem();
        mnuHelp = new JMenu();
        itmHelp = new JMenuItem();
        itmResetTutorialDialogs = new JMenuItem();
        itmNocNuuz = new JMenuItem();
        itmChangeLog = new JMenuItem();
        itmBugTracker = new JMenuItem();
        itmUBOLicenseAgreement = new JMenuItem();
        sep7 = new JPopupMenu.Separator();
        itmAboutUBO = new JMenuItem();

        mnuFile.setText("File");
        mnuFile.setName("mnuFile"); // NOI18N

        itmNewSubscriber.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        itmNewSubscriber.setText("New Subscriber");
        itmNewSubscriber.setName("itmNewSubscriber"); // NOI18N
        mnuFile.add(itmNewSubscriber);

        itmISPConfiguration.setText("ISP Configuration");
        itmISPConfiguration.setName("itmISPConfiguration"); // NOI18N
        mnuFile.add(itmISPConfiguration);

        itmUpdatePassword.setText("Update Password");
        itmUpdatePassword.setName("itmUpdatePassword"); // NOI18N
        mnuFile.add(itmUpdatePassword);

        sep1.setName("sep1"); // NOI18N
        mnuFile.add(sep1);

        itmDeleteSubscriber.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        itmDeleteSubscriber.setText("Delete Subscriber");
        itmDeleteSubscriber.setName("itmDeleteSubscriber"); // NOI18N
        mnuFile.add(itmDeleteSubscriber);

        sep2.setName("sep2"); // NOI18N
        mnuFile.add(sep2);

        itmPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        itmPrint.setText("Print");
        itmPrint.setName("itmPrint"); // NOI18N
        mnuFile.add(itmPrint);

        sep3.setName("sep3"); // NOI18N
        mnuFile.add(sep3);

        itmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        itmExit.setText("Exit");
        itmExit.setName("itmExit"); // NOI18N
        mnuFile.add(itmExit);

        add(mnuFile);

        mnuEdit.setText("Edit");
        mnuEdit.setName("mnuEdit"); // NOI18N

        itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        itmSave.setText("Save");
        itmSave.setName("itmSave"); // NOI18N
        mnuEdit.add(itmSave);

        itmCancel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        itmCancel.setText("Cancel");
        itmCancel.setName("itmCancel"); // NOI18N
        mnuEdit.add(itmCancel);

        sep4.setName("sep4"); // NOI18N
        mnuEdit.add(sep4);

        itmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        itmCut.setText("Cut");
        itmCut.setName("itmCut"); // NOI18N
        mnuEdit.add(itmCut);

        itmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        itmCopy.setText("Copy");
        itmCopy.setName("itmCopy"); // NOI18N
        mnuEdit.add(itmCopy);

        itmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        itmPaste.setText("Paste");
        itmPaste.setName("itmPaste"); // NOI18N
        mnuEdit.add(itmPaste);

        sep5.setName("sep5"); // NOI18N
        mnuEdit.add(sep5);

        itmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        itmSelectAll.setText("Select All");
        itmSelectAll.setName("itmSelectAll"); // NOI18N
        mnuEdit.add(itmSelectAll);

        sep6.setName("sep6"); // NOI18N
        mnuEdit.add(sep6);

        itmFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        itmFind.setText("Find");
        itmFind.setName("itmFind"); // NOI18N
        mnuEdit.add(itmFind);

        add(mnuEdit);

        mnuView.setText("View");
        mnuView.setName("mnuView"); // NOI18N

        chkToolBar.setSelected(true);
        chkToolBar.setText("Tool Bar");
        chkToolBar.setName("chkToolBar"); // NOI18N
        mnuView.add(chkToolBar);

        chkStatusBar.setSelected(true);
        chkStatusBar.setText("Status Bar");
        chkStatusBar.setName("chkStatusBar"); // NOI18N
        mnuView.add(chkStatusBar);

        itmChooseColumns.setText("Choose Columns...");
        itmChooseColumns.setName("itmChooseColumns"); // NOI18N
        mnuView.add(itmChooseColumns);

        mnuFilter.setText("Filter");
        mnuFilter.setName("mnuFilter"); // NOI18N

        itmPaidUp.setText("Paid Up");
        itmPaidUp.setName("itmPaidUp"); // NOI18N
        mnuFilter.add(itmPaidUp);

        itmDue.setText("Due");
        itmDue.setName("itmDue"); // NOI18N
        mnuFilter.add(itmDue);

        itmPastDue.setText("Past Due");
        itmPastDue.setName("itmPastDue"); // NOI18N
        mnuFilter.add(itmPastDue);

        itmSuspended.setText("Suspended");
        itmSuspended.setName("itmSuspended"); // NOI18N
        mnuFilter.add(itmSuspended);

        itmInactive.setText("Inactive");
        itmInactive.setName("itmInactive"); // NOI18N
        mnuFilter.add(itmInactive);

        mnuView.add(mnuFilter);

        itmRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        itmRefresh.setText("Refresh");
        itmRefresh.setName("itmRefresh"); // NOI18N
        mnuView.add(itmRefresh);

        add(mnuView);

        mnuSubscriber.setText("Subscriber");
        mnuSubscriber.setName("mnuSubscriber"); // NOI18N

        mnuAdd.setText("Add");
        mnuAdd.setName("mnuAdd"); // NOI18N

        itmNote.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        itmNote.setText("Note");
        itmNote.setName("itmNote"); // NOI18N
        mnuAdd.add(itmNote);

        itmTicket.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        itmTicket.setText("Ticket");
        itmTicket.setName("itmTicket"); // NOI18N
        mnuAdd.add(itmTicket);

        mnuSubscriber.add(mnuAdd);

        mnuAccounting.setText("Accounting");
        mnuAccounting.setName("mnuAccounting"); // NOI18N

        itmEditInvoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        itmEditInvoice.setText("Edit Invoice");
        itmEditInvoice.setName("itmEditInvoice"); // NOI18N
        mnuAccounting.add(itmEditInvoice);

        itmStatementPreview.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_MASK));
        itmStatementPreview.setText("Statement Preview");
        itmStatementPreview.setName("itmStatementPreview"); // NOI18N
        mnuAccounting.add(itmStatementPreview);

        itmReceivePayment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        itmReceivePayment.setText("Receive Payment");
        itmReceivePayment.setName("itmReceivePayment"); // NOI18N
        mnuAccounting.add(itmReceivePayment);

        itmCreditDebitMemo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        itmCreditDebitMemo.setText("Credit/Debit Memo");
        itmCreditDebitMemo.setName("itmCreditDebitMemo"); // NOI18N
        mnuAccounting.add(itmCreditDebitMemo);

        mnuSubscriber.add(mnuAccounting);

        mnuActions.setText("Actions");
        mnuActions.setName("mnuActions"); // NOI18N

        mnuEmailSubscriber.setText("Email Subscriber");
        mnuEmailSubscriber.setName("mnuEmailSubscriber"); // NOI18N

        itmUsingUBO.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
        itmUsingUBO.setText("using Ultimate Back Office");
        itmUsingUBO.setName("itmUsingUBO"); // NOI18N
        mnuEmailSubscriber.add(itmUsingUBO);

        itmDefaultMailSoftware.setText("using default mail software");
        itmDefaultMailSoftware.setName("itmDefaultMailSoftware"); // NOI18N
        mnuEmailSubscriber.add(itmDefaultMailSoftware);

        mnuActions.add(mnuEmailSubscriber);

        mnuEmailReminder.setText("Email Reminder");
        mnuEmailReminder.setName("mnuEmailReminder"); // NOI18N
        mnuActions.add(mnuEmailReminder);

        mnuSubscriber.add(mnuActions);

        mnuSubscriberTools.setText("Subscriber Tools");
        mnuSubscriberTools.setName("mnuSubscriberTools"); // NOI18N

        itmAccountManager.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
        itmAccountManager.setText("Account Manager");
        itmAccountManager.setName("itmAccountManager"); // NOI18N
        mnuSubscriberTools.add(itmAccountManager);

        mnuCheckEmail.setText("Check Email");
        mnuCheckEmail.setName("mnuCheckEmail"); // NOI18N
        mnuSubscriberTools.add(mnuCheckEmail);

        mnuSpamFilter.setText("Spam Filter");
        mnuSpamFilter.setName("mnuSpamFilter"); // NOI18N
        mnuSubscriberTools.add(mnuSpamFilter);

        itmDialupUsageShaping.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        itmDialupUsageShaping.setText("Dialup Usage Shaping");
        itmDialupUsageShaping.setName("itmDialupUsageShaping"); // NOI18N
        mnuSubscriberTools.add(itmDialupUsageShaping);

        itmCheckPOPs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itmCheckPOPs.setText("Check POPs");
        itmCheckPOPs.setName("itmCheckPOPs"); // NOI18N
        mnuSubscriberTools.add(itmCheckPOPs);

        mnuPrequalifySubscriber.setText("Prequalify Subscriber");
        mnuPrequalifySubscriber.setName("mnuPrequalifySubscriber"); // NOI18N

        itmSubscriberAddress.setText("Subscriber Address");
        itmSubscriberAddress.setName("itmSubscriberAddress"); // NOI18N
        mnuPrequalifySubscriber.add(itmSubscriberAddress);

        itmBillingAddress.setText("Billing Address");
        itmBillingAddress.setName("itmBillingAddress"); // NOI18N
        mnuPrequalifySubscriber.add(itmBillingAddress);

        mnuSubscriberTools.add(mnuPrequalifySubscriber);

        mnuSubscriber.add(mnuSubscriberTools);

        mnuSubscriberReports.setText("Subscriber Reports");
        mnuSubscriberReports.setName("mnuSubscriberReports"); // NOI18N

        itmSubscriberInstructions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
        itmSubscriberInstructions.setText("Subscriber Instructions");
        itmSubscriberInstructions.setName("itmSubscriberInstructions"); // NOI18N
        mnuSubscriberReports.add(itmSubscriberInstructions);

        mnuServiceCallReport.setText("Service Call Report");
        mnuServiceCallReport.setName("mnuServiceCallReport"); // NOI18N

        itmAcceleratedDialup.setText("Accelerated Dialup");
        itmAcceleratedDialup.setName("itmAcceleratedDialup"); // NOI18N
        mnuServiceCallReport.add(itmAcceleratedDialup);

        itmAlias.setText("Alias");
        itmAlias.setName("itmAlias"); // NOI18N
        mnuServiceCallReport.add(itmAlias);

        itmAttDSL.setText("AT&T DSL");
        itmAttDSL.setName("itmAttDSL"); // NOI18N
        mnuServiceCallReport.add(itmAttDSL);

        itmCenturyLinkDSL.setText("CenturyLink DSL");
        itmCenturyLinkDSL.setName("itmCenturyLinkDSL"); // NOI18N
        mnuServiceCallReport.add(itmCenturyLinkDSL);

        itmDialup.setText("Dialup");
        itmDialup.setName("itmDialup"); // NOI18N
        mnuServiceCallReport.add(itmDialup);

        itmEmail.setText("Email");
        itmEmail.setName("itmEmail"); // NOI18N
        mnuServiceCallReport.add(itmEmail);

        itmHosting.setText("Hosting");
        itmHosting.setName("itmHosting"); // NOI18N
        mnuServiceCallReport.add(itmHosting);

        itmWireless.setText("Wireless");
        itmWireless.setName("itmWireless"); // NOI18N
        mnuServiceCallReport.add(itmWireless);

        mnuSubscriberReports.add(mnuServiceCallReport);

        mnuCheckLogins.setText("Check Logins");
        mnuCheckLogins.setName("mnuCheckLogins"); // NOI18N
        mnuSubscriberReports.add(mnuCheckLogins);

        mnuCheckUsage.setText("Check Usage");
        mnuCheckUsage.setName("mnuCheckUsage"); // NOI18N
        mnuSubscriberReports.add(mnuCheckUsage);

        mnuSubscriber.add(mnuSubscriberReports);

        add(mnuSubscriber);

        mnuTools.setText("Tools");
        mnuTools.setName("mnuTools"); // NOI18N

        itmMassEmail.setText("Mass Email");
        itmMassEmail.setName("itmMassEmail"); // NOI18N
        mnuTools.add(itmMassEmail);

        itmFilter.setText("Filter");
        itmFilter.setName("itmFilter"); // NOI18N
        mnuTools.add(itmFilter);

        itmCheckOnlineUsers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_MASK));
        itmCheckOnlineUsers.setMnemonic('O');
        itmCheckOnlineUsers.setText("Check Online Users");
        itmCheckOnlineUsers.setName("itmCheckOnlineUsers"); // NOI18N
        mnuTools.add(itmCheckOnlineUsers);

        itmEquipmentManager.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
        itmEquipmentManager.setText("Equipment Manager");
        itmEquipmentManager.setName("itmEquipmentManager"); // NOI18N
        mnuTools.add(itmEquipmentManager);

        itmTicketScheduleManager.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.ALT_MASK));
        itmTicketScheduleManager.setText("Ticket & Schedule Manager");
        itmTicketScheduleManager.setName("itmTicketScheduleManager"); // NOI18N
        mnuTools.add(itmTicketScheduleManager);

        itmMonitoringMapping.setText("Monitoring & Mapping");
        itmMonitoringMapping.setName("itmMonitoringMapping"); // NOI18N
        mnuTools.add(itmMonitoringMapping);

        itmDomainHosting.setText("Domain Hosting");
        itmDomainHosting.setName("itmDomainHosting"); // NOI18N
        mnuTools.add(itmDomainHosting);

        itmRadiusLogs.setText("RADIUS Logs");
        itmRadiusLogs.setName("itmRadiusLogs"); // NOI18N
        mnuTools.add(itmRadiusLogs);

        add(mnuTools);

        mnuReports.setText("Reports");
        mnuReports.setName("mnuReports"); // NOI18N

        itmReportCenter.setText("Report Center");
        itmReportCenter.setName("itmReportCenter"); // NOI18N
        mnuReports.add(itmReportCenter);

        add(mnuReports);

        mnuHelp.setText("Help");
        mnuHelp.setName("mnuHelp"); // NOI18N

        itmHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        itmHelp.setText("Help");
        itmHelp.setName("itmHelp"); // NOI18N
        mnuHelp.add(itmHelp);

        itmResetTutorialDialogs.setText("Reset Tutorial Dialogs");
        itmResetTutorialDialogs.setName("itmResetTutorialDialogs"); // NOI18N
        itmResetTutorialDialogs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                itmResetTutorialDialogsActionPerformed(evt);
            }
        });
        mnuHelp.add(itmResetTutorialDialogs);

        itmNocNuuz.setText("NOC Nuuz");
        itmNocNuuz.setName("itmNocNuuz"); // NOI18N
        mnuHelp.add(itmNocNuuz);

        itmChangeLog.setText("Change Log");
        itmChangeLog.setName("itmChangeLog"); // NOI18N
        mnuHelp.add(itmChangeLog);

        itmBugTracker.setText("Bug Tracker");
        itmBugTracker.setName("itmBugTracker"); // NOI18N
        mnuHelp.add(itmBugTracker);

        itmUBOLicenseAgreement.setText("UBO License Agreement");
        itmUBOLicenseAgreement.setName("itmUBOLicenseAgreement"); // NOI18N
        mnuHelp.add(itmUBOLicenseAgreement);

        sep7.setName("sep7"); // NOI18N
        mnuHelp.add(sep7);

        itmAboutUBO.setText("About UBO");
        itmAboutUBO.setName("itmAboutUBO"); // NOI18N
        mnuHelp.add(itmAboutUBO);

        add(mnuHelp);
    }// </editor-fold>//GEN-END:initComponents

    private void itmResetTutorialDialogsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_itmResetTutorialDialogsActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_itmResetTutorialDialogsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    @Event("toggleStatusBar")
    private JCheckBoxMenuItem chkStatusBar;
    @Event("toggleToolBar")
    private JCheckBoxMenuItem chkToolBar;
    @Draw(ICON_VISPICON_SMALL)
    @Event("showAbout")
    private JMenuItem itmAboutUBO;
    private JMenuItem itmAcceleratedDialup;
    @Draw(ICON_ACCOUNT_MANAGER_16)
    @Event("showAccountManager")
    private JMenuItem itmAccountManager;
    private JMenuItem itmAlias;
    private JMenuItem itmAttDSL;
    @Event("prequalifyBillingAddress")
    private JMenuItem itmBillingAddress;
    @Draw(ICON_BUG_RED)
    @Event("showBugTracker")
    private JMenuItem itmBugTracker;
    @Draw(ICON_CANCEL_SMALL)
    @Event("cancel")
    private JMenuItem itmCancel;
    private JMenuItem itmCenturyLinkDSL;
    @Draw(ICON_NEWS_CHANGE)
    @Event("showChangeLog")
    private JMenuItem itmChangeLog;
    @Draw(ICON_USER_MONITOR16)
    @Event("checkOnlineUsers")
    private JMenuItem itmCheckOnlineUsers;
    @Draw(ICON_POPS_16)
    @Event("checkPops")
    private JMenuItem itmCheckPOPs;
    @Event("showColumnChooser")
    private JMenuItem itmChooseColumns;
    @Draw(ICON_COPY)
    @Event("copy")
    private JMenuItem itmCopy;
    @Draw(ICON_MONEY_ENVELOPE_SMALL)
    @Event("showCreditDebitMemo")
    private JMenuItem itmCreditDebitMemo;
    @Draw(ICON_CUT)
    @Event("cut")
    private JMenuItem itmCut;
    @Event("sendMailDefault")
    private JMenuItem itmDefaultMailSoftware;
    @Draw(ICON_SUBSCRIBER_DELETE_16)
    @Event("deleteSubscriber")
    private JMenuItem itmDeleteSubscriber;
    private JMenuItem itmDialup;
    @Draw(ICON_USAGEBASED_BILLING_16)
    @Event("dialupUsageShaping")
    private JMenuItem itmDialupUsageShaping;
    @Draw(ICON_HOSTIN2)
    @Event("domainHosting")
    private JMenuItem itmDomainHosting;
    @Draw(ICON_SUBSCRIBERDUE_16)
    @Event("filterDue")
    private JMenuItem itmDue;
    @Draw(ICON_CURRENCY_DOLLAR_SMALL)
    @Event("editInvoice")
    private JMenuItem itmEditInvoice;
    private JMenuItem itmEmail;
    @Draw(ICON_SHELF_16)
    @Event("showEquipmentManager")
    private JMenuItem itmEquipmentManager;
    @Draw(ICON_STOP_16)
    @Event("exit")
    private JMenuItem itmExit;
    @Draw(ICON_FUNNEL)
    @Event("toggleFilter")
    private JMenuItem itmFilter;
    @Draw(ICON_VIEW)
    @Event("find")
    private JMenuItem itmFind;
    @Draw(ICON_HELP2_16)
    @Event("showOnlineHelp")
    private JMenuItem itmHelp;
    private JMenuItem itmHosting;
    @Draw(ICON_GEARS_16)
    @Event("showIspSettings")
    private JMenuItem itmISPConfiguration;
    @Draw(ICON_SUBSCRIBERUNACTIVE_16)
    @Event("filterInactive")
    private JMenuItem itmInactive;
    @Draw(ICON_SERVER_MAIL_UPLOAD_SMALL)
    @Event("toggleMassEmail")
    private JMenuItem itmMassEmail;
    @Draw(ICON_EARTH_LOCATION_16)
    @Event("showMonitoringMapping")
    private JMenuItem itmMonitoringMapping;
    @Draw(ICON_ADDSUBSCRIBER_16)
    @Event("addNewSubscriber")
    private JMenuItem itmNewSubscriber;
    @Draw(ICON_NEWS)
    @Event("showNocNuuz")
    private JMenuItem itmNocNuuz;
    @Draw(ICON_NOTE_ADD)
    @Event("showAddNote")
    private JMenuItem itmNote;
    @Draw(ICON_SUBSCRIBERCURRENT_16)
    @Event("filterPaidUp")
    private JMenuItem itmPaidUp;
    @Draw(ICON_SUBSCRIBERPASTDUE_16)
    @Event("filterPastDue")
    private JMenuItem itmPastDue;
    @Draw(ICON_PASTE)
    @Event("paste")
    private JMenuItem itmPaste;
    @Draw(ICON_PRINTER)
    @Event("print")
    private JMenuItem itmPrint;
    @Draw(ICON_RADIUS_LOGS_16)
    @Event("showRadiusLogs")
    private JMenuItem itmRadiusLogs;
    @Draw(ICON_CASHIER_16)
    @Event("toggleReceivePayment")
    private JMenuItem itmReceivePayment;
    @Draw(ICON_REFRESH_16)
    @Event("refresh")
    private JMenuItem itmRefresh;
    @Draw(ICON_DOCUMENT_16)
    @Event("showReportCenter")
    private JMenuItem itmReportCenter;
    @Draw(ICON_REFRESH)
    @Event("resetTutorialDialogs")
    private JMenuItem itmResetTutorialDialogs;
    @Draw(ICON_DISK_BLUE_SMALL)
    @Event("save")
    private JMenuItem itmSave;
    @Event("selectAll")
    private JMenuItem itmSelectAll;
    @Draw(ICON_DOCUMENT_VIEW)
    @Event("showStatementPreview")
    private JMenuItem itmStatementPreview;
    @Event("prequalifySubscriberAddress")
    private JMenuItem itmSubscriberAddress;
    @Draw(ICON_PRINTINSTRUCTIONS_16)
    @Event("showSubscriberInstructions")
    private JMenuItem itmSubscriberInstructions;
    @Draw(ICON_SUBSCRIBERSUSPENDED_16)
    @Event("filterSuspended")
    private JMenuItem itmSuspended;
    @Draw(ICON_TICKET_ADD_16)
    @Event("showAddTicket")
    private JMenuItem itmTicket;
    @Draw(ICON_TICKET_MANAGER_16)
    @Event("showTicketScheduleManager")
    private JMenuItem itmTicketScheduleManager;
    @Draw(ICON_REPORTS16)
    @Event("showLicenseAgreement")
    private JMenuItem itmUBOLicenseAgreement;
    @Draw(ICON_LOCK)
    @Event("updatePassword")
    private JMenuItem itmUpdatePassword;
    @Event("sendMailUsingUBO")
    private JMenuItem itmUsingUBO;
    private JMenuItem itmWireless;
    private JMenu mnuAccounting;
    private JMenu mnuActions;
    private JMenu mnuAdd;
    @Draw(ICON_MAILBOX_FULL_SMALL)
    private JMenu mnuCheckEmail;
    @Draw(ICON_LOGINSCHECK_16)
    private JMenu mnuCheckLogins;
    @Draw(ICON_CHART_SMALL)
    private JMenu mnuCheckUsage;
    private JMenu mnuEdit;
    @Draw(ICON_MAIL_ATTACHMENT_SMALL)
    private JMenu mnuEmailReminder;
    @Draw(ICON_EMAIL_SUBSCRIBER_SMALL)
    private JMenu mnuEmailSubscriber;
    private JMenu mnuFile;
    private JMenu mnuFilter;
    private JMenu mnuHelp;
    @Draw(ICON_EARTH_LOCATION_16)
    private JMenu mnuPrequalifySubscriber;
    private JMenu mnuReports;
    private JMenu mnuServiceCallReport;
    @Draw(ICON_SPAM16X16)
    private JMenu mnuSpamFilter;
    private JMenu mnuSubscriber;
    private JMenu mnuSubscriberReports;
    private JMenu mnuSubscriberTools;
    private JMenu mnuTools;
    private JMenu mnuView;
    private JPopupMenu.Separator sep1;
    private JPopupMenu.Separator sep2;
    private JPopupMenu.Separator sep3;
    private JPopupMenu.Separator sep4;
    private JPopupMenu.Separator sep5;
    private JPopupMenu.Separator sep6;
    private JPopupMenu.Separator sep7;
    // End of variables declaration//GEN-END:variables

    private void initListeners() {
	chkStatusBar.addActionListener(e -> {
	    presenter.toggleStatusBar(e);
	});
	chkToolBar.addActionListener(e -> {
	    presenter.toggleToolBar(e);
	});
	itmAboutUBO.addActionListener(e -> {
	    presenter.showAbout();
	});
	itmAccountManager.addActionListener(e -> {
	    presenter.showAccountManager();
	});
	itmBillingAddress.addActionListener(e -> {
	    presenter.prequalifyBillingAddress();
	});
	itmBugTracker.addActionListener(e -> {
	    presenter.showBugTracker();
	});
	itmCancel.addActionListener(e -> {
	    presenter.cancel();
	});
	itmChangeLog.addActionListener(e -> {
	    presenter.showChangeLog();
	});
	itmCheckOnlineUsers.addActionListener(e -> {
	    presenter.checkOnlineUsers();
	});
	itmCheckPOPs.addActionListener(e -> {
	    presenter.checkPops();
	});
	itmChooseColumns.addActionListener(e -> {
	    presenter.showColumnChooser();
	});
	itmCopy.addActionListener(e -> {
	    presenter.copy();
	});
	itmCreditDebitMemo.addActionListener(e -> {
	    presenter.showCreditDebitMemo();
	});
	itmCut.addActionListener(e -> {
	    presenter.cut();
	});
	itmDefaultMailSoftware.addActionListener(e -> {
	    presenter.sendMailDefault();
	});
	itmDeleteSubscriber.addActionListener(e -> {
	    presenter.deleteSubscriber();
	});
	itmDialupUsageShaping.addActionListener(e -> {
	    presenter.dialupUsageShaping();
	});
	itmDomainHosting.addActionListener(e -> {
	    presenter.domainHosting();
	});
	itmDue.addActionListener(e -> {
	    presenter.filterDue();
	});
	itmEditInvoice.addActionListener(e -> {
	    presenter.editInvoice();
	});
	itmEquipmentManager.addActionListener(e -> {
	    presenter.showEquipmentManager();
	});
	itmExit.addActionListener(e -> {
	    presenter.exit();
	});
	itmFilter.addActionListener(e -> {
	    presenter.toggleFilter();
	});
	itmFind.addActionListener(e -> {
	    presenter.find();
	});
	itmHelp.addActionListener(e -> {
	    presenter.showOnlineHelp();
	});
	itmISPConfiguration.addActionListener(e -> {
	    presenter.showIspSettings();
	});
	itmInactive.addActionListener(e -> {
	    presenter.filterInactive();
	});
	itmMassEmail.addActionListener(e -> {
	    presenter.toggleMassEmail();
	});
	itmMonitoringMapping.addActionListener(e -> {
	    presenter.showMonitoringMapping();
	});
	itmNewSubscriber.addActionListener(e -> {
	    presenter.addNewSubscriber();
	});
	itmNocNuuz.addActionListener(e -> {
	    presenter.showNocNuuz();
	});
	itmNote.addActionListener(e -> {
	    presenter.showAddNote();
	});
	itmPaidUp.addActionListener(e -> {
	    presenter.filterPaidUp();
	});
	itmPastDue.addActionListener(e -> {
	    presenter.filterPastDue();
	});
	itmPaste.addActionListener(e -> {
	    presenter.paste();
	});
	itmPrint.addActionListener(e -> {
	    presenter.print();
	});
	itmRadiusLogs.addActionListener(e -> {
	    presenter.showRadiusLogs();
	});
	itmReceivePayment.addActionListener(e -> {
	    presenter.toggleReceivePayment();
	});
	itmRefresh.addActionListener(e -> {
	    presenter.refresh();
	});
	itmReportCenter.addActionListener(e -> {
	    presenter.showReportCenter();
	});
	itmResetTutorialDialogs.addActionListener(e -> {
	    presenter.resetTutorialDialogs();
	});
	itmSave.addActionListener(e -> {
	    presenter.save();
	});
	itmSelectAll.addActionListener(e -> {
	    presenter.selectAll();
	});
	itmStatementPreview.addActionListener(e -> {
	    presenter.showStatementPreview();
	});
	itmSubscriberAddress.addActionListener(e -> {
	    presenter.prequalifySubscriberAddress();
	});
	itmSubscriberInstructions.addActionListener(e -> {
	    presenter.showSubscriberInstructions();
	});
	itmSuspended.addActionListener(e -> {
	    presenter.filterSuspended();
	});
	itmTicket.addActionListener(e -> {
	    presenter.showAddTicket();
	});
	itmTicketScheduleManager.addActionListener(e -> {
	    presenter.showTicketScheduleManager();
	});
	itmUBOLicenseAgreement.addActionListener(e -> {
	    presenter.showLicenseAgreement();
	});
	itmUpdatePassword.addActionListener(e -> {
	    presenter.updatePassword();
	});
	itmUsingUBO.addActionListener(e -> {
	    presenter.sendMailUsingUBO();
	});
    }

}