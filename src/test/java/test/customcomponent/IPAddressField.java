package test.customcomponent;

import javax.swing.JDialog;
import javax.swing.UnsupportedLookAndFeelException;
import net.wirex.MVP;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.AppEngine;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.Alignment.BASELINE;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import net.wirex.structures.XComponent;
import net.wirex.structures.XValueListener;

public class IPAddressField extends JPanel implements XComponent {

    private XValueListener listener;
    private int tab;
    private String Strmodifier;
    public boolean isTransferedFocus = false;
    String text = null;
    public boolean isNotMax = false;
    public int dotCounter = 0;
    public int counter = 0;
    public boolean isfromMaxLength = false;
    boolean hasChanged = false;
    final int delay = 500;
    final Timer timer = new Timer(delay, null);
    Octet octet0 = new Octet();
    Octet octet1 = new Octet();
    Octet octet2 = new Octet();
    Octet octet3 = new Octet();
    JLabel dot1 = new JLabel(".");
    JLabel dot2 = new JLabel(".");
    JLabel dot3 = new JLabel(".");
    private static final String PATTERN
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public IPAddressField() {
        init();
    }

    private class DocListener implements DocumentListener {

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

    }

    private void init() {
        addDocumentListener(new DocListener());

        this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        this.setBackground(Color.WHITE);
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                Collections.EMPTY_SET);
        this.setFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                Collections.EMPTY_SET);

        for (JLabel dot : getDots()) {
            dot.setOpaque(false);
        }

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(octet0, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
                .addComponent(dot1)
                .addComponent(octet1, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
                .addComponent(dot2)
                .addComponent(octet2, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE)
                .addComponent(dot3)
                .addComponent(octet3, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE));

        layout.setVerticalGroup(layout.createParallelGroup(BASELINE)
                .addGroup(layout.createSequentialGroup()
                        .addGap(2)
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(octet0)
                                .addComponent(dot1)
                                .addComponent(octet1)
                                .addComponent(dot2)
                                .addComponent(octet2)
                                .addComponent(dot3)
                                .addComponent(octet3))
                        .addGap(2)));

        layout.linkSize(SwingConstants.VERTICAL, new Component[]{octet0,
            octet1, octet2, octet3, dot1, dot2, dot3});
    }

    public void fieldCopy() {
        StringSelection ss = new StringSelection(getText());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        doSelectAll();
    }

    public void fieldPaste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        try {
            String value = (String) content
                    .getTransferData(DataFlavor.stringFlavor);
            setText(value);
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void setText(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()
                || ipAddress.equalsIgnoreCase("...")) {
            return;
        }

        String[] pieces = ipAddress.split("\\.");
        Octet[] octets = getOctets();
        for (int i = 0; i < pieces.length; i++) {
            if (i < 4) {
                octets[i].setText(pieces[i].trim());
            }
        }
    }

    protected void octetKeyTyped(KeyEvent e) {
        Octet[] octets = getOctets();
        if (tab == KeyEvent.VK_TAB) {
            if (Strmodifier.equalsIgnoreCase("shift")) {
                octets[0].transferFocusBackward();
            } else {
                octets[3].nextFocus();
            }
        }
        tab = 0;
    }

    public void setEmpty() {
        Octet[] octets = getOctets();
        for (int i = 0; i < 4; i++) {
            octets[i].setText("");
        }
    }

    public static String removeLeadingZeros(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        int index = 0;
        for (; index < str.length(); index++) {
            if (chars[index] != '0') {
                break;
            }
        }
        return (index == 0) ? str : str.substring(index);
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (Octet octet : getOctets()) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            String text = octet.getText();
            sb.append(text);
        }
        return sb.toString();
    }

    public void grabFocusIP() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Octet octetToFocus = octet0;
                for (Octet octet : getOctets()) {
                    if (octet.hasFocus()) {
                        octetToFocus = octet;
                        break;
                    }
                }
                grabFocus();
                octetToFocus.requestFocusInWindow();
            }
        });
        doSelectAll();
    }

    public boolean isFocusOwnerIP() {
        for (Octet octet : getOctets()) {
            if (octet.isFocusOwner()) {
                return true;
            }
        }
        return false;
    }

    public String getFocusOwnerIP() {
        for (Octet octet : getOctets()) {
            if (octet.isFocusOwner()) {
                return octet.getText();
            }
        }
        return null;
    }

    public void setEnabledIP(boolean enabled) {
        for (Octet octet : getOctets()) {
            octet.setEnabled(enabled);
        }
        this.setBackground(octet0.getBackground());
    }

    public void setEditableIP(boolean editable) {
        for (Octet octet : getOctets()) {
            octet.setEditable(editable);
        }
        this.setBackground(octet0.getBackground());
    }

    public void setBackgroundIP(Color color) {
        for (Octet octet : getOctets()) {
            octet.setBackground(color);
        }
        this.setBackground(octet0.getBackground());
    }

    public Octet[] getOctets() {
        return new Octet[]{octet0, octet1, octet2, octet3};
    }

    public JLabel[] getDots() {
        return new JLabel[]{dot1, dot2, dot3};
    }

    public void doSelectAll() {
        KeyboardFocusManager focusManager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();
        final Component focusOwner = focusManager.getFocusOwner();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (focusOwner instanceof Octet) {
                    ((Octet) focusOwner).selectAll();
                }
            }
        });
    }

    public void addDocumentListener(DocumentListener l) {
        this.octet0.getDocument().addDocumentListener(l);
        this.octet1.getDocument().addDocumentListener(l);
        this.octet2.getDocument().addDocumentListener(l);
        this.octet3.getDocument().addDocumentListener(l);
    }

    public void addFocusListener(FocusListener l) {
        this.octet0.addFocusListener(l);
        this.octet1.addFocusListener(l);
        this.octet2.addFocusListener(l);
        this.octet3.addFocusListener(l);
    }

    public void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        this.octet0.addMouseListener(l);
        this.octet1.addMouseListener(l);
        this.octet2.addMouseListener(l);
        this.octet3.addMouseListener(l);
        this.dot1.addMouseListener(l);
        this.dot2.addMouseListener(l);
        this.dot3.addMouseListener(l);
    }

    public boolean IpKeyListener() {

        for (Octet octet : getOctets()) {
            octet.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    hasChanged = true;
                }
            });
        }
        return hasChanged;
    }

    @Override
    public void setValue(Object value) {
        setText(String.valueOf(value));
    }

    @Override
    public void addValueListener(XValueListener listener) {
        this.listener = listener;
    }

    public class Octet extends VTextField {

        static final String DEFAULT_TEXT = "0";
        static final int COLUMNS = 3;
        static final int MIN = 0;
        static final int MAX = 255;
        boolean isTransferred = false;
        int numberOfDotsPressed = 0;
        public String moveOctetKeys[] = {"LEFT", "RIGHT", "NumPad .", "Period"};

        public Octet() {
            super(COLUMNS);
        }

        public boolean moveToNextOctet(String keyEvent) {
            for (int i = 0; i < moveOctetKeys.length; i++) {
                if (moveOctetKeys[i].toString().equalsIgnoreCase(keyEvent)) {
                    return true;
                }
            }
            return false;
        }

        public void setTransferFocusState(boolean isTransferred) {
            this.isTransferred = isTransferred;
        }

        public boolean getTransferFocusState() {
            return this.isTransferred;
        }

        public void setNumberofDots(int num) {
            if (num == 0) {
                this.numberOfDotsPressed = 0;
            } else {
                this.numberOfDotsPressed = this.numberOfDotsPressed + num;
            }
        }

        public int getNumberofDots() {
            return this.numberOfDotsPressed;
        }

        protected void init() {
            super.init();
            // setText(DEFAULT_TEXT);
            setBorder(null);
            setHorizontalAlignment(SwingConstants.CENTER);
            addKeyListener(new KeyAdapter() {
                private String text;
                private int keyCode = KeyEvent.VK_UNDEFINED;

                @Override
                public void keyTyped(KeyEvent e) {
                    octetKeyTyped(e);
                }

                public void keyPressed(KeyEvent e) {
                    if (e.isControlDown()) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_X:
                                fieldCopy();
                                if (isEnabled() && isEditable()) {
                                    setEmpty();
                                }
                            case KeyEvent.VK_C:
                                fieldCopy();
                            case KeyEvent.VK_V:
                                if (isEnabled() && isEditable()) {
                                    fieldPaste();
                                }
                            default:
                                break;
                        }
                    }
                    this.text = getText();
                    this.keyCode = e.getKeyCode();
                    Strmodifier = KeyEvent.getModifiersExText(e
                            .getModifiersEx());
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        tab = e.getKeyCode();
                    }
                    switch (keyCode) {
                        case KeyEvent.VK_PERIOD:
                        case KeyEvent.VK_DECIMAL:
                        case KeyEvent.VK_RIGHT:
                            if (getSelectedText() != null) {
                                setText(getText());
                            }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    switch (keyCode) {
                        case KeyEvent.VK_BACK_SPACE:
                            if (!text.isEmpty()) {
                                break;
                            }
                        default:
                            break;
                    }
                    text = Octet.this.getText();
                    int maxLength = 3;
                    String pressedKey = e.getKeyText(e.getKeyCode());
                    boolean isMaxLength = text.length() == 3;
                    if (moveToNextOctet(pressedKey) || isMaxLength) {
                        if (pressedKey.equalsIgnoreCase("LEFT")) {
                            if (Octet.this != octet0) {
                                transferFocusBackward();
                                setTransferFocusState(true);
                            }
                        } else {
                            if (Octet.this != octet3) {
                                if (!((pressedKey.equalsIgnoreCase("Period") || pressedKey.equalsIgnoreCase("NumPad .")) && getTransferFocusState())) {
                                    if (!(getTransferFocusState() && isMaxLength)) {
                                        transferFocus();
                                        setTransferFocusState(true);
                                    }
                                } else if (pressedKey.equalsIgnoreCase("Period") || pressedKey.equalsIgnoreCase("NumPad .")) {
                                    setNumberofDots(1);
                                    if (getNumberofDots() == 2) {
                                        transferFocus();
                                        setTransferFocusState(true);
                                        setNumberofDots(0);
                                    }
                                    if (Octet.this == octet0) {
                                        transferFocus();
                                        setTransferFocusState(true);
                                    }
                                }
                            }
                        }
                        doSelectAll();

                    } else {
                        setTransferFocusState(false);
                    }
                }
            });
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (!isEnabled() || !isEditable()) {
                        return;
                    }

                    String text = getText();
                    for (Octet octet : getOctets()) {
                        JPopupMenu popupMenu = octet.getMenuPopup();
                        if (popupMenu != null && popupMenu.isShowing()) {
                            return;
                        }
                    }
                    if (text.isEmpty()) {
                        setText(DEFAULT_TEXT);
                    }
                    for (Octet octet : getOctets()) {
                        octet.setText(removeLeadingZeros(octet.getText()));
                        if (octet.getText().isEmpty()) {
                            octet.setText("0");
                        }
                    }
                }

                public void focusGained(FocusEvent e) {
                    getText();
                    doSelectAll();
                }
            });
        }

        @Override
        protected Document createDefaultModel() {
            String regex = "^[0-9]{0,3}$";
            return new RegexDocument(regex) {
                @Override
                public boolean isValidText(String text) {
                    if (!super.isValidText(text)) {
                        return false;
                    }
                    if (text.isEmpty()) {
                        return true;
                    }
                    int oct = Integer.parseInt(text);
                    return (oct >= MIN && oct <= MAX);
                }
            };
        }
    }

    public boolean isEditableIP() {
        for (Octet octet : getOctets()) {
            if (octet.isEditable()) {
                return octet.isEditable();
            }
        }
        return false;
    }

    public static boolean isValidIP(String ip) {
        if (!(ip.isEmpty() || ip.equalsIgnoreCase("..."))) {
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
        }
        return true;
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
        MVP mvp = AppEngine.prepare(IPAddressField.class);
        mvp.display(JDialog.class, true);
    }
}
