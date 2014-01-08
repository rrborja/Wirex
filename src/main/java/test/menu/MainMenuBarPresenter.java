package test.menu;

import java.awt.event.ActionEvent;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.annotations.Dispose;
import net.wirex.annotations.Fire;
import net.wirex.interfaces.Presenter;

public class MainMenuBarPresenter extends Presenter {

    MainMenuBar mainMenuBar;

    public MainMenuBarPresenter(JMenuBar menu) {
	super(menu);
	this.mainMenuBar = (MainMenuBar) menu;
    }

    public void showReportCenter() {
	System.out.println("success!");
    }


    public void showColumnChooser() {

    }

    public void toggleStatusBar(ActionEvent e) {
	       System.out.println("success!");
    }

    public void toggleToolBar(ActionEvent e) {
	System.out.println("success!");
    }

    public void showAbout() {

    }

    public void showAccountManager() {
	System.out.println("success!");
    }

    public void prequalifyBillingAddress() {

    }

    public void showBugTracker() {
	System.out.println("success!");
    }

    public void cancel() {

    }

    public void showChangeLog() {
	System.out.println("success!");
    }

    public void checkPops() {

    }

    public void checkOnlineUsers() {

    }

    public void copy() {

    }

    public void showCreditDebitMemo() {
	System.out.println("success!");
    }

    public void cut() {

    }

    public void sendMailDefault() {

    }

    public void deleteSubscriber() {
	System.out.println("success!");
    }

    public void dialupUsageShaping() {

    }

    public void domainHosting() {

    }

    public void filterDue() {
	System.out.println("success!");
    }

    public void editInvoice() {

    }

    public void showEquipmentManager() {
	System.out.println("success!");
    }

    @Dispose
    public void exit() {

    }

    public void toggleFilter() {
	System.out.println("success!");
    }

    public void find() {
	System.out.println("success!");
    }

    public void showOnlineHelp() {
	System.out.println("success!");
    }

    public void showIspSettings() {
	System.out.println("success!");
    }

    public void filterInactive() {
	System.out.println("success!");
    }

    public void toggleMassEmail() {
	System.out.println("success!");
    }

    public void showMonitoringMapping() {

    }

    public void addNewSubscriber() {

    }

    public void showNocNuuz() {

    }

    public void showAddNote() {
	System.out.println("success!");
    }

    public void filterPaidUp() {
	System.out.println("success!");
    }

    public void filterPastDue() {
	System.out.println("success!");
    }

    public void paste() {

    }

    public void print() {

    }

    public void showRadiusLogs() {

    }

    public void toggleReceivePayment() {
	System.out.println("success!");
    }

    public void refresh() {

    }

    public void resetTutorialDialogs() {

    }

    public void save() {

    }

    public void selectAll() {

    }

    public void showStatementPreview() {

    }

    public void prequalifySubscriberAddress() {

    }

    public void showSubscriberInstructions() {
	System.out.println("success!");
    }

    public void filterSuspended() {
	System.out.println("success!");
    }

    public void showAddTicket() {
	System.out.println("success!");
    }

    public void showTicketScheduleManager() {
	System.out.println("success!");
    }

    public void showLicenseAgreement() {
	System.out.println("success!");
    }

    public void updatePassword() {

    }

    public void sendMailUsingUBO() {

    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> chm) {

    }
}
