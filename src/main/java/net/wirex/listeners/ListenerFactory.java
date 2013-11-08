/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.ExpandVetoException;
import net.wirex.AppEngine;
import net.wirex.exceptions.EventInterruptionException;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;

/**
 *
 * @author RBORJA
 */
abstract class ListenerFactory {

    public static ActionListener actionListener(final Object presenter, final Method listener) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static CaretListener caretListener(final Object presenter, final Method listener) {
        return new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static CellEditorListener cellEditorListener(final Object presenter, final Method... listener) {
        return new CellEditorListener() {
            @Override
            public void editingCanceled(ChangeEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void editingStopped(ChangeEvent e) {
                invoke(listener[1], presenter, e);
            }
        };
    }

    public static ChangeListener changeListener(final Object presenter, final Method listener) {
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static ComponentListener componentListener(final Object presenter, final Method... listener) {
        return new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                invoke(listener[3], presenter, e);
            }
        };
    }

    public static ContainerListener containerListener(final Object presenter, final Method... listener) {
        return new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                invoke(listener[1], presenter, e);
            }
        };
    }

    public static DocumentListener documentListener(final Object presenter, final Method... listener) {
        return new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                invoke(listener[2], presenter, e);
            }
        };
    }

    public static FocusListener focusListener(final Object presenter, final Method... listener) {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                invoke(listener[1], presenter, e);
            }
        };
    }

    public static HierarchyBoundsListener hierarchyBoundsListener(final Object presenter, final Method... listener) {
        return new HierarchyBoundsListener() {
            @Override
            public void ancestorMoved(HierarchyEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void ancestorResized(HierarchyEvent e) {
                invoke(listener[1], presenter, e);
            }
        };
    }

    public static HierarchyListener hierarchyListener(final Object presenter, final Method listener) {
        return new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static HyperlinkListener hyperlinkListener(final Object presenter, final Method listener) {
        return new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static InputMethodListener inputMethodListener(final Object presenter, final Method... listener) {
        return new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent event) {
                invoke(listener[0], presenter, event);
            }

            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                invoke(listener[1], presenter, event);
            }
        };
    }

    public static InternalFrameListener internalFrameListener(final Object presenter, final Method... listener) {
        return new InternalFrameListener() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                invoke(listener[3], presenter, e);
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                invoke(listener[4], presenter, e);
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                invoke(listener[5], presenter, e);
            }

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                invoke(listener[6], presenter, e);
            }
        };
    }

    public static ItemListener itemListener(final Object presenter, final Method listener) {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static KeyListener keyListener(final Object presenter, final Method... listener) {
        return new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                invoke(listener[2], presenter, e);
            }
        };
    }

    public static ListDataListener listDataListener(final Object presenter, final Method... listener) {
        return new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoke(listener[2], presenter, e);
            }
        };
    }

    public static ListSelectionListener listSelectionListener(final Object presenter, final Method listener) {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static MenuDragMouseListener menuDragMouseListener(final Object presenter, final Method... listener) {
        return new MenuDragMouseListener() {
            @Override
            public void menuDragMouseDragged(MenuDragMouseEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void menuDragMouseEntered(MenuDragMouseEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void menuDragMouseExited(MenuDragMouseEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void menuDragMouseReleased(MenuDragMouseEvent e) {
                invoke(listener[3], presenter, e);
            }
        };
    }

    public static MenuKeyListener menuKeyListener(final Object presenter, final Method... listener) {
        return new MenuKeyListener() {
            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
                invoke(listener[2], presenter, e);
            }
        };
    }

    public static MenuListener menuListener(final Object presenter, final Method... listener) {
        return new MenuListener() {
            @Override
            public void menuCanceled(MenuEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void menuSelected(MenuEvent e) {
                invoke(listener[2], presenter, e);
            }
        };
    }

    public static MouseInputListener mouseInputListener(final Object presenter, final Method... listener) {
        return new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                invoke(listener[3], presenter, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                invoke(listener[4], presenter, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                invoke(listener[5], presenter, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                invoke(listener[6], presenter, e);
            }
        };
    }

    public static MouseListener mouseListener(final Object presenter, final Method... listener) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                invoke(listener[3], presenter, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                invoke(listener[4], presenter, e);
            }
        };
    }

    public static MouseMotionListener mouseMotionListener(final Object presenter, final Method... listener) {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                invoke(listener[1], presenter, e);
            }
        };
    }

    public static MouseWheelListener mouseWheelListener(final Object presenter, final Method listener) {
        return new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static PopupMenuListener popupMenuListener(final Object presenter, final Method... listener) {
        return new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                invoke(listener[2], presenter, e);
            }
        };
    }

    public static PropertyChangeListener propertyChangeListener(final Object presenter, final Method listener) {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                invoke(listener, presenter, evt);
            }
        };
    }

    public static TableColumnModelListener tableColumnModelListener(final Object presenter, final Method... listener) {
        return new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
                invoke(listener[3], presenter, e);
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
                invoke(listener[4], presenter, e);
            }
        };
    }

    public static TableModelListener tableModelListener(final Object presenter, final Method listener) {
        return new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static TreeExpansionListener treeExpansionListener(final Object presenter, final Method... listener) {
        return new TreeExpansionListener() {
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                invoke(listener[0], presenter, event);
            }

            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                invoke(listener[1], presenter, event);
            }
        };
    }

    public static TreeModelListener treeModelListener(final Object presenter, final Method... listener) {
        return new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                invoke(listener[3], presenter, e);
            }
        };
    }

    public static TreeSelectionListener treeSelectionListener(final Object presenter, final Method listener) {
        return new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static TreeWillExpandListener treeWillExpandListener(final Object presenter, final Method... listener) {
        return new TreeWillExpandListener() {
            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                invoke(listener[0], presenter, event);
            }

            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                invoke(listener[1], presenter, event);
            }
        };
    }

    public static UndoableEditListener undoableEditListener(final Object presenter, final Method listener) {
        return new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    public static VetoableChangeListener vetoableChangeListener(final Object presenter, final Method listener) {
        return new VetoableChangeListener() {
            @Override
            public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                invoke(listener, presenter, evt);
            }
        };
    }

    public static WindowFocusListener windowFocusListener(final Object presenter, final Method... listener) {
        return new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                invoke(listener[1], presenter, e);
            }
        };
    }

    public static WindowListener windowListener(final Object presenter, final Method... listener) {
        return new WindowListener() {
            @Override
            public void windowActivated(WindowEvent e) {
                invoke(listener[0], presenter, e);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                invoke(listener[1], presenter, e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                invoke(listener[2], presenter, e);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                invoke(listener[3], presenter, e);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                invoke(listener[4], presenter, e);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                invoke(listener[5], presenter, e);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                invoke(listener[6], presenter, e);
            }
        };
    }

    public static WindowStateListener windowStateListener(final Object presenter, final Method listener) {
        return new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                invoke(listener, presenter, e);
            }
        };
    }

    private static void invoke(Method listener, Object presenter, Object e) {
        AppEngine.injectRestSpec(presenter, listener);
        try {
            if (listener.getParameterTypes().length > 0) {
                listener.invoke(presenter, e);
            } else {
                listener.invoke(presenter);
            }
            AppEngine.proceed(presenter, listener);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ViewClassNotBindedException | WrongComponentException ex) {
            if (ex.getCause() instanceof EventInterruptionException) {
                Logger.getLogger(ListenerFactory.class.getName()).log(Level.INFO, ex.getMessage());
            } else {
                Logger.getLogger(ListenerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
