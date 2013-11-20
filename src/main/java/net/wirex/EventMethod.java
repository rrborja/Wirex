package net.wirex;

import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.EventListener;
import javax.swing.event.CaretListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.event.UndoableEditListener;

/**
 *
 * @author Ritchie Borja
 */
public enum EventMethod {

    // ActionListener
    ACTION_PERFORMED(ActionListener.class, "actionPerformed"),
    // CaretListener
    CARET_UPDATE(CaretListener.class, "caretUpdate"),
    // CellEditorListener
    EDITING_CANCELED(CellEditorListener.class, "editingCanceled"),
    EDITING_STOPPED(CellEditorListener.class, "editingStopped"),
    // ChangeListener
    STATE_CHANGED(ChangeListener.class, "stateChanged"),
    // ComponentListener
    COMPONENT_HIDDEN(ComponentListener.class, "componentHidden"),
    COMPONENT_MOVED(ComponentListener.class, "componentMoved"),
    COMPONENT_RESIZED(ComponentListener.class, "componentResized"),
    COMPONENT_SHOWN(ComponentListener.class, "componentShown"),
    // ContainerListener
    COMPONENT_ADDED(ContainerListener.class, "componentAdded"),
    COMPONENT_REMOVED(ContainerListener.class, "componentRemoved"),
    // DocumentListener
    CHANGED_UPDATE(DocumentListener.class, "changedUpdate"),
    INSERT_UPDATE(DocumentListener.class, "insertUpdate"),
    REMOVE_UPDATE(DocumentListener.class, "removeUpdate"),
    // FocusListener
    FOCUS_GAINED(FocusListener.class, "focusGained"),
    FOCUS_LOST(FocusListener.class, "focusLost"),
    // HierarchyBoundsListener
    ANCESTOR_MOVED(HierarchyBoundsListener.class, "ancestorMoved"),
    ANCESTOR_RESIZED(HierarchyBoundsListener.class, "ancestorResized"),
    // HierarchyListener
    HIERARCHY_CHANGED(HierarchyListener.class, "hierarchyChanged"),
    // HyperlinkListener
    HYPERLINK_UPDATE(HyperlinkListener.class, "hyperlinkUpdate"),
    // InputMethodListener
    CARET_POSITION_CHANGED(InputMethodListener.class, "caretPositionChanged"),
    INPUT_METHOD_TEXT_CHANGED(InputMethodListener.class, "inputMethodTextChanged"),
    // InternalFrameListener
    INTERNAL_FRAME_ACTIVATE(InternalFrameListener.class, "internalFrameActivated"),
    INTERNAL_FRAME_CLOSED(InternalFrameListener.class, "internalFrameClosed"),
    INTERNAL_FRAME_CLOSING(InternalFrameListener.class, "internalFrameClosing"),
    INTERNAL_FRAME_DEACTIVATED(InternalFrameListener.class, "internalFrameDeactivated"),
    INTERNAL_FRAME_DEICONIFIED(InternalFrameListener.class, "internalFrameDeiconified"),
    INTERNAL_FRAME_ICONIFIED(InternalFrameListener.class, "internalFrameIconified"),
    INTERNAL_FRAME_OPENED(InternalFrameListener.class, "internalFrameOpened"),
    // ItemListener
    ITEM_STATE_CHANGED(ItemListener.class, "itemStateChanged"),
    // KeyListener
    KEY_PRESSED(KeyListener.class, "keyPressed"),
    KEY_RELEASED(KeyListener.class, "keyReleased"),
    KEY_TYPED(KeyListener.class, "keyTyped"),
    // ListDataListener
    CONTENTS_CHANGED(ListDataListener.class, "contentsChanged"),
    INTERVAL_ADDED(ListDataListener.class, "intervalAdded"),
    INTERVAL_REMOVED(ListDataListener.class, "intervalRemoved"),
    // ListSelectionListener
    VALUE_CHANGED_LIST(ListSelectionListener.class, "valueChanged"),
    // MenuDragMouseListener
    MENU_DRAG_MOUSE_DRAGGED(MenuDragMouseListener.class, "menuDragMouseDragged"),
    MENU_DRAG_MOUSE_ENTERED(MenuDragMouseListener.class, "menuDragMouseEntered"),
    MENU_DRAG_MOUSE_EXITED(MenuDragMouseListener.class, "menuDragMouseExited"),
    MENU_DRAG_MOUSE_RELEASED(MenuDragMouseListener.class, "menuDragMouseReleased"),
    // MenuKeyListener
    MENU_KEY_PRESSED(MenuKeyListener.class, "menuKeyPressed"),
    MENU_KEY_RELEASED(MenuKeyListener.class, "menuKeyReleased"),
    MENU_KEY_TYPED(MenuKeyListener.class, "menuKeyTyped"),
    // MenuListener
    MENU_CANCELED(MenuListener.class, "menuCanceled"),
    MENU_DESELECTED(MenuListener.class, "menuDeselected"),
    MENU_SELECTED(MenuListener.class, "menuSelected"),
    // MouseInputListener  
    MOUSE_INPUT_CLICKED(MouseInputListener.class, "mouseClicked"),
    MOUSE_INPUT_ENTERED(MouseInputListener.class, "mouseEntered"),
    MOUSE_INPUT_EXITED(MouseInputListener.class, "mouseExited"),
    MOUSE_INPUT_DRAGGED(MouseInputListener.class, "mouseDragged"),
    MOUSE_INPUT_MOVED(MouseInputListener.class, "mouseMoved"),
    MOUSE_INPUT_PRESSED(MouseInputListener.class, "mousePressed"),
    MOUSE_INPUT_RELEASED(MouseInputListener.class, "mouseReleased"),
    // MouseListener
    MOUSE_CLICKED(MouseListener.class, "mouseClicked"),
    MOUSE_ENTERED(MouseListener.class, "mouseEntered"),
    MOUSE_EXITED(MouseListener.class, "mouseExited"),
    MOUSE_DRAGGED(MouseListener.class, "mouseDragged"),
    MOUSE_MOVED(MouseListener.class, "mouseMoved"),
    MOUSE_PRESSED(MouseListener.class, "mousePressed"),
    MOUSE_RELEASED(MouseListener.class, "mouseReleased"),
    // MouseMotionListener
    MOUSE_MOTION_CLICKED(MouseMotionListener.class, "mouseClicked"),
    MOUSE_MOTION_ENTERED(MouseMotionListener.class, "mouseEntered"),
    MOUSE_MOTION_EXITED(MouseMotionListener.class, "mouseExited"),
    MOUSE_MOTION_DRAGGED(MouseMotionListener.class, "mouseDragged"),
    MOUSE_MOTION_MOVED(MouseMotionListener.class, "mouseMoved"),
    MOUSE_MOTION_PRESSED(MouseMotionListener.class, "mousePressed"),
    MOUSE_MOTION_RELEASED(MouseMotionListener.class, "mouseReleased"),
    // MouseWheelListener
    MOUSE_WHEEL_MOVED(MouseWheelListener.class, "mouseWheelMoved"),
    // PopupMenuListener
    POPUP_MENU_CANCELED(PopupMenuListener.class, "popupMenuCanceled"),
    POPUP_MENU_WILL_BECOME_INVISIBLE(PopupMenuListener.class, "popupMenuWillBecomeInvisible"),
    POPUP_MENU_WILL_BECOME_VISIBLE(PopupMenuListener.class, "popupMenuWillBecomeVisible"),
    // PropertyChangeListener
    PROPERTY_CHANGE(PropertyChangeListener.class, "propertyChange"),
    // TableColumnModelListener
    COLUMN_ADDED(TableColumnModelListener.class, "columnAdded"),
    COLUMN_MARGIN_CHANGED(TableColumnModelListener.class, "columnMarginChanged"),
    COLUMN_MOVED(TableColumnModelListener.class, "columnMoved"),
    COLUMN_REMOVED(TableColumnModelListener.class, "columnRemoved"),
    COLUMN_SELECTION_CHANGED(TableColumnModelListener.class, "columnSelectionChanged"),
    // TableModelListener
    TABLE_CHANGED(TableModelListener.class, "tableChanged"),
    // TreeExpansionListener
    TREE_COLLAPSED(TreeExpansionListener.class, "treeCollapsed"),
    TREE_EXPANDED(TreeExpansionListener.class, "treeExpanded"),
    // TreeModelListener
    TREE_NODES_CHANGED(TreeModelListener.class, "treeNodesChanged"),
    TREE_NODES_INSERTED(TreeModelListener.class, "treeNodesInserted"),
    TREE_NODES_REMOVED(TreeModelListener.class, "treeNodesRemoved"),
    TREE_STRUCTURE_CHANGED(TreeModelListener.class, "treeStructureChanged"),
    // TreeSelectionListener
    VALUE_CHANGED_TREE(TreeSelectionListener.class, "valueChanged"),
    // TreeWillExpandListener
    TREE_WILL_COLLAPSE(TreeWillExpandListener.class, "treeWillCollapse"),
    TREE_WILL_EXPAND(TreeWillExpandListener.class, "treeWillExpand"),
    // UndoableEditListener
    UNDOABLE_EDIT_HAPPENED(UndoableEditListener.class, "undoableEditHappened"),
    // VetoableChangeListener
    VETOABLE_CHANGED(VetoableChangeListener.class, "vetoableChange"),
    // WindowFocusListener
    WINDOW_GAINED_FOCUS(WindowFocusListener.class, "windowGainedFocus"),
    WINDOW_LOST_FOCUS(WindowFocusListener.class, "windowLostFocus"),
    // WindowListener
    WINDOW_ACTIVATED(WindowListener.class, "windowActivated"),
    WINDOW_CLOSED(WindowListener.class, "windowClosed"),
    WINDOW_CLOSING(WindowListener.class, "windowClosing"),
    WINDOW_DEACTIVATED(WindowListener.class, "windowDeactivated"),
    WINDOW_DEICONIFIED(WindowListener.class, "windowDeiconified"),
    WINDOW_ICONIFIED(WindowListener.class, "windowIconified"),
    WINDOW_OPENED(WindowListener.class, "windowOpened"),
    // WindowStateListener
    WINDOW_STATE_CHANGED(WindowStateListener.class, "windowStateChanged");

    EventMethod(Class<? extends EventListener> listener, String method) {
        this.listener = listener;
        this.method = method;
    }

    private final Class<? extends EventListener> listener;
    private final String method;

    protected Class<?> getListener() {
        return listener;
    }
    
    protected String getMethod() {
        return method;
    }
}
