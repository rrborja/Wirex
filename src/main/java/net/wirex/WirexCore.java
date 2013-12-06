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
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyNotFoundException;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreeModel;
import javax.validation.ConstraintValidator;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;
import net.wirex.annotations.Access;
import net.wirex.annotations.Balloon;
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
import net.wirex.annotations.Property;
import net.wirex.annotations.Optional;
import net.wirex.annotations.Permit;
import net.wirex.annotations.Retrieve;
import net.wirex.annotations.Rule;
import net.wirex.annotations.Snip;
import net.wirex.annotations.Text;
import net.wirex.annotations.Type;
import net.wirex.annotations.View;
import net.wirex.enums.Media;
import net.wirex.exceptions.InvalidKeywordFromBindingNameException;
import net.wirex.exceptions.ReservedKeywordFromBindingNameException;
import net.wirex.exceptions.UnknownComponentException;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.gui.ErrorReportPanel;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.interfaces.Resource;
import net.wirex.interfaces.Validator;
import net.wirex.structures.XList;
import net.wirex.structures.XObject;
import net.wirex.structures.XTreeFormat;
import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
final class WirexCore implements Wirex {

    private static final Logger LOG = LoggerFactory.getLogger(Wirex.class.getSimpleName());

    static {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        LOG.info("Wirex Framework {}", dateFormat.format(date));
    }

