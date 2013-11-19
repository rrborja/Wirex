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

package net.wirex;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.EventTreeModel;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import net.wirex.annotations.Access;
import net.wirex.annotations.Bind;
import net.wirex.annotations.DELETE;
import net.wirex.annotations.Data;
import net.wirex.annotations.Dispose;
import net.wirex.annotations.Draw;
import net.wirex.annotations.Event;
import net.wirex.annotations.EventContainer;
import net.wirex.annotations.Fire;
import net.wirex.annotations.Form;
import net.wirex.annotations.GET;
import net.wirex.annotations.POST;
import net.wirex.annotations.PUT;
import net.wirex.annotations.Path;
import net.wirex.annotations.Retrieve;
import net.wirex.annotations.Snip;
import net.wirex.annotations.Type;
import net.wirex.annotations.View;
import net.wirex.enums.Media;
import net.wirex.exceptions.InvalidKeywordFromBindingNameException;
import net.wirex.exceptions.ReservedKeywordFromBindingNameException;
import net.wirex.exceptions.UnknownComponentException;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.listeners.ListenerFactory;
import net.wirex.structures.XList;
import net.wirex.structures.XTreeFormat;

/**
 *
 * @author Ritchie Borja
 */
public class AppEngine {

    private static final Logger LOG = LoggerFactory.getLogger(AppEngine.class.getSimpleName());

    static {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        LOG.info("Wirex Framework {}", dateFormat.format(date));
    }

