/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package net.wirex;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
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
import net.wirex.annotations.Bind;
import net.wirex.annotations.DELETE;
import net.wirex.annotations.Data;
import net.wirex.annotations.Event;
import net.wirex.annotations.Form;
import net.wirex.annotations.GET;
import net.wirex.annotations.POST;
import net.wirex.annotations.PUT;
import net.wirex.annotations.Path;
import net.wirex.annotations.Retrieve;
import net.wirex.annotations.Type;
import net.wirex.annotations.View;
import net.wirex.enums.Media;
import net.wirex.exceptions.UnknownComponentException;
import net.wirex.exceptions.UnknownListenerException;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.interfaces.Model;
import net.wirex.listeners.JButtonListener;
import net.wirex.listeners.JTextFieldListener;
import net.wirex.structures.XList;

/**
 *
 * @author RBORJA
 */
public class ApplicationControllerFactory {

    static {
        components = new HashMap<>();
        models = new HashMap<>();
    }
    private final static Map<String, JComponent> components;
    private final static Map<Class<? extends Model>, Model> models;
    private static String hostname;

    public static <T> T checkout(String name) {
        return (T) components.remove(name);
    }

    public static <T> T checkout(Class<T> component, String name) {
        if (components.containsKey(name)) {
            return (T) checkout(name);
        } else {
            try {
                return component.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, "No instance for " + component, ex);
                return null;
            }
        }
    }

    public static Model checkout(Class<? extends Model> modelClass) {
        if (modelClass != null) {
            return models.get(modelClass);
        } else {
            return null;
        }
    }

    public static void connect(String url) {
        if (url.endsWith("/")) {
            hostname = url;
        } else {
            hostname = url + "/";
        }
        // TODO: Connect client and return response code and throw exception if not 200
    }

    private ApplicationControllerFactory() {
    }

    public static MVP prepare(Class viewClass) throws ViewClassNotBindedException, WrongComponentException {

        Bind bind = (Bind) viewClass.getAnnotation(Bind.class);
        if (bind == null) {
            throw new ViewClassNotBindedException("Have you annotated @Bind to your " + viewClass.getSimpleName() + " class?");
        }

        Class modelClass = bind.model();
        final Class presenterClass = bind.presenter();
        Field[] fields = viewClass.getDeclaredFields();
        ArrayList<Field> actionFields = new ArrayList<>();
        ArrayList<Field> viewFields = new ArrayList<>();
        Model model = null;
        try {
            model = (Model) modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        models.put(modelClass, model);

        for (Field field : fields) {
            Data data = field.getAnnotation(Data.class);
            if (data != null) {
                if (JTextField.class == field.getType()) {
                    bindComponent(JTextField.class, model, data.value());
                } else if (JLabel.class == field.getType()) {
                    bindComponent(JLabel.class, model, data.value());
                } else if (JCheckBox.class == field.getType()) {
                    bindComponent(JCheckBox.class, model, data.value());
                } else if (JComboBox.class == field.getType()) {
                    bindComponent(JComboBox.class, model, data.value());
                } else if (JTable.class == field.getType()) {
                    bindComponent(JTable.class, model, data.value());
                } else if (JPasswordField.class == field.getType()) {
                    bindComponent(JPasswordField.class, model, data.value());
                } else {
                    throw new WrongComponentException("Component " + field.getType() + " cannot be used for binding the model");
                }
            } else {
                actionFields.add(field);
            }
        }

        final JPanel viewPanel;
        try {
            viewPanel = (JPanel) viewClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        final Object presenter;
        try {
            presenter = presenterClass.getDeclaredConstructor(Model.class, JPanel.class).newInstance(model, viewPanel);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        for (Field field : actionFields) {
            final Event event = field.getAnnotation(Event.class);
            if (event != null) {
                field.setAccessible(true);
                Class component = field.getType();
                Method[] listener;
                try {
                    listener = getArrayMethods(presenter, event.value());
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
                if (ActionListener.class == event.type()) {
                    if (component == JButton.class) {
                        JButtonListener.addActionListener(viewPanel, field, presenter, listener[0]);
                    } else if (component == JTextField.class) {
                        JTextFieldListener.addActionListener(viewPanel, field, presenter, listener[0]);
                    }
                } else if (CaretListener.class == event.type()) {
                } else if (CellEditorListener.class == event.type()) {
                } else if (ChangeListener.class == event.type()) {
                } else if (ComponentListener.class == event.type()) {
                } else if (ContainerListener.class == event.type()) {
                } else if (DocumentListener.class == event.type()) {
                } else if (FocusListener.class == event.type()) {
                } else if (HierarchyBoundsListener.class == event.type()) {
                } else if (HierarchyListener.class == event.type()) {
                } else if (HyperlinkListener.class == event.type()) {
                } else if (InternalFrameListener.class == event.type()) {
                } else if (ItemListener.class == event.type()) {
                } else if (KeyListener.class == event.type()) {
                    if (component == JButton.class) {
                        JButtonListener.addKeyListener(viewPanel, field, presenter, listener);
                    } else if (component == JTextField.class) {
                        JTextFieldListener.addKeyListener(viewPanel, field, presenter, listener);
                    }
                } else if (ListDataListener.class == event.type()) {
                } else if (ListSelectionListener.class == event.type()) {
                } else if (MenuDragMouseListener.class == event.type()) {
                } else if (MenuKeyListener.class == event.type()) {
                } else if (MenuListener.class == event.type()) {
                } else if (MouseInputListener.class == event.type()) {
                } else if (MouseListener.class == event.type()) {
                } else if (MouseMotionListener.class == event.type()) {
                } else if (MouseWheelListener.class == event.type()) {
                } else if (PopupMenuListener.class == event.type()) {
                } else if (PropertyChangeListener.class == event.type()) {
                } else if (TableColumnModelListener.class == event.type()) {
                } else if (TableModelListener.class == event.type()) {
                } else if (TreeExpansionListener.class == event.type()) {
                } else if (TreeModelListener.class == event.type()) {
                } else if (TreeSelectionListener.class == event.type()) {
                } else if (TreeWillExpandListener.class == event.type()) {
                } else if (UndoableEditListener.class == event.type()) {
                } else if (VetoableChangeListener.class == event.type()) {
                } else if (WindowFocusListener.class == event.type()) {
                } else if (WindowListener.class == event.type()) {
                } else if (WindowStateListener.class == event.type()) {
                } else {
                    try {
                        throw new UnknownListenerException(event.type() + " is not a listener class");
                    } catch (UnknownListenerException ex) {
                    }
                }
            } else {
                viewFields.add(field);
            }
        }

        for (Field field : viewFields) {
            final View view = field.getAnnotation(View.class);
            if (view != null) {
                field.setAccessible(true);
                Class subViewClass = field.getType();
                String panelId = view.value();
                MVP mvp = prepare(subViewClass);
                components.put(panelId, mvp.getView());
//                System.out.println("downnnn" + viewPanel.getClass());
                mvp.display(JFrame.class, Boolean.TRUE);

            }
        }

        final Method run;
        try {
            run = presenterClass.getMethod("run", HashMap.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        final Annotation[][] retrieveAnnotations = run.getParameterAnnotations();
        Retrieve retrieve;
        final HashMap<String, Invoker> runMethodParameters = new HashMap<>();
        if (retrieveAnnotations[0].length > 0) {
            retrieve = (Retrieve) retrieveAnnotations[0][0];
            for (String methodName : retrieve.value()) {
                MyActionListener myActionListener = new MyActionListener(presenterClass, presenter, methodName);
                Invoker invokeCode = new Invoker(myActionListener);
                runMethodParameters.put(methodName, invokeCode);
            }
        }



        return new MVP() {
            @Override
            public JPanel getView() {
                return viewPanel;
            }

            @Override
            public void display(final Class<? extends Window> window, final Boolean isVisible) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        Window dialog;
                        if (window == JFrame.class) {
                            dialog = new JFrame();
                            ((JFrame) dialog).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        } else {
                            dialog = new JDialog();
                            ((JDialog) dialog).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        }
                        dialog.add(viewPanel);
                        dialog.pack();
                        dialog.setMinimumSize(dialog.getPreferredSize());
                        Container parent = dialog.getParent();

                        if (parent != null) {
                            dialog.setLocationRelativeTo(parent);
                        } else {
                            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                            int x = (int) ((dimension.getWidth() - dialog.getWidth()) / 2);
                            int y = (int) ((dimension.getHeight() - dialog.getHeight()) / 2);
                            dialog.setLocation(x, y);
                        }
                        dialog.setVisible(isVisible);
                        if (retrieveAnnotations[0].length > 0) {
                            try {
                                run.invoke(presenter, runMethodParameters);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
            }
        };
    }

    private static Method[] getArrayMethods(Object presenter, String[] values) throws NoSuchMethodException {
        Method[] methods = new Method[values.length];
        for (int i = 0; i < values.length; i++) {
            methods[i] = presenter.getClass().getMethod(values[i]);
        }
        return methods;
    }

    public synchronized static void deserialize(Class<? extends Model> modelClass, Model model, Model fromJson) {
        for (Field field : modelClass.getDeclaredFields()) {
            try {

                field.setAccessible(true);

                Class listClass = field.get(fromJson).getClass();
                if (listClass == XList.class) {
                    XList oldList = (XList) field.get(model);
                    XList newList = (XList) field.get(fromJson);

                    oldList.clear();

                    if (Model.class.isAssignableFrom(listClass)) {
                        for (Object e : newList) {
                            oldList.add(new MyObject(e));
                        }
                    } else {
                        for (Object e : newList) {
                            oldList.add(e);
                        }
                    }

//                    field.set(model, oldList);
                } else {
                    Object oldValue = field.get(model) != null ? field.get(model) : "";
                    Object newValue = field.get(fromJson) != null ? field.get(fromJson) : "";
                    field.set(model, field.get(fromJson));
                    modelClass.getSuperclass().getDeclaredMethod("fireChanges", String.class, Object.class, Object.class)
                            .invoke(model, field.getName(), oldValue, newValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized static void injectJersey(Object presenter, Method method) {
        Path path = method.getAnnotation(Path.class);
        Type type = method.getAnnotation(Type.class);
        Form form = method.getAnnotation(Form.class);
        POST post = method.getAnnotation(POST.class);
        GET get = method.getAnnotation(GET.class);
        PUT put = method.getAnnotation(PUT.class);
        DELETE delete = method.getAnnotation(DELETE.class);
        String urlPath;
        if (path != null) {
            if (post != null && form != null) {
                try {
                    if (path.value().startsWith("/")) {
                        urlPath = path.value().substring(1);
                    } else {
                        urlPath = path.value();
                    }
                    Class presenterClass = presenter.getClass();
                    Method initMethod = presenterClass.getSuperclass().getDeclaredMethod("init", String.class, Media.class, String.class);
                    initMethod.setAccessible(true);
                    initMethod.invoke(presenter, hostname + urlPath, type.value(), "post");
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (get != null && form != null) {
                try {
                    if (path.value().startsWith("/")) {
                        urlPath = path.value().substring(1);
                    } else {
                        urlPath = path.value();
                    }
                    Class presenterClass = presenter.getClass();
                    Method initMethod = presenterClass.getSuperclass().getDeclaredMethod("init", String.class, Media.class, String.class);
                    initMethod.setAccessible(true);
                    initMethod.invoke(presenter, hostname + urlPath, type.value(), "get");
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void bindComponent(Class component, Object bean, String property) {
        PresentationModel adapter = new PresentationModel(bean);
        ValueModel componentModel = adapter.getModel(property);
        JComponent newComponent;
        if (JTextField.class == component) {
            newComponent = BasicComponentFactory.createTextField(componentModel);
        } else if (JLabel.class == component) {
            newComponent = BasicComponentFactory.createLabel(componentModel);
        } else if (JCheckBox.class == component) {
            newComponent = BasicComponentFactory.createCheckBox(componentModel, "");
        } else if (JComboBox.class == component) {
            SelectionInList selectionModel = new SelectionInList(componentModel);
            newComponent = BasicComponentFactory.createComboBox(selectionModel);
        } else if (JTable.class == component) {
            try {
                /*
                 * Throw exception if List has no generic types
                 */

                Field listField = bean.getClass().getDeclaredField(property);
                java.lang.reflect.Type type = listField.getGenericType();
                ParameterizedType listType = (ParameterizedType) type;
                Class<?> listTypeClass = (Class<?>) listType.getActualTypeArguments()[0];

                Field[] fields = listTypeClass.getDeclaredFields();
                String[] propertyNames;
                if (Model.class.isAssignableFrom(listTypeClass)) {
                    propertyNames = new String[fields.length];
                    for (int i = 0; i < propertyNames.length; i++) {
                        propertyNames[i] = fields[i].getName();
                    }
                } else {
                    EventList list = (XList) componentModel.getValue();
                    propertyNames = new String[]{"value"};
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, new MyObject(list.get(i)));
                    }
                    adapter.setValue(property, list);
                    listTypeClass = MyObject.class;
                }

                EventList rows = (XList) adapter.getModel(property).getValue();
                TableFormat tf = GlazedLists.tableFormat(listTypeClass, propertyNames, propertyNames);

                JTable table = new JTable(new EventTableModel(rows, tf));

                newComponent = table;
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
                newComponent = null;
            }

        } else if (JPasswordField.class == component) {
            newComponent = BasicComponentFactory.createPasswordField(componentModel);
        } else {
            try {
                throw new UnknownComponentException(component.getName() + " is neither a JComponent nor supported in Wirex.");
            } catch (UnknownComponentException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

            newComponent = null;
        }
        components.put(property, newComponent);
    }

    public static class MyObject {

        private Object value;

        public MyObject(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    public static class MyActionListener implements ActionListener {

        private final String methodName;
        private final Class presenterClass;
        private final Object presenter;

        public MyActionListener(Class presenterClass, Object presenter, String methodName) {
            this.methodName = methodName;
            this.presenterClass = presenterClass;
            this.presenter = presenter;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Method methodInPresenter = presenterClass.getMethod(methodName);
                ApplicationControllerFactory.injectJersey(presenter, methodInPresenter);
                methodInPresenter.invoke(presenter);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ApplicationControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