    private final LoadingCache<ServerRequest, ServerResponse> cacheResource = CacheBuilder.newBuilder()
            .maximumSize(1)
            .concurrencyLevel(10)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new ServerResponseCacheLoader());

    private final Queue accessQueue = new ConcurrentLinkedQueue();

    private final ConcurrentMap<Class<? extends Presenter>, PresenterModel> presenterModels = new ConcurrentHashMap();

    private final ConcurrentMap<String, JComponent> components = new ConcurrentHashMap();

    private final ConcurrentMap<Class<? extends Validator>, List> validators = new ConcurrentHashMap();

    private final ConcurrentMap<String, JLabel> mediators = new ConcurrentHashMap();

    private final LoadingCache<Class<? extends Model>, Model> modelCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Class<? extends Model>, Model>() {
                        public @Override
                        Model load(Class<? extends Model> name) {
                            return models.get(name);
                        }
                    });

    private final ConcurrentMap<Class<? extends Presenter>, Presenter> presenters = new ConcurrentHashMap();

    private final ConcurrentMap<Class<? extends Model>, Model> models = new ConcurrentHashMap();

    private final LoadingCache<String, ImageIcon> iconResource = CacheBuilder.newBuilder()
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

    private int totalPreparedViews;

    private String hostname;

    private String resourceHostname;

    private Class<? extends Model> privilegeModelClass;

    private String error;

    private BufferedImage screenshot;

    public WirexCore() {
        this.totalPreparedViews = 0;
        this.hostname = "http://10.0.1.46:8080/";
        this.resourceHostname = "http://10.0.1.46/~rborja/icons/";
        this.privilegeModelClass = null;
    }

    public WirexCore(String hostname, String resourceHostname, Class privilegeModelClass) {
        this.totalPreparedViews = 0;
        this.hostname = hostname;
        this.resourceHostname = resourceHostname;
        this.privilegeModelClass = privilegeModelClass;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public BufferedImage getScreenshot() {
        return screenshot;
    }

    @Override
    public Icon icon(String filename) {
        try {
            return iconResource.get(filename);
        } catch (ExecutionException ex) {
            LOG.warn("IO error in icon retrieval of {}", filename);
            return new ImageIcon();
        }
    }

    /**
     * Checkouts a binded component.
     *
     * @param <T> The type must be a JComponent class or its subclass
     * @param name The name of the component binded from the annotated fields
     * @return Returns a model-binded component
     * @deprecated Use checkout(Class component, String name) instead
     */
    @Deprecated
    @Override
    public <T> T checkout(String name) {
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
    @Override
    public <T> T checkout(Class<T> component, String name) {
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

    @Override
    public void setPermissionModel(Class<? extends Model> modelClass) {
        this.privilegeModelClass = modelClass;
        Model model;
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return;
        }
        models.put(modelClass, model);
    }

    @Override
    public JLabel mediator(String name) {
        return mediators.remove(name);
    }

    @Override
    public List checkout(Class<? extends Validator> validator) {
        if (validator != null) {
            return validators.get(validator);
        } else {
            return null;
        }
    }

    private Model checkoutModel(Class<? extends Model> modelClass) {
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
    @Override
    public void connect(String url) {
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
    @Override
    public void locateResource(String url) {
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
    @Override
    public ServerResponse push(ServerRequest request) {
        try {
            return cacheResource.get(request);
        } catch (ExecutionException ex) {
            LOG.error(request.getBody(), ex);
            return null;
        }
    }

    @Override
    public void showError(Presenter presenter, Exception error) {
        try {
            String resultingError = "Caused by: " + error.getCause().toString() + "\n";
            StackTraceElement[] errors = error.getCause().getStackTrace();
            for (StackTraceElement error1 : errors) {
                resultingError += error1.toString() + "\n";
            }
            this.error = resultingError;
            this.screenshot = createImage(presenter.getPanel());
            MVP mvp = prepare(ErrorReportPanel.class);
            mvp.display(JDialog.class);
        } catch (ViewClassNotBindedException | WrongComponentException ex) {
            java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public <T> T access(Class<T> presenterClass) {
        if (!Presenter.class.isAssignableFrom(presenterClass)) {
            LOG.warn("Is the {} of Presenter type? Please review your code.", presenterClass);
            return null;
        }
        Class<? extends Presenter> presenter = (Class<? extends Presenter>) presenterClass;
        if (presenters.containsKey(presenter)) {
            return (T) presenters.get(presenter);
        } else {
            LOG.warn("Presenter ({}) does not or still not yet existed", presenterClass);
            return null;
        }
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
    @Override
    public MVP prepare(Class viewClass) throws ViewClassNotBindedException, WrongComponentException {
        return prepare(viewClass, null);
    }

    protected MVP prepare(Class viewClass, Window parent) throws ViewClassNotBindedException, WrongComponentException {

        Bind bind = (Bind) viewClass.getAnnotation(Bind.class);
        if (bind == null) {
            throw new ViewClassNotBindedException("Have you annotated @Bind to your " + viewClass.getSimpleName() + " class?");
        }

        Property property = (Property) viewClass.getAnnotation(Property.class);

        Class modelClass = bind.model();
        final Class presenterClass = bind.presenter();
        Field[] fields = viewClass.getDeclaredFields();

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

        for (Field field : fields) {
            final Data data = field.getAnnotation(Data.class);
            final View view = field.getAnnotation(View.class);
            scanFieldWithData(data, field, model);
            scanFieldWithView(view, field);
        }

        Resource resource;
        try {
            if (property != null) {
                resource = (Resource) property.value().newInstance();
            } else {
                resource = null;
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.warn("Unable to load resource " + property.value(), ex);
            resource = null;
        }

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
            presenters.put(presenterClass, (Presenter) presenter);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.error("Unable to create " + presenterClass, ex);
            return null;
        }

        for (Field field : fields) {
            field.setAccessible(true);

            final Event[] events = field.getAnnotationsByType(Event.class);
            final EventContainer[] eventContainers = field.getAnnotationsByType(EventContainer.class);
            final Draw draw = field.getAnnotation(Draw.class);
            final Text[] texts = field.getAnnotationsByType(Text.class);
            final Balloon balloon = field.getAnnotation(Balloon.class);
            final Permit permit = field.getAnnotation(Permit.class);

            scanFieldWithEvent(events, field, viewPanel, presenterClass, presenter, eventContainers);
            scanFieldWithDraw(draw, field, viewPanel);
            scanFieldWithText(texts, field, viewPanel, resource);
            scanFieldWithBalloon(balloon, field, viewPanel);
            scanFieldWithPermit(permit, field, viewPanel);

            field.setAccessible(false);
        }

        scanFieldsWithAccess(presenterClass, presenter);

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

        LOG.info("{} loaded. Total prepared views: {}", viewClass.getName(), ++totalPreparedViews);

        return new MVPObject(viewPanel, parent);
    }

    private void scanFieldWithView(final View view, Field field) throws SecurityException {
        if (view != null) {
            field.setAccessible(true);
            Class subViewClass = field.getType();
            String panelId = view.value();
            MVP mvp;
            try {
                mvp = prepare(subViewClass);
            } catch (ViewClassNotBindedException | WrongComponentException ex) {
                mvp = new MVPObject(new JPanel());
            }
            components.put(panelId, mvp.getView());
        }
    }

    private void scanFieldWithData(final Data data, final Field field, final Model model) throws WrongComponentException {
        if (data != null) {
            Class clazz = field.getType();
            final String modelProperty;
            if (JComponent.class.isAssignableFrom(clazz)) {
                try {
                    modelProperty = LegalIdentifierChecker.check(data.value());
                    bindComponent(clazz, model, modelProperty);
                } catch (InstantiationException | IllegalAccessException ex) {
                    LOG.error("Unable to bind component " + clazz, ex);
                    return;
                } catch (InvalidKeywordFromBindingNameException ex) {
                    LOG.warn("Your binding identifier {} is not a valid Java identifer", ex.getInvalidToken());
                    return;
                } catch (ReservedKeywordFromBindingNameException ex) {
                    LOG.warn("Your binding identifier {} is a reserved Java keyword", ex.getInvalidToken());
                    return;
                } catch (PropertyNotFoundException ex) {
                    LOG.warn("Your binding property '{}' doesn't bind to any existing components", data.value());
                    return;
                }
            } else {
                throw new WrongComponentException("Component " + field.getType() + " cannot be used for binding the model");
            }
            Rule rule = (Rule) field.getAnnotation(Rule.class);
            if (rule != null) {
                Class<? extends ConstraintValidator> validatorClass = rule.value();
                ConstraintValidator validator;
                try {
                    validator = validatorClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    return;
                }
                JLabel label = new JLabel();
                Object component = components.get(modelProperty);
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    textField.getDocument().addDocumentListener(new DocumentListener() {
                        public void validate() {
                            Class modelClass = model.getClass();
                            Model model = models.get(modelClass);
                            Optional notRequired = field.getAnnotation(Optional.class);

                            PresentationModel adapter = new PresentationModel(model);
                            ValueModel componentModel = adapter.getModel(modelProperty);
                            Object value = componentModel.getValue();
                            String inputText = value != null ? value.toString() : "";

                            if (notRequired != null) {
                                return;
                            }
                            if (validator.isValid(inputText, null)) {
                                label.setForeground(Color.BLUE);
                            } else {
                                label.setForeground(Color.RED);
                            }
                        }

                        public void checkChanges() {

                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            validate();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            validate();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            validate();
                        }

                    });
                }
                mediators.put(modelProperty, label);
            }
        }
    }

    private void scanFieldWithText(final Text[] texts, Field field, final JPanel viewPanel, Resource resource) {
        if (texts.length > 0) {
            try {
                Object componentInstance = field.get(viewPanel);
                Class component = field.getType();
                if (component != JTable.class) {
                    Method setTextMethod = component.getMethod("setText", String.class);
                    String propertyText = texts[0].value();
                    Class resourceClass = resource.getClass();
                    Field resourceField = resourceClass.getDeclaredField(propertyText);
                    resourceField.setAccessible(true);
                    String textValue = resourceField.get(resource).toString();
                    setTextMethod.invoke(componentInstance, textValue);
                } else {
                    JTable table = (JTable) componentInstance;
                    JTableHeader th = new JTableHeader();
                    TableColumnModel tcm = new DefaultTableColumnModel();
                    for (Text text : texts) {
                        TableColumn column = new TableColumn();
                        column.setHeaderValue(text);
                        tcm.addColumn(column);
                    }
                    th.setColumnModel(tcm);
                    table.setTableHeader(th);
                    table.repaint();
                }
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException | NoSuchFieldException ex) {
                java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void scanFieldWithDraw(final Draw draw, Field field, final JPanel viewPanel) {
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
        }
    }

    private void scanFieldWithEvent(final Event[] events, Field field, final JPanel viewPanel, final Class presenterClass, final Object presenter, final EventContainer[] eventContainers) throws IllegalArgumentException, SecurityException {
        String presenterMethodName = "";
        try {
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

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            LOG.warn("Is the method {} existed in {}?", presenterMethodName, presenterClass);
        } catch (InvalidKeywordFromBindingNameException ex) {
            LOG.warn("Is the method {} in {} a valid Java keyword?", presenterMethodName, presenterClass);
        } catch (ReservedKeywordFromBindingNameException ex) {
            LOG.warn("Is the method {} in {} a reserved Java keyword?", presenterMethodName, presenterClass);
        }
    }

    private void scanFieldsWithAccess(final Class presenterClass, final Object presenter) throws SecurityException {
        PresenterModel presenterModel = new PresenterModel();
        for (Field field : presenterClass.getDeclaredFields()) {
            Access access = field.getAnnotation(Access.class);
            if (access != null) {
                field.setAccessible(true);
                Class<? extends Model> accessModelClass = (Class<? extends Model>) field.getType();
                try {
                    Model checkedOutModel = checkoutModel(accessModelClass);
                    if (checkedOutModel != null) {
                        field.set(presenter, checkedOutModel);
                        presenterModel.put(field.getName(), checkedOutModel);
                    } else {
                        Model newModel = (Model) accessModelClass.newInstance();
                        models.put(accessModelClass, newModel);
                        field.set(presenter, newModel);
                        presenterModel.put(field.getName(), newModel);
                    }
                } catch (IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
                    LOG.error("Unable to set model object " + accessModelClass, ex);
                }
            }
        }
        if (!presenterModel.isEmpty()) {
            presenterModels.put(presenterClass, presenterModel);
        }
    }

    @Override
    public ImmutableMap form(Presenter presenter) {
        Class<? extends Presenter> presenterClass = presenter.getClass();
        PresenterModel presenterModel = presenterModels.get(presenterClass);
        return presenterModel.getForm();
    }

    /**
     * Deserialization tool to transfer a data from a model to the model origin.
     *
     * This method must not be called unless you know what you're doing.
     *
     * @param model The origin model
     * @param fromJson The model to be transfered
     */
    @Override
    public synchronized void deserialize(Model model, Model fromJson) {
        Class<? extends Model> modelClass = model.getClass();
        for (Field field : modelClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Class listClass = field.getType();
                if (listClass == XList.class) {
                    XList oldList = (XList) field.get(model);
                    XList newList = (XList) field.get(fromJson);

                    oldList.clear();
                    ParameterizedType pType = (ParameterizedType) field.getGenericType();
                    System.out.println((Class<?>) pType.getActualTypeArguments()[0]);
                    if (Model.class.isAssignableFrom((Class<?>) pType.getActualTypeArguments()[0])) {
                        newList.stream().forEach(e -> {
                            oldList.getReadWriteLock().readLock().lock();
                            try {
                                oldList.add(e);
                            } finally {
                                oldList.getReadWriteLock().readLock().unlock();
                            }
                        });
                    } else {
                        newList.stream().forEach(e -> {
                            oldList.getReadWriteLock().readLock().lock();
                            try {
                                oldList.add(new XObject(e));
                            } finally {
                                oldList.getReadWriteLock().readLock().unlock();
                            }
                        });
                    }

                    field.set(model, oldList);
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

    @Override
    public String encodeToUrl(Model body) {
        String encodedUrl = "";
        Class modelClass = body.getClass();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers)) {
                try {
                    field.setAccessible(true);
                    String key = field.getName();
                    String value = field.get(body).toString();
                    encodedUrl += key + "=" + value + "&";
                    field.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOG.error("Framework bug! Can't access field.", ex);
                }
            }
        }
        return encodedUrl.substring(0, encodedUrl.length() - 1);
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
    @Override
    public String snip(Model model) throws IllegalArgumentException, IllegalAccessException {
        Gson gson = new GsonBuilder().create();
        Class modelClass = model.getClass();
        String json;
        if (modelClass.isAssignableFrom(PresenterModel.class)) {
            PresenterModel presenterModel = (PresenterModel) model;
            return gson.toJson(presenterModel.getForm());
        } else {
            json = gson.toJson(model);
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
    }

    /**
     * Injects the Wirex REST specification listener to the event components.
     *
     * @param presenter The presenter object to inject the server connectors to
     * its annotated methods
     * @param method The method to inject with
     */
    @Override
    public synchronized void injectRestSpec(Object presenter, Method method) {
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
    @Override
    public synchronized void proceed(Object presenter, Method method) throws ViewClassNotBindedException, WrongComponentException {
        Fire fire = method.getAnnotation(Fire.class);
        Dispose dispose = method.getAnnotation(Dispose.class);
        if (fire != null) {
            Class<? extends JPanel> panelClass = fire.view();
            Window parent = SwingUtilities.getWindowAncestor(((Presenter) presenter).getPanel());
            MVP mvp = prepare(panelClass, parent);
            mvp.setTitle(fire.title());
            mvp.display(fire.type(), true);
            mvp.setResizable(fire.dynamic());
        }
        if (dispose != null) {
            dispose((Presenter) presenter);
        }
    }

    private void bindComponent(Class component, Object bean, String property) throws InstantiationException, IllegalAccessException, PropertyNotFoundException {
        PresentationModel adapter = new PresentationModel(bean);
        ValueModel componentModel = adapter.getModel(property);
        JComponent newComponent = (JComponent) component.newInstance();
        if (JTextField.class == component || JTextField.class.isAssignableFrom(component)) {
            Bindings.bind((JTextField) newComponent, componentModel);
        } else if (JLabel.class == component || JLabel.class.isAssignableFrom(component)) {
            Bindings.bind((JFormattedTextField) newComponent, componentModel);
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
//                JTree tree = new JTree(new EventTreeModel(list));
                if (JTree.class == component) {
                    JTree tree = new JTree(new EventTreeModel(list));
                    newComponent = tree;
                } else {
                    newComponent = (JComponent) component.getConstructor(TreeModel.class).newInstance(new EventTreeModel(list));
                }
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
//                newComponent = tree;
            } catch (NoSuchFieldException | SecurityException ex) {
                LOG.warn("Unable to bind component " + component + " with " + property, ex);
                newComponent = new JTree();
            } catch (NoSuchMethodException ex) {
                java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (JTable.class == component || JTable.class.isAssignableFrom(component) || JXTable.class == component || JXTable.class.isAssignableFrom(component)) {
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
                        list.set(i, new XObject(list.get(i)));
                    }
                    adapter.setValue(property, list);
                    listTypeClass = XObject.class;
//                    System.out.println(listTypeClass);
                }

                EventList rows = (XList) adapter.getModel(property).getValue();
                TableFormat tf = GlazedLists.tableFormat(listTypeClass, propertyNames, propertyNames);

                if (JTable.class == component) {
                    JTable table = new JTable(new EventTableModel(rows, tf));
                    newComponent = table;
                } else {
                    JXTable table = new JXTable(new EventTableModel(rows, tf));
                    newComponent = table;
                }

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
    @Override
    public void dispose(Presenter presenter) {
        JPanel panel = presenter.getPanel();
        Window window = SwingUtilities.getWindowAncestor(panel);
        window.dispose();
        totalPreparedViews--;
    }

    private void scanFieldWithBalloon(final Balloon balloon, final Field field, final JPanel viewPanel) throws ViewClassNotBindedException, WrongComponentException {
        try {
            if (balloon != null) {
                JComponent fieldComponent = (JComponent) field.get(viewPanel);
                String text = balloon.text();
                Class<? extends JComponent> componentClass = balloon.value();
                Integer seconds = balloon.seconds();
                if (componentClass == JPanel.class) {
                    JLabel label = new JLabel(text);
                    label.setForeground(new Color(230, 230, 230));
                    BalloonTip balloonTip = new BalloonTip(fieldComponent, label,
                            new MinimalBalloonStyle(new Color(0, 0, 0, 200), 10),
                            BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.CENTER,
                            0, 5, false);
                    ToolTipUtils.balloonToToolTip(balloonTip, seconds * 100, 3000);
                } else {
                    Object component = componentClass.newInstance();
                    if (component instanceof JPanel) {
                        JPanel view = (JPanel) component;
                        Class viewClass = view.getClass();
                        Bind bind = (Bind) viewClass.getAnnotation(Bind.class);
                        JPanel finalBalloonPanel;
                        if (bind != null) {
                            MVP mvp = prepare(viewClass);
                            finalBalloonPanel = mvp.getView();
                        } else {
                            finalBalloonPanel = (JPanel) viewClass.newInstance();
                        }
                        BalloonTip balloonTip = new BalloonTip(fieldComponent, finalBalloonPanel,
                                new MinimalBalloonStyle(new Color(0, 0, 0, 200), 10),
                                BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.CENTER,
                                0, 5, false);
                        ToolTipUtils.balloonToToolTip(balloonTip, seconds * 100, 3000000);
                    } else {
                        BalloonTip balloonTip = new BalloonTip(fieldComponent, (JComponent) component,
                                new MinimalBalloonStyle(new Color(0, 0, 0, 200), 10),
                                BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.CENTER,
                                0, 5, false);
                        ToolTipUtils.balloonToToolTip(balloonTip, seconds * 100, 3000000);
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void scanFieldWithPermit(final Permit permit, final Field field, final JPanel viewPanel) {
        if (permit != null) {
            try {
                Model privilegeModel = models.get(privilegeModelClass);
                String property = permit.value();
                PresentationModel adapter = new PresentationModel(privilegeModel);
                ValueModel componentModel = adapter.getModel(property);
                Boolean value = (Boolean) componentModel.getValue();

                JComponent component = (JComponent) field.get(viewPanel);

                BeanAdapter beanAdapter = new BeanAdapter(privilegeModel, true);
                Bindings.bind(component, "enabled", beanAdapter.getValueModel(property));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        return bi;
    }

    public class MyActionListener implements ActionListener {

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

    private class AccessTask {

        Presenter presenter;
        Field field;

    }

    private class MVPObject implements MVP {

        private final JPanel viewPanel;
        private final Window parent;

        public MVPObject(JPanel viewPanel) {
            this(viewPanel, null);
        }

        public MVPObject(JPanel viewPanel, Window parent) {
            this.viewPanel = viewPanel;
            this.parent = parent;
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
                    ((JFrame) dialog).setContentPane(viewPanel);
                } else {
                    dialog = new JDialog(parent);
                    ((JDialog) dialog).setTitle(title);
                    ((JDialog) dialog).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    ((JDialog) dialog).setContentPane(viewPanel);
                }
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

        @Override
        public void display(Class<? extends Window> window) {
            this.display(window, true);
        }

        @Override
        public void setResizable(boolean resizable) {
            Window window = SwingUtilities.getWindowAncestor(viewPanel);
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                dialog.setResizable(resizable);
            } else if (window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                frame.setResizable(resizable);
            } else {
                LOG.warn("Current window type {} doesn't support yet in this framework", window.getType());
            }
        }

    }
}