    private static final LoadingCache<ServerRequest, ServerResponse> cacheResource = CacheBuilder.newBuilder()
            .maximumSize(1)
            .concurrencyLevel(10)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new ServerResponseCacheLoader());

    private final static ConcurrentMap<String, JComponent> components = new ConcurrentHashMap();

    private final static LoadingCache<Class<? extends Model>, Model> modelCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Class<? extends Model>, Model>() {
                        public @Override
                        Model load(Class<? extends Model> name) {
                            return models.get(name);
                        }
                    });

    private final static ConcurrentMap<Class<? extends Model>, Model> models = new ConcurrentHashMap();

    private final static LoadingCache<String, ImageIcon> iconResource = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ImageIcon>() {
                @Override
                public ImageIcon load(String iconName) throws Exception {
                    String name = resourceHostname + iconName;
                    URL url = new URL(name);
                    Image resource = ImageIO.read(url);
                    if (resource == null) {
                        LOG.warn("Missing icon at {}", name);
                        return new ImageIcon();
                    }
                    return new ImageIcon(resource);
                }
            });

    private static int totalPreparedViews = 0;
    private static String hostname = "http://10.0.1.46:8080/";

    private static String resourceHostname = "http://10.0.1.46/~rborja/icons/";

    /**
     * Checkouts a binded component.
     *
     * @param <T> The type must be a JComponent class or its subclass
     * @param name The name of the component binded from the annotated fields
     * @return Returns a model-binded component
     * @deprecated Use checkout(Class component, String name) instead
     */
    @Deprecated
    public static <T> T checkout(String name) {
        return (T) components.remove(name);
    }

    /**
     * Checkouts a prepared component binded from @Data
     *
     * @param <T> The type must be a JComponent class or its subclass
     * @param component Your JComponent class
     * @param name The name of the component binded from the annotated fields
     * @return Returns a model-binded component
     */
    public static <T> T checkout(Class<T> component, String name) {
        if (components.containsKey(name)) {
            return (T) checkout(name);
        } else {
            try {
                return component.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.error("No instance for " + component, ex);
                return null;
            }
        }
    }

    private static Model checkout(Class<? extends Model> modelClass) {
        if (modelClass != null) {
            return models.get(modelClass);
        } else {
            return null;
        }
    }

    /**
     * Connects a Java EE web server that supports REST transactions
     *
     * @param url The url to connect to a server that processes REST
     * transactions
     */
    public static void connect(String url) {
        if (url.endsWith("/")) {
            hostname = url;
        } else {
            hostname = url + "/";
        }
        // TODO: Connect client and return response code and throw exception if not 200
    }

    /**
     * Locates a suite of icons from a web server for the ResourceBundle feature
     *
     * @param url The URL to retrieve the icons
     */
    public static void locateResource(String url) {
        try {
            URI uri = new URI(url);
            resourceHostname = uri.toString();
        } catch (URISyntaxException ex) {
            LOG.warn("Check url syntax: {}", url);
        }
    }

    /**
     * Connects to a server based on the request headers in the ServerRequest
     * object. It should contain all the required properties such as REST
     * method, Media Type, the Path mapped by Spring MVC, etc.
     *
     * @param request An object containing the ServerRequest properties like
     * REST, Media Type, etc.
     * @return A response from the server
     */
    public static ServerResponse push(ServerRequest request) {
        try {
            return cacheResource.get(request);
        } catch (ExecutionException ex) {
            LOG.error(request.getBody(), ex);
            return null;
        }
    }

    private AppEngine() {
    }

    /**
     * Prepares a binded view class together with its Presenter and Model
     * classes and binds them together to form an MVP object that contains
     * method to display the view.
     *
     * @param viewClass The Swing-based GUI JPanel class
     * @return Returns an MVP object
     * @throws ViewClassNotBindedException Throws an exception when a View class
     * doesn't have a binding annotation
     * @throws WrongComponentException Throws this exception when a field
     * declaration is not of type JComponent
     */
    public static MVP prepare(Class viewClass) throws ViewClassNotBindedException, WrongComponentException {

        Bind bind = (Bind) viewClass.getAnnotation(Bind.class);
        if (bind == null) {
            throw new ViewClassNotBindedException("Have you annotated @Bind to your " + viewClass.getSimpleName() + " class?");
        }

        Class modelClass = bind.model();
        final Class presenterClass = bind.presenter();
        Field[] fields = viewClass.getDeclaredFields();
        ArrayList<Field> drawFields = new ArrayList<>();
        ArrayList<Field> actionFields = new ArrayList<>();
        ArrayList<Field> viewFields = new ArrayList<>();
        Model model = null;
        try {
            if (models.containsKey(modelClass)) {
                model = modelCache.get(modelClass);
            } else {
                model = (Model) modelClass.newInstance();
                models.put(modelClass, model);
            }
        } catch (InstantiationException | IllegalAccessException | ExecutionException ex) {
            LOG.error("Unable to load model", ex);
        }
        
        scanFieldsWithData(fields, model, viewFields);

        scanFieldsWithView(viewFields, actionFields);

        final JPanel viewPanel;
        try {
            viewPanel = (JPanel) viewClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Unable to create " + viewClass, ex);
            return null;
        }

        final Object presenter;
        try {
            presenter = presenterClass.getDeclaredConstructor(Model.class, JPanel.class).newInstance(model, viewPanel);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.error("Unable to create " + presenterClass, ex);
            return null;
        }

        scanFieldsWithEvent(actionFields, viewPanel, presenterClass, presenter, drawFields);

        final Method run;
        try {
            run = presenterClass.getMethod("run", ConcurrentHashMap.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            LOG.error("Framework bug! Can't access run method.", ex);
            return null;
        }
        final Annotation[][] retrieveAnnotations = run.getParameterAnnotations();
        Retrieve retrieve;
        final ConcurrentHashMap<String, Invoker> runMethodParameters = new ConcurrentHashMap<>();
        if (retrieveAnnotations[0].length > 0) {
            retrieve = (Retrieve) retrieveAnnotations[0][0];
            for (String methodName : retrieve.value()) {
                MyActionListener myActionListener = new MyActionListener(presenterClass, presenter, methodName);
                Invoker invokeCode = new Invoker(myActionListener);
                runMethodParameters.put(methodName, invokeCode);
            }
        }

        if (retrieveAnnotations[0].length > 0) {
            try {
                run.invoke(presenter, runMethodParameters);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.error("Framework bug! Cannot invoke run in " + presenter.getClass(), ex);
            }
        }
        
        scanFieldsWithAccess(presenterClass, presenter);

        scanFieldsWithDraw(drawFields, viewPanel, actionFields);

        LOG.info("{} loaded. Total prepared views: {}", viewClass.getName(), ++totalPreparedViews);

        return new MVPObject(viewPanel);
    }

    private static void scanFieldsWithData(Field[] fields, Model model, ArrayList<Field> viewFields) throws WrongComponentException {
        for (Field field : fields) {
            Data data = field.getAnnotation(Data.class);
            if (data != null) {
                Class clazz = field.getType();
                if (JComponent.class.isAssignableFrom(clazz)) {
                    try {
                        String modelProperty = LegalIdentifierChecker.check(data.value());
                        bindComponent(clazz, model, modelProperty);
                    } catch (InstantiationException | IllegalAccessException ex) {
                        LOG.error("Unable to bind component " + clazz, ex);
                    } catch (InvalidKeywordFromBindingNameException ex) {
                        LOG.warn("Your binding identifier {} is not a valid Java identifer", ex.getInvalidToken());
                    } catch (ReservedKeywordFromBindingNameException ex) {
                        LOG.warn("Your binding identifier {} is a reserved Java keyword", ex.getInvalidToken());
                    }
                } else {
                    throw new WrongComponentException("Component " + field.getType() + " cannot be used for binding the model");
                }
            } else {
                viewFields.add(field);
            }
        }
    }

    private static void scanFieldsWithView(ArrayList<Field> viewFields, ArrayList<Field> actionFields) {
        viewFields.stream().forEach((field) -> {
            final View view = field.getAnnotation(View.class);
            if (view != null) {
                field.setAccessible(true);
                Class subViewClass = field.getType();
                String panelId = view.value();
                MVP mvp;
                try {
                    mvp = prepare(subViewClass);
                } catch (ViewClassNotBindedException | WrongComponentException ex) {
                    mvp = new MVP() {
                        
                        @Override
                        public JPanel getView() {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                        
                        @Override
                        public void setTitle(String title) {
                        }
                        
                        @Override
                        public void display(Class<? extends Window> window, Boolean isVisible) {
                        }
                        
                    };
                }
                components.put(panelId, mvp.getView());
            } else {
                actionFields.add(field);
            }
        });
    }

    private static void scanFieldsWithEvent(ArrayList<Field> actionFields, final JPanel viewPanel, final Class presenterClass, final Object presenter, ArrayList<Field> drawFields) {
        actionFields.stream().forEach((field) -> {
            String presenterMethodName = "";
            try {
                field.setAccessible(true);
                final Event[] events = field.getAnnotationsByType(Event.class);
                final EventContainer[] eventContainers = field.getAnnotationsByType(EventContainer.class);
                
                if (events.length > 0) {
                    Class component = field.getType();
                    Object componentObject = field.get(viewPanel);
                    Class<?> listenerType = ActionListener.class;
                    Map<String, Method> listeners = new HashMap<>();
                    for (Event event : events) {
                        presenterMethodName = LegalIdentifierChecker.check(event.value());
                        String listenerMethod = event.at().getMethod();
                        listenerType = event.at().getListener();
                        listeners.put(listenerMethod, presenterClass.getMethod(presenterMethodName));
                    }
                    Method addListenerToComponentMethod = component.getMethod("add" + listenerType.getSimpleName(), listenerType);
                    Method injectListenerMethod = ListenerFactory.class.getMethod(listenerType.getSimpleName(), Object.class, Map.class);
                    addListenerToComponentMethod.invoke(componentObject, injectListenerMethod.invoke(null, presenter, listeners));
                }
                
                if (eventContainers.length > 0) {
                    Class component = field.getType();
                    Object componentObject = field.get(viewPanel);
                    for (EventContainer eventContainer : eventContainers) {
                        Class listenerType = eventContainer.listens();
                        Map<String, Method> listeners = new HashMap<>();
                        Method addListenerToComponentMethod = component.getMethod("add" + listenerType.getSimpleName(), listenerType);
                        Method injectListenerMethod = ListenerFactory.class.getMethod(listenerType.getSimpleName(), Object.class, Map.class);
                        for (Event event : eventContainer.events()) {
                            EventMethod method = event.at();
                            String listenerMethod = method.getMethod();
                            presenterMethodName = LegalIdentifierChecker.check(event.value());
                            Method presenterMethod = presenterClass.getMethod(presenterMethodName);
                            listeners.put(listenerMethod, presenterMethod);
                        }
                        addListenerToComponentMethod.invoke(componentObject, injectListenerMethod.invoke(null, presenter, listeners));
                    }
                }
                field.setAccessible(false);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                LOG.warn("Is the method {} existed in {}?", presenterMethodName, presenterClass);
            } catch (InvalidKeywordFromBindingNameException ex) {
                LOG.warn("Is the method {} in {} a valid Java keyword?", presenterMethodName, presenterClass);
            } catch (ReservedKeywordFromBindingNameException ex) {
                LOG.warn("Is the method {} in {} a reserved Java keyword?", presenterMethodName, presenterClass);
            }
            Draw draw = field.getAnnotation(Draw.class);
            if (draw != null) {
                drawFields.add(field);
            }
        });
    }

    private static void scanFieldsWithAccess(final Class presenterClass, final Object presenter) throws SecurityException {
        for (Field field : presenterClass.getDeclaredFields()) {
            Access access = field.getAnnotation(Access.class);
            if (access != null) {
                field.setAccessible(true);
                Class<? extends Model> accessModelClass = (Class<? extends Model>) field.getType();
                try {
                    Model checkedOutModel = checkout(accessModelClass);
                    if (checkedOutModel != null) {
                        field.set(presenter, checkedOutModel);
                    } else {
                        Model newModel = (Model) accessModelClass.newInstance();
                        models.put(accessModelClass, newModel);
                        field.set(presenter, newModel);
                    }
                } catch (IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
                    LOG.error("Unable to set model object " + accessModelClass, ex);
                }
            }
        }
    }

    private static void scanFieldsWithDraw(ArrayList<Field> drawFields, final JPanel viewPanel, ArrayList<Field> actionFields) {
        drawFields.stream().forEach((field) -> {
            Draw draw = field.getAnnotation(Draw.class);
            if (draw != null) {
                Class componentClass = null;
                try {
                    field.setAccessible(true);
                    Object component = field.get(viewPanel);
                    ImageIcon icon = iconResource.get(draw.value());
                    componentClass = component.getClass();
                    Method setIconMethod = componentClass.getMethod("setIcon", Icon.class);
                    setIconMethod.invoke(component, icon);
                } catch (IllegalArgumentException | IllegalAccessException | ExecutionException | SecurityException ex) {
                    LOG.error("Cannot set icon in " + componentClass, ex);
                } catch (NoSuchMethodException | InvocationTargetException ex) {
                    LOG.error("The field {} annotated with resource icon is not a component with image binding.", field.getName());
                }
            } else {
                actionFields.add(field);
            }
        });
    }

    /**
     * Deserialization tool to transfer a data from a model to the model origin.
     *
     * This method must not be called unless you know what you're doing.
     *
     * @param model The origin model
     * @param fromJson The model to be transfered
     */
    public synchronized static void deserialize(Model model, Model fromJson) {
        Class<? extends Model> modelClass = model.getClass();
        for (Field field : modelClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Class listClass = field.getType();
                if (listClass == XList.class) {
                    XList oldList = (XList) field.get(model);
                    XList newList = (XList) field.get(fromJson);

                    oldList.clear();

                    if (Model.class.isAssignableFrom(listClass)) {
                        newList.stream().forEach((e) -> {
                            oldList.add(e);
                        });
                    } else {
                        newList.stream().forEach((e) -> {
                            oldList.add(new MyObject(e));
                        });
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
                LOG.error("Cannot deserialize " + modelClass, ex);
            } catch (NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                LOG.error("Cannot deserialize " + modelClass, ex);
            }
        }
    }

    /**
     * Snips a field data in the Model. To snip, fields must have @Snip
     * annotation
     *
     * Useful for snipping sensitive data like Passwords, Credit Card numbers,
     * etc.
     *
     * @param model The model in which its field(s) to be snipped
     * @return A JSON data containing the final snipped values
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected static String snip(Model model) throws IllegalArgumentException, IllegalAccessException {
        Gson gson = new GsonBuilder().create();
        Class modelClass = model.getClass();
        String json = gson.toJson(model);
        Model clonedModel = (Model) gson.fromJson(json, modelClass);
        for (Field field : modelClass.getDeclaredFields()) {
            Snip snip = field.getAnnotation(Snip.class);
            if (snip != null) {
                field.setAccessible(true);
                field.set(clonedModel, "*snipped*");
                field.setAccessible(false);
            }
        }
        return gson.toJson(clonedModel);
    }

    /**
     * Injects the Wirex REST specification listener to the event components.
     *
     * @param presenter The presenter object to inject the server connectors to
     * its annotated methods
     * @param method The method to inject with
     */
    public synchronized static void injectRestSpec(Object presenter, Method method) {
        Path path = method.getAnnotation(Path.class);
        Type type = method.getAnnotation(Type.class);
        Form form = method.getAnnotation(Form.class);
        POST post = method.getAnnotation(POST.class);
        GET get = method.getAnnotation(GET.class);
        PUT put = method.getAnnotation(PUT.class);
        DELETE delete = method.getAnnotation(DELETE.class);

        String urlPath;
        if (path != null) {
            Method initMethod = null;
            if (post != null) {
                try {
                    if (path.value().startsWith("/")) {
                        urlPath = path.value().substring(1);
                    } else {
                        urlPath = path.value();
                    }
                    Class presenterClass = presenter.getClass();
                    initMethod = presenterClass.getSuperclass().getDeclaredMethod("init", String.class, Media.class, String.class, Model.class);
                    initMethod.setAccessible(true);
                    initMethod.invoke(presenter, hostname + urlPath, type.value(), "POST", form != null ? form.value() : null);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.error("Framework bug! Cannot invoke method " + initMethod, ex);
                }
            } else if (get != null) {
                try {
                    if (path.value().startsWith("/")) {
                        urlPath = path.value().substring(1);
                    } else {
                        urlPath = path.value();
                    }
                    Class presenterClass = presenter.getClass();
                    initMethod = presenterClass.getSuperclass().getDeclaredMethod("init", String.class, Media.class, String.class);
                    initMethod.setAccessible(true);
                    initMethod.invoke(presenter, hostname + urlPath, type.value(), "GET");
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.error("Framework bug! Cannot invoke method " + initMethod, ex);
                }
            }
        }
    }

    /**
     * Transitional state method to change windows and disposing a window. This
     * method should not be used unless you know what you're doing
     *
     * @param presenter The presenter object to inject events in window changing
     * @param method The particular method in the presenter object to invoke its
     * window changing
     * @throws ViewClassNotBindedException Throws this exception when a View
     * class doesn't have a binding annotation
     * @throws WrongComponentException Throws this exception when a annotated
     * field declaration is not of type JComponent
     */
    public synchronized static void proceed(Object presenter, Method method) throws ViewClassNotBindedException, WrongComponentException {
        Fire fire = method.getAnnotation(Fire.class);
        Dispose dispose = method.getAnnotation(Dispose.class);
        if (fire != null) {
            Class<? extends JPanel> panelClass = fire.view();
            MVP mvp = prepare(panelClass);
            mvp.display(fire.type(), true);
        }
        if (dispose != null) {
            dispose((Presenter) presenter);
        }
    }

    private static void bindComponent(Class component, Object bean, String property) throws InstantiationException, IllegalAccessException {
        PresentationModel adapter = new PresentationModel(bean);
        ValueModel componentModel = adapter.getModel(property);
        JComponent newComponent = (JComponent) component.newInstance();
        if (JTextField.class == component || JTextField.class.isAssignableFrom(component)) {
            Bindings.bind((JTextField) newComponent, componentModel);
        } else if (JLabel.class == component || JLabel.class.isAssignableFrom(component)) {
            Bindings.bind((JLabel) newComponent, componentModel);
        } else if (JCheckBox.class == component || JCheckBox.class.isAssignableFrom(component)) {
            Bindings.bind((JCheckBox) newComponent, componentModel);
        } else if (JComboBox.class == component || JComboBox.class.isAssignableFrom(component)) {
            SelectionInList selectionModel = new SelectionInList(componentModel);
            Bindings.bind((JComboBox) newComponent, selectionModel, "");
        } else if (JProgressBar.class == component || JProgressBar.class.isAssignableFrom(component)) {
            BeanAdapter beanAdapter = new BeanAdapter(bean, true);
            Bindings.bind(newComponent, "value", beanAdapter.getValueModel(property));
            Bindings.bind(newComponent, "indeterminate", beanAdapter.getValueModel(property + 2));
        } else if (JTextArea.class == component || JTextArea.class.isAssignableFrom(component)) {
            Bindings.bind((JTextArea) newComponent, componentModel);
        } else if (JTree.class == component || JTree.class.isAssignableFrom(component)) {
            try {
                Field listField = bean.getClass().getDeclaredField(property);
                Class modelClass = listField.getType();
                Object model = componentModel.getValue();
                EventList eventList = (XList) model;
                SortedList sortedList = new SortedList(eventList, null);
                TreeList list = new TreeList(sortedList, new XTreeFormat(model), TreeList.NODES_START_EXPANDED);
                JTree tree = new JTree(new EventTreeModel(list));
//                tree.setCellRenderer(new TreeCellRenderer() {
//                    @Override
//                    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                        JLabel label = new JLabel();
//                        try {
//                            TreeList.Node list = (TreeList.Node) value;
//
//                            label.setText(list.getElement().toString());
//                            return label;
//                        } catch (ClassCastException ex) {
//                            return label;
//                        }
//                    }
//                });
                newComponent = tree;
            } catch (NoSuchFieldException | SecurityException ex) {
                LOG.warn("Unable to bind component " + component + " with " + property, ex);
                newComponent = new JTree();
            }
        } else if (JTable.class == component || JTable.class.isAssignableFrom(component)) {
            try {
                /*
                 * Throw exception if List has no generic types
                 */

                Field listField = bean.getClass().getDeclaredField(property);
                java.lang.reflect.Type type = listField.getGenericType();

                ParameterizedType listType;
                Class<?> listTypeClass;
                if (type instanceof Class) {
                    listTypeClass = (Class<?>) type;
                } else {
                    listType = (ParameterizedType) type;
                    listTypeClass = (Class<?>) listType.getActualTypeArguments()[0];
                }

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
//                    System.out.println(listTypeClass);
                }

                EventList rows = (XList) adapter.getModel(property).getValue();
                TableFormat tf = GlazedLists.tableFormat(listTypeClass, propertyNames, propertyNames);

                JTable table = new JTable(new EventTableModel(rows, tf));

                newComponent = table;
            } catch (NoSuchFieldException | SecurityException ex) {
                LOG.error("Unable to bind component " + component + " with " + property, ex);
                newComponent = new JTable();
            }

        } else if (JPasswordField.class == component || JPasswordField.class.isAssignableFrom(component)) {
            newComponent = BasicComponentFactory.createPasswordField(componentModel);
        } else {
            try {
                throw new UnknownComponentException(component.getName() + " is neither a JComponent nor supported in Wirex.");
            } catch (UnknownComponentException ex) {
                LOG.error("Unable to bind component " + component + " with " + property, ex);
            }
            try {
                newComponent = (JComponent) component.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                newComponent = null;
            }
        }
        components.put(property, newComponent);
    }

    /**
     * Disposes a presenter's binded View
     *
     * @param presenter The presenter where its binded View to be disposed
     */
    public static void dispose(Presenter presenter) {
        JPanel panel = presenter.getPanel();
        Window window = SwingUtilities.getWindowAncestor(panel);
        window.dispose();
    }

    /**
     * Data structure for Wirex simple object. Useful when a value's type is a
     * primary data type.
     */
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
                AppEngine.injectRestSpec(presenter, methodInPresenter);
                methodInPresenter.invoke(presenter);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.error("Unable to invoke method " + methodName + " in " + presenter.getClass(), ex);
            }
        }
    }

    private static class MVPObject implements MVP {

        private final JPanel viewPanel;

        public MVPObject(JPanel viewPanel) {
            this.viewPanel = viewPanel;
        }
        private String title = "Untitled";

        @Override
        public JPanel getView() {
            return viewPanel;
        }

            @Override
            public void setTitle(String title) {
                this.title = title;
                Window window = SwingUtilities.getWindowAncestor(viewPanel);
                if (window != null) {
                    if (window.getClass() == JFrame.class) {
                        ((JFrame) window).setTitle(title);
                    } else {
                        ((JDialog) window).setTitle(title);
                    }
                }
            }

            @Override
            public void display(final Class<? extends Window> window, final Boolean isVisible) {
                EventQueue.invokeLater(() -> {
                    Window dialog;
                    if (window == JFrame.class) {
                        dialog = new JFrame();
                        ((JFrame) dialog).setTitle(title);
                        ((JFrame) dialog).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    } else {
                        dialog = new JDialog();
                        ((JDialog) dialog).setTitle(title);
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
                });
            }
    }
}
