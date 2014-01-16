package test.customcomponent;

import javax.swing.JDialog;
import javax.swing.UnsupportedLookAndFeelException;
import net.wirex.MVP;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.AppEngine;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Formatter;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import java.awt.event.FocusAdapter;
import javax.swing.event.DocumentEvent;
import net.wirex.structures.XComponent;
import net.wirex.structures.XValueListener;

public class MACAddressField extends JPanel implements XComponent {

    boolean focused = false;
    boolean isTyped = false;
    FormattedTextField fmtMacAddress;
    private XValueListener listener;

    public MACAddressField() {
	init();
	initLayout();
	initListeners();
    }

    public BoundedPlainDocument getDocument() {
	return fmtMacAddress.document;
    }

    private void init() {
	this.setLayout(null);
	FormattedTextField.FormatSpec macSpec = new FormattedTextField.FormatSpec("  :  :  :  :  :  ",
		"**:**:**:**:**:**", "[a-f_A-F_0-9]");

	fmtMacAddress = new FormattedTextField() {
	    //feature request to allow full mac addresses to be pasted in
	    protected Document createDefaultModel() {
		document = new BoundedPlainDocument() {
		    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			super.insertString(offset, str.replaceAll("[^a-z0-9A-Z]", ""), a);
		    }
		};
		document.addInsertErrorListener(new BoundedPlainDocument.InsertErrorListener() {
		    public void insertFailed(BoundedPlainDocument doc, int offset, String str, AttributeSet a) {
			// Beep when the field is full
			Toolkit.getDefaultToolkit().beep();
		    }
		});

		return document;
	    }
	};
	fmtMacAddress.setFormatSpec(macSpec);

        fmtMacAddress.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                listener.updateValue(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listener.updateValue(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listener.updateValue(getText());
            }
            
        });
	this.setBackground(Color.WHITE);
    }

    private void initLayout() {
	GroupLayout layout = new GroupLayout(this);
	this.setLayout(layout);
	layout.setHorizontalGroup(layout.createSequentialGroup()
		.addComponent(fmtMacAddress, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE));
	layout.setVerticalGroup(layout.createParallelGroup(LEADING)
		.addComponent(fmtMacAddress));
	this.add(fmtMacAddress, null);
    }

    private void initListeners() {
	fmtMacAddress.addFocusListener(new FocusAdapter() {
	    @Override
	    public void focusLost(FocusEvent e) {
		focusLostAction(e);
	    }

	    @Override
	    public void focusGained(FocusEvent e) {
		isTyped = true;
	    }
	});
	fmtMacAddress.getActionMap().put("copy", new AbstractAction("copy") {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		fieldCopy();
	    }
	});
	fmtMacAddress.getActionMap().put("cut", new AbstractAction("cut") {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		fieldCut();
	    }
	});
    }

    public void fieldCopy() {
	String text = getText();
	int maxLength = 12;
	int diff = maxLength - text.length();
	if (diff > 0) {
	    for (int i = 0; i < diff; i++) {
		text += " ";
	    }
	}

	String copied = String.valueOf(getSelectedText());
	if (copied.isEmpty()) {
	    return;
	}
	int index = text.indexOf(copied);
	index += index / 2;

	String mac1 = text.substring(0, 2);
	String mac2 = text.substring(2, 4);
	String mac3 = text.substring(4, 6);
	String mac4 = text.substring(6, 8);
	String mac5 = text.substring(8, 10);
	String mac6 = text.substring(10, 12);

	StringBuilder sb = new StringBuilder();
	Formatter formatter = new Formatter(sb);
	formatter.format("%2s:%2s:%2s:%2s:%2s:%2s", mac1, mac2, mac3, mac4, mac5, mac6);

	int maxIndex = 17;
	int length = copied.length();
	length += (length / 2) + index;
	if (length > maxIndex) {
	    length -= 1;
	}
	copied = sb.substring(index, length);
	copied = Utils.trimRight(copied, ':');

	StringSelection ss = new StringSelection(copied);
	Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    public void fieldCut() {
	fieldCopy();

	int start = fmtMacAddress.getSelectionStart();
	int end = fmtMacAddress.getSelectionEnd();
	if (start == end) {
	    return;
	}
	String text = getText();
	String text1 = text.substring(0, start);
	String text2 = text.substring(end, text.length());
	setText(text1 + text2);

	fmtMacAddress.setCaretPosition(start);
    }

    public String getSelectedText() {
	return fmtMacAddress.getSelectedText();
    }

    public void setBorderMAC(Border border) {
	fmtMacAddress.setBorder(border);
    }

    public void setText(String phonenumber) {
	fmtMacAddress.setText(phonenumber);
    }

    public String getText() {
	return fmtMacAddress.getText();
    }

    public void setEditable(boolean editable) {
	fmtMacAddress.setEditable(editable);
    }

    public boolean isEditable() {
	return fmtMacAddress.isEditable();
    }

    public void setEnabledMAC(boolean editable) {
	fmtMacAddress.setEnabled(editable);
    }

    public boolean isEnabledMAC() {
	return fmtMacAddress.isEnabled();
    }

    public void setBackgroundMAC(Color color) {
	fmtMacAddress.setBackground(color);
    }

    @Override
    public void setToolTipText(String message) {
	fmtMacAddress.setToolTipText(message);
    }

    public void addFocusListenerMAC(FocusListener focus) {
	fmtMacAddress.addFocusListener(focus);
    }

    public void addKeyListenerMAC(KeyListener key) {
	fmtMacAddress.addKeyListener(key);
    }

    public void addDocumentListenerMAC(DocumentListener doc) {
	fmtMacAddress.getDocument().addDocumentListener(doc);
    }

    public void setNextFocusableComponentMAC(Component aComponent) {
	fmtMacAddress.setNextFocusableComponent(aComponent);
    }

    public boolean isFocused() {
	return focused;
    }

    public void setFocused(boolean focused) {
	this.focused = focused;
    }

    public void selectAll() {
	fmtMacAddress.selectAll();
    }

    public void grabFocusMAC() {
	fmtMacAddress.grabFocus();
    }

    protected void focusLostAction(FocusEvent e) {
	String macaddress = fmtMacAddress.getText();
	if (getInputVerifier() == null && macaddress.length() > 0 && macaddress.length() < 12 && isTyped == true) {
	    errorMessageDialog("MAC address is too short");
	    fmtMacAddress.setText("");
	    fmtMacAddress.grabFocus();
	} else {
	    setFocused(false);
	}
    }

    public int errorMessageDialog(String msg) {
	Object[] options = {"OK"};
	int n = JOptionPane.showOptionDialog(this, msg, "Oops", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
		options, options[0]);

	return n;
    }
    public static void main(String[] args) throws ViewClassNotBindedException, WrongComponentException, UnsupportedLookAndFeelException {
        //<editor-fold defaultstate="collapsed" desc="Initialize Look and feel setting code in OS Native theme">
            try {
                String OS = System.getProperty("os.name").toLowerCase();
                LOOP:
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    switch (info.getName()) {
                        case "Windows":
                            if (OS.indexOf("win") >= 0) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break LOOP;
                            }
                            break;
                        case "Nimbus":
                            if (OS.indexOf("nux") >= 0) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break LOOP;
                            }
                            break;
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            }
            //</editor-fold>
        MVP mvp = AppEngine.prepare(MACAddressField.class);
        mvp.display(JDialog.class, true);
    }

    @Override
    public void setValue(Object value) {
        setText(String.valueOf(value));
    }

    @Override
    public void addValueListener(XValueListener listener) {
        this.listener = listener;
    }
}