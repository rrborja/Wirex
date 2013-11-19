/*
 * The MIT License
 *
 * Copyright 2013 Ritchie Borja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author Ritchie Borja
 */
public abstract class ListenerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ListenerFactory.class.getName());

    private static void invoke(Method listener, Object presenter, Object e) {
        if (listener == null) {
            LOG.warn("No binding for this triggered presenter method");
            return;
        }
        AppEngine.injectRestSpec(presenter, listener);
        try {
            if (listener.getParameterTypes().length > 0) {
                listener.invoke(presenter, e);
            } else {
                listener.invoke(presenter);
            }
            AppEngine.proceed(presenter, listener);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ViewClassNotBindedException | WrongComponentException ex) {
            if (ex instanceof EventInterruptionException) {
                LOG.info("The method " + listener + " throws an error: " + ex.getMessage(), ex);
            } else {
                LOG.error("Unable to invoke {}", listener);
            }
        }
    }

    public static ActionListener ActionListener(final Object presenter, final Map<String, Method> listener) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invoke(listener.get("actionPerformed"), presenter, e);
            }
        };
    }

    public static CaretListener CaretListener(final Object presenter, final Map<String, Method> listener) {
        return new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                invoke(listener.get("caretUpdate"), presenter, e);
            }
        };
    }

    public static CellEditorListener CellEditorListener(final Object presenter, final Map<String, Method> listener) {
        return new CellEditorListener() {
            @Override
            public void editingCanceled(ChangeEvent e) {
                invoke(listener.get("editingCanceled"), presenter, e);
            }

            @Override
            public void editingStopped(ChangeEvent e) {
                invoke(listener.get("editingStopped"), presenter, e);
            }
        };
    }

    public static ChangeListener ChangeListener(final Object presenter, final Map<String, Method> listener) {
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                invoke(listener.get("stateChanged"), presenter, e);
            }
        };
    }

    public static ComponentListener ComponentListener(final Object presenter, final Map<String, Method> listener) {
        return new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                invoke(listener.get("componentHidden"), presenter, e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                invoke(listener.get("componentMoved"), presenter, e);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                invoke(listener.get("componentResized"), presenter, e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                invoke(listener.get("componentShown"), presenter, e);
            }
        };
    }

    public static ContainerListener ContainerListener(final Object presenter, final Map<String, Method> listener) {
        return new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                invoke(listener.get("componentAdded"), presenter, e);
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                invoke(listener.get("componentRemoved"), presenter, e);
            }
        };
    }

    public static DocumentListener DocumentListener(final Object presenter, final Map<String, Method> listener) {
        return new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                invoke(listener.get("changedUpdate"), presenter, e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                invoke(listener.get("insertUpdate"), presenter, e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                invoke(listener.get("removeUpdate"), presenter, e);
            }
        };
    }

    public static FocusListener FocusListener(final Object presenter, final Map<String, Method> listener) {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                invoke(listener.get("focusGained"), presenter, e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                invoke(listener.get("focusLost"), presenter, e);
            }
        };
    }

    public static HierarchyBoundsListener HierarchyBoundsListener(final Object presenter, final Map<String, Method> listener) {
        return new HierarchyBoundsListener() {
            @Override
            public void ancestorMoved(HierarchyEvent e) {
                invoke(listener.get("ancestorMoved"), presenter, e);
            }

            @Override
            public void ancestorResized(HierarchyEvent e) {
                invoke(listener.get("ancestorResized"), presenter, e);
            }
        };
    }

    public static HierarchyListener HierarchyListener(final Object presenter, final Map<String, Method> listener) {
        return new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                invoke(listener.get("hierarchyChanged"), presenter, e);
            }
        };
    }

    public static HyperlinkListener HyperlinkListener(final Object presenter, final Map<String, Method> listener) {
        return new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                invoke(listener.get("hyperlinkUpdate"), presenter, e);
            }
        };
    }

    public static InputMethodListener InputMethodListener(final Object presenter, final Map<String, Method> listener) {
        return new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent event) {
                invoke(listener.get("caretPositionChanged"), presenter, event);
            }

            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                invoke(listener.get("inputMethodTextChanged"), presenter, event);
            }
        };
    }

    public static InternalFrameListener InternalFrameListener(final Object presenter, final Map<String, Method> listener) {
        return new InternalFrameListener() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                invoke(listener.get("internalFrameActivated"), presenter, e);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                invoke(listener.get("internalFrameClosed"), presenter, e);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                invoke(listener.get("internalFrameClosing"), presenter, e);
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                invoke(listener.get("internalFrameDeactivated"), presenter, e);
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                invoke(listener.get("internalFrameDeiconified"), presenter, e);
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                invoke(listener.get("internalFrameIconified"), presenter, e);
            }

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                invoke(listener.get("internalFrameOpened"), presenter, e);
            }
        };
    }

    public static ItemListener ItemListener(final Object presenter, final Map<String, Method> listener) {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                invoke(listener.get("itemStateChanged"), presenter, e);
            }
        };
    }

    public static KeyListener KeyListener(final Object presenter, final Map<String, Method> listener) {
        return new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                invoke(listener.get("keyPressed"), presenter, e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                invoke(listener.get("keyReleased"), presenter, e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                invoke(listener.get("keyTyped"), presenter, e);
            }
        };
    }

    public static ListDataListener ListDataListener(final Object presenter, final Map<String, Method> listener) {
        return new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                invoke(listener.get("contentsChanged"), presenter, e);
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                invoke(listener.get("intervalAdded"), presenter, e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                invoke(listener.get("intervalRemoved"), presenter, e);
            }
        };
    }

    public static ListSelectionListener ListSelectionListener(final Object presenter, final Map<String, Method> listener) {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                invoke(listener.get("valueChanged"), presenter, e);
            }
        };
    }

    public static MenuDragMouseListener MenuDragMouseListener(final Object presenter, final Map<String, Method> listener) {
        return new MenuDragMouseListener() {
            @Override
            public void menuDragMouseDragged(MenuDragMouseEvent e) {
                invoke(listener.get("menuDragMouseDragged"), presenter, e);
            }

            @Override
            public void menuDragMouseEntered(MenuDragMouseEvent e) {
                invoke(listener.get("menuDragMouseEntered"), presenter, e);
            }

            @Override
            public void menuDragMouseExited(MenuDragMouseEvent e) {
                invoke(listener.get("menuDragMouseExited"), presenter, e);
            }

            @Override
            public void menuDragMouseReleased(MenuDragMouseEvent e) {
                invoke(listener.get("menuDragMouseReleased"), presenter, e);
            }
        };
    }

    public static MenuKeyListener MenuKeyListener(final Object presenter, final Map<String, Method> listener) {
        return new MenuKeyListener() {
            @Override
            public void menuKeyPressed(MenuKeyEvent e) {
                invoke(listener.get("menuKeyPressed"), presenter, e);
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e) {
                invoke(listener.get("menuKeyReleased"), presenter, e);
            }

            @Override
            public void menuKeyTyped(MenuKeyEvent e) {
                invoke(listener.get("menuKeyTyped"), presenter, e);
            }
        };
    }

    public static MenuListener MenuListener(final Object presenter, final Map<String, Method> listener) {
        return new MenuListener() {
            @Override
            public void menuCanceled(MenuEvent e) {
                invoke(listener.get("menuCanceled"), presenter, e);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                invoke(listener.get("menuDeselected"), presenter, e);
            }

            @Override
            public void menuSelected(MenuEvent e) {
                invoke(listener.get("menuSelected"), presenter, e);
            }
        };
    }

    public static MouseInputListener MouseInputListener(final Object presenter, final Map<String, Method> listener) {
        return new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                invoke(listener.get("mouseClicked"), presenter, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                invoke(listener.get("mouseEntered"), presenter, e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invoke(listener.get("mouseExited"), presenter, e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                invoke(listener.get("mouseDragged"), presenter, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                invoke(listener.get("mouseMoved"), presenter, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                invoke(listener.get("mousePressed"), presenter, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                invoke(listener.get("mouseReleased"), presenter, e);
            }
        };
    }

    public static MouseListener MouseListener(final Object presenter, final Map<String, Method> listener) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                invoke(listener.get("mouseClicked"), presenter, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                invoke(listener.get("mouseEntered"), presenter, e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invoke(listener.get("mouseExited"), presenter, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                invoke(listener.get("mousePressed"), presenter, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                invoke(listener.get("mouseReleased"), presenter, e);
            }
        };
    }

    public static MouseMotionListener MouseMotionListener(final Object presenter, final Map<String, Method> listener) {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                invoke(listener.get("mouseDragged"), presenter, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                invoke(listener.get("mouseMoved"), presenter, e);
            }
        };
    }

    public static MouseWheelListener MouseWheelListener(final Object presenter, final Map<String, Method> listener) {
        return new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                invoke(listener.get("mouseWheelMoved"), presenter, e);
            }
        };
    }

    public static PopupMenuListener PopupMenuListener(final Object presenter, final Map<String, Method> listener) {
        return new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                invoke(listener.get("popupMenuCanceled"), presenter, e);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                invoke(listener.get("popupMenuWillBecomeInvisible"), presenter, e);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                invoke(listener.get("popupMenuWillBecomeVisible"), presenter, e);
            }
        };
    }

    public static PropertyChangeListener PropertyChangeListener(final Object presenter, final Map<String, Method> listener) {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                invoke(listener.get("propertyChange"), presenter, evt);
            }
        };
    }

    public static TableColumnModelListener TableColumnModelListener(final Object presenter, final Map<String, Method> listener) {
        return new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
                invoke(listener.get("columnAdded"), presenter, e);
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                invoke(listener.get("columnMarginChanged"), presenter, e);
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                invoke(listener.get("columnMoved"), presenter, e);
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
                invoke(listener.get("columnRemoved"), presenter, e);
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
                invoke(listener.get("columnSelectionChanged"), presenter, e);
            }
        };
    }

    public static TableModelListener TableModelListener(final Object presenter, final Map<String, Method> listener) {
        return new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                invoke(listener.get("tableChanged"), presenter, e);
            }
        };
    }

    public static TreeExpansionListener TreeExpansionListener(final Object presenter, final Map<String, Method> listener) {
        return new TreeExpansionListener() {
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                invoke(listener.get("treeCollapsed"), presenter, event);
            }

            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                invoke(listener.get("treeExpanded"), presenter, event);
            }
        };
    }

    public static TreeModelListener TreeModelListener(final Object presenter, final Map<String, Method> listener) {
        return new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                invoke(listener.get("treeNodesChanged"), presenter, e);
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                invoke(listener.get("treeNodesInserted"), presenter, e);
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
                invoke(listener.get("treeNodesRemoved"), presenter, e);
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                invoke(listener.get("treeStructureChanged"), presenter, e);
            }
        };
    }

    public static TreeSelectionListener TreeSelectionListener(final Object presenter, final Map<String, Method> listener) {
        return new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                invoke(listener.get("valueChanged"), presenter, e);
            }
        };
    }

    public static TreeWillExpandListener TreeWillExpandListener(final Object presenter, final Map<String, Method> listener) {
        return new TreeWillExpandListener() {
            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                invoke(listener.get("treeWillCollapse"), presenter, event);
            }

            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                invoke(listener.get("treeWillExpand"), presenter, event);
            }
        };
    }

    public static UndoableEditListener UndoableEditListener(final Object presenter, final Map<String, Method> listener) {
        return new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                invoke(listener.get("undoableEditHappened"), presenter, e);
            }
        };
    }

    public static VetoableChangeListener VetoableChangeListener(final Object presenter, final Map<String, Method> listener) {
        return new VetoableChangeListener() {
            @Override
            public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                invoke(listener.get("vetoableChange"), presenter, evt);
            }
        };
    }

    public static WindowFocusListener WindowFocusListener(final Object presenter, final Map<String, Method> listener) {
        return new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                invoke(listener.get("windowGainedFocus"), presenter, e);
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                invoke(listener.get("windowLostFocus"), presenter, e);
            }
        };
    }

    public static WindowListener WindowListener(final Object presenter, final Map<String, Method> listener) {
        return new WindowListener() {
            @Override
            public void windowActivated(WindowEvent e) {
                invoke(listener.get("windowActivated"), presenter, e);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                invoke(listener.get("windowClosed"), presenter, e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                invoke(listener.get("windowClosing"), presenter, e);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                invoke(listener.get("windowDeactivated"), presenter, e);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                invoke(listener.get("windowDeiconified"), presenter, e);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                invoke(listener.get("windowIconified"), presenter, e);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                invoke(listener.get("windowOpened"), presenter, e);
            }
        };
    }

    public static WindowStateListener WindowStateListener(final Object presenter, final Map<String, Method> listener) {
        return new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                invoke(listener.get("windowStateChanged"), presenter, e);
            }
        };
    }

}
