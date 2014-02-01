package test.customcomponent;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import java.awt.PopupMenu;

@SuppressWarnings("serial")
public class VTextField extends JTextField implements MouseListener,
	KeyListener {

    private UndoManager undoManager = new UndoManager();
    private boolean undoable = false;
    private JPopupMenu menuPopup;

    public VTextField() {
	super();
	init();
    }

    public VTextField(Document doc, String text, int columns) {
	super(doc, text, columns);
	init();
    }

    public VTextField(int columns) {
	super(columns);
	init();
    }

    public VTextField(String text, int columns) {
	super(text, columns);
	init();
    }

    public VTextField(String text) {
	super(text);
	init();
    }

    protected void init() {
	this.addMouseListener(this);
	this.addKeyListener(this);

	ActionMap actionMap = this.getActionMap();
	actionMap.put("Undo", new AbstractAction("Undo") {
	    public void actionPerformed(ActionEvent evt) {
		try {
		    undoManager.undo();
		} catch (CannotUndoException e) {
		    Toolkit.getDefaultToolkit().beep();
		}
	    }
	});
	InputMap inputMap = this.getInputMap();
	KeyStroke keyStroke = KeyStroke.getKeyStroke("control Z");
	inputMap.put(keyStroke, "Undo");
    }

    public void setUndoManager() {
	if (!undoable) {
	    final Document document = this.getDocument();
	    document.addUndoableEditListener(new UndoableEditListener() {
		String prevText = getText();

		public void undoableEditHappened(UndoableEditEvent e) {
		    UndoableEdit edit = e.getEdit();
		    String text = getText();
		    if (document instanceof RegexDocument) {
			RegexDocument regexDocument = (RegexDocument) document;
			if (regexDocument.isValidText(text) && !prevText.equals(text)) {
			    undoManager.addEdit(edit);
			    prevText = text;
			}
		    } else {
			undoManager.addEdit(edit);
		    }
		}
	    });
	    undoable = true;
	}
    }

    public UndoManager getUndoManager() {
	return undoManager;
    }

    public void resetUndoManager() {
	undoManager.discardAllEdits();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	if (SwingUtilities.isRightMouseButton(e)) {
	    this.requestFocus();
	    PopupMenu popup = new PopupMenu();
//	    menuPopup = popup.getMenuPopup(this);
//	    menuPopup.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
	setUndoManager();
    }

    public JPopupMenu getMenuPopup() {
	return menuPopup;
    }

    @Override
    public void setText(String t) {
	super.setText(t);
	super.setCaretPosition(0);
    }

    public void grabFocus() {
	super.grabFocus();
    }

    @Override
    protected Document createDefaultModel() {
	String regex = ".*";
	return new RegexDocument(regex);
    }
}