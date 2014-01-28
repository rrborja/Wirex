package net.wirex;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.EventTreeModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyNotFoundException;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.validation.ConstraintValidator;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;
import net.wirex.annotations.Access;
import net.wirex.annotations.Balloon;
import net.wirex.annotations.Bind;
import net.wirex.annotations.Column;
import net.wirex.annotations.DELETE;
import net.wirex.annotations.Data;
import net.wirex.annotations.Dispose;
import net.wirex.annotations.Draw;
import net.wirex.annotations.Editable;
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
import net.wirex.annotations.RenderAs;
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
import net.wirex.structures.XComponent;
import net.wirex.structures.XList;
import net.wirex.structures.XLive;
import net.wirex.structures.XObject;
import net.wirex.structures.XRenderer;
import net.wirex.structures.XTreeFormat;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
final class WirexCore implements Wirex {

    private static final Logger LOG = LoggerFactory.getLogger(Wirex.class.getSimpleName());

    public static final String version = "1.0.14.40-BETA";

    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date dateObject = new Date();
            String date = dateFormat.format(dateObject);
            LOG.info("Wirex Framework v{} {}", version, date);
        } catch (Throwable t) {
            LOG.info("UI Development inside IDE");
        }
    }

    private static Map getMethods(Method[] methods) {
        Map<String, Method> map = new HashMap<>(0);
        for (Method method : methods) {
            String name = method.getName();
            map.put(name, method);
        }
        return map;
    }

    private WirexLock semaphore = WirexLock.getInstance();
    
    private final LoadingCache<ServerRequest, ServerResponse> cacheResource = CacheBuilder.newBuilder()
            .maximumSize(1)
            .concurrencyLevel(10)
            .expireAfterAccess(1, TimeUnit.SECONDS)
            .build(new ServerResponseCacheLoader(semaphore));

    private final ConcurrentMap<Class<? extends Presenter>, PresenterModel> presenterModels = new ConcurrentHashMap(10);

    private final ConcurrentMap<String, JComponent> components = new ConcurrentHashMap(10);

    private final ConcurrentMap<Class<? extends Validator>, List> validators = new ConcurrentHashMap(10);

    private final ConcurrentMap<String, JLabel> mediators = new ConcurrentHashMap(10);

    private final LoadingCache<Class<? extends Model>, Model> modelCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Class<? extends Model>, Model>() {
                public @Override
                Model load(Class<? extends Model> name) {
                    return models.get(name);
                }
            });

    private final ConcurrentMap<Class<? extends Presenter>, Presenter> presenters = new ConcurrentHashMap(10);

    private final ConcurrentMap<Class<? extends Model>, Model> models = new ConcurrentHashMap(10);

    private final LoadingCache<String, ImageIcon> iconResource = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ImageIcon>() {
                @Override
                public ImageIcon load(String iconName) {
                    String name = resourceHostname + iconName;
                    Image resource = null;
                    try {
                        URL url = new URL(name);
                        URLConnection con = url.openConnection();
                        con.setConnectTimeout(3000);
                        con.setReadTimeout(3000);
                        InputStream in = con.getInputStream();
                        resource = ImageIO.read(in);
                    } catch (IOException ex) {
                        LOG.warn("Icon at {} not found", name);
                    }
                    if (resource == null) {
                        LOG.warn("Missing icon at {}", name);
                        return new ImageIcon();
                    }
                    return new ImageIcon(resource);
                }
            });


    private int stackCount;

    private Stack<String> preStackUncheckedOutComponentCounts;

    private Queue<String> preStackNoResourcePropertyCounts;

    private int totalPreparedViews;

    private String hostname;

    private String resourceHostname;

    private Class<? extends Model> privilegeModelClass;

    private String error;

    private BufferedImage screenshot;

    private Map<String, XLive> liveContainer;

    private SocketEngine socket;

    private boolean encrypt;

    private Image appIcon;

    WirexCore() {
        this("http://10.0.1.69:8080/g7/", "jar:http://10.0.1.69:8080/g7/icon!/", null);
    }

    WirexCore(String hostname, String resourceHostname, Class privilegeModelClass) {
        this.totalPreparedViews = 0;
        this.stackCount = 0;
        this.preStackUncheckedOutComponentCounts = new Stack();
        this.preStackNoResourcePropertyCounts = new LinkedList();
        this.hostname = hostname;
        this.resourceHostname = resourceHostname;
        this.privilegeModelClass = privilegeModelClass;
        this.liveContainer = new HashMap<>(3);
        this.socket = new SocketEngine();
        this.encrypt = false;
//        new ConsoleProcess("console").start();
    }

    @Override
    public void setTrayIcon(String iconName) {
        ImageIcon icon;
        try {
            icon = iconResource.get(iconName + ".png");
        } catch (ExecutionException ex) {
            LOG.warn("Error loading {}", resourceHostname + iconName);
            return;
        }
        WirexSystemTray.init(icon);
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
                LOG.warn("No such component as {} of {}", component);
                return component.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.error("No instance for " + component, ex);
                return null;
            }
        }
    }

    @Override
    public <T> T checkout(Class<T> component, String name, String path) {
        ServerRequest request = new ServerRequest("GET", path, Media.JSON, null, null, null);
        ServerResponse response;
        try {
            response = cacheResource.get(request);
        } catch (ExecutionException ex) {
            LOG.warn("Can't load component {} from {}", name, hostname + path);
            return null;
        }
//        response.getMessage()
        return checkout(component, name);
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
        if (mediators.containsKey(name)) {
            return mediators.remove(name);
        } else {
            LOG.info("This mediator {} doesn't exist in the Wirex container.", name);
            return new JLabel();
        }
    }

    @Override
    public List checkout(Class<? extends Validator> validator) {
        if (validator != null) {
            return validators.get(validator);
        } else {
            return null;
        }
    }

    @Override
    public Model checkoutModel(Class<? extends Model> modelClass) {
        if (!Model.class.isAssignableFrom(modelClass)) {
            LOG.warn("Is the {} of Model type? Please review your code.", modelClass);
            return null;
        }
        if (modelClass != null) {
            return models.get(modelClass);
        } else {
            return null;
        }
    }

    @Override
    public List listModels() {
        return new ArrayList<>(models.keySet());
    }

    @Override
    public List listViews() {
        return null;
    }

    @Override
    public List listPresenters() {
        return new ArrayList<>(presenters.keySet());
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
        semaphore.lockReceiving();
        try {
            return cacheResource.get(request);
        } catch (ExecutionException ex) {
            LOG.error(request.getBody(), ex);
            return null;
        } finally {
            semaphore.lockReceiving();
        }
    }

    @Override
    public void showError(Presenter presenter, Exception error) {
        try {
            String resultingError;
            if (error.getCause() != null) {
                resultingError = "Caused by: " + error.getCause().toString() + "\n";
            } else {
                resultingError = "Caused by: " + error.toString() + "\n";
            }
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

    @Override
    public String hash(Model model) {
        String plaintext = "";
        Class modelClass = model.getClass();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object object;
            try {
                object = field.get(model);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                LOG.warn("Unable to hash value in {}", model.getClass());
                return "";
            }
            plaintext += String.valueOf(object);
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(plaintext.getBytes("utf8"));
            byte[] digest = md.digest();
            BigInteger bi = new BigInteger(1, digest);
            String hashedHex = String.format("%0" + (digest.length << 1) + "X", bi);
            return new String(Base64.getEncoder().encode(hashedHex.getBytes("utf8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOG.warn("Hashing failed in Wirex using SHA-1.");
            return "";
        }
    }

    @Override
    public <T> T settle(Class<T> menuClass) {
        if (!JMenuBar.class.isAssignableFrom(menuClass)) {

        }
        Bind bind = menuClass.getAnnotation(Bind.class);
        Class<? extends Presenter> presenterClass = bind.presenter();
        try {
            JMenuBar menuBar = (JMenuBar) menuClass.newInstance();
            final Presenter presenter = presenterClass.getConstructor(JMenuBar.class).newInstance(menuBar);
            Field[] fields = menuClass.getDeclaredFields();
            for (Field field : fields) {
                final Draw draw = field.getAnnotation(Draw.class);
                final Event event = field.getAnnotation(Event.class);
                if (draw != null) {
                    scanFieldWithDraw(draw, field, menuBar);
                }
                if (event != null) {
                    try {
                        Map<String, Method> presenterMethods = getMethods(presenterClass.getMethods());
                        String presenterMethodName = LegalIdentifierChecker.check(event.value());
                        if (!presenterMethods.containsKey(presenterMethodName)) {
                            LOG.warn("Is the method {} existed in {}?", presenterMethodName, presenterClass);
                            continue;
                        }
                        Map<String, Method> listeners = new HashMap<>(0);
                        Method injectListenerMethod = ListenerFactory.class.getMethod("ActionListener", Object.class, Map.class);
                        listeners.put("actionPerformed", presenterMethods.get(presenterMethodName));
                        ActionListener listener = (ActionListener) injectListenerMethod.invoke(null, presenter, listeners);
                        field.setAccessible(true);
                        Object component = field.get(menuBar);
                        Class clazz = field.getType();
                        Method injectListener = clazz.getMethod("addActionListener", ActionListener.class);
                        injectListener.invoke(component, listener);
                        field.set(menuBar, component);
                    } catch (InvalidKeywordFromBindingNameException ex) {
                        LOG.warn("Your binding identifier {} is not a valid Java identifer", ex.getInvalidToken());
                    } catch (ReservedKeywordFromBindingNameException ex) {
                        LOG.warn("Your binding identifier {} is a reserved Java keyword", ex.getInvalidToken());
                    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
                        LOG.error("Framework bug or your menubar presenter methods has one or more args! Fix the bug asap");
                    }
                }
            }
            return (T) menuBar;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.error("Framework bug!", ex);
            return null;
        }
    }

    private Model createModel(Class<? extends Model> modelClass) {
        try {
            Model model = modelClass.newInstance();
            Field[] fields = modelClass.getDeclaredFields();
            for (Field field : fields) {
                Class clazz = field.getType();
                field.setAccessible(true);
                if (clazz == Integer.class || clazz.getName().equals("int")) {
                    field.set(model, 0);
                } else if (clazz == Double.class || clazz == Float.class || clazz.getName().equals("double") || clazz.getName().equals("float")) {
                    field.set(model, 0.0);
                } else if (clazz == String.class) {
                    field.set(model, "");
                } else if (clazz == Boolean.class || clazz.getName().equals("boolean")) {
                    field.set(model, false);
                } else {
                    field.set(model, clazz.newInstance());
                }
                field.setAccessible(false);
            }
            model.store();
            return model;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Unable to load model", ex);
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
    public MVP prepare(Class viewClass) {
        return prepare(viewClass, null);
    }

    MVP prepare(Class viewClass, Window parent) {

        if (!JPanel.class.isAssignableFrom(viewClass)) {
            LOG.error("Attempted to prepare a non-JPanel {}", viewClass);
            return null;
        }

        int numberOfData = 0;
        int numberOfView = 0;

        Bind bind = (Bind) viewClass.getAnnotation(Bind.class);
        if (bind == null) {
            throw new ViewClassNotBindedException("Have you annotated @Bind to your " + viewClass.getSimpleName() + " class?");
        }

        Property property = (Property) viewClass.getAnnotation(Property.class);

        Class modelClass = bind.model();
        final Class presenterClass = bind.presenter();
        Field[] fields = viewClass.getDeclaredFields();

        Model model = null;

        if (models.containsKey(modelClass)) {
            try {
                model = modelCache.get(modelClass);
            } catch (ExecutionException ex) {
                LOG.error("Unable to load model", ex);
            }
        } else {
            model = createModel(modelClass);
            models.put(modelClass, model);
        }

        if (model instanceof XLive) {
            liveContainer.put(model.getClass().getName(), (XLive) model);
        }

        for (Field field : fields) {
            final Data data = field.getAnnotation(Data.class);
            final View view = field.getAnnotation(View.class);
            scanFieldWithData(data, field, model);
            scanFieldWithView(view, field);
            numberOfData = data != null ? numberOfData + 1 : numberOfData;
            numberOfView = view != null ? numberOfView + 1 : numberOfView;
        }

        Resource resource;
        try {
            if (property != null) {
                resource = property.value().newInstance();
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
        final ConcurrentHashMap<String, Invoker> runMethodParameters = new ConcurrentHashMap<>(0);
        if (retrieveAnnotations[0].length > 0) {
            retrieve = (Retrieve) retrieveAnnotations[0][0];
            for (String methodName : retrieve.value()) {
                MyActionListener myActionListener = new MyActionListener(presenterClass, presenter, methodName);
                Invoker invokeCode = new Invoker(myActionListener);
                runMethodParameters.put(methodName, invokeCode);
            }
        }

        if (retrieveAnnotations[0].length > 0) {
            new Thread(() -> {
                try {
                    run.invoke(presenter, runMethodParameters);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }, "init-" + presenter.getClass().getSimpleName()).start();
        }

        ++totalPreparedViews;
        LOG.info("{}{} loaded. Total prepared MVPs: {}", stackTab(), viewClass.getName(), totalPreparedViews);

        stackCount--;

        checkUncheckedOut(viewClass, numberOfData + numberOfView);
        checkNoResources(property);

        presenters.put(presenterClass, (Presenter) presenter);
        return new MVPObject(viewPanel, parent);
    }

    private void checkUncheckedOut(Class viewClass, int numberOfPush) {
        boolean flagDisplayedFirstLineWarning = true;
        for (int i = 0; i < numberOfPush; i++) {
            if (preStackUncheckedOutComponentCounts.isEmpty()) {
                return;
            }
            String identifier = preStackUncheckedOutComponentCounts.pop();
            if (components.containsKey(identifier)) {
                JComponent component = components.remove(identifier);
                if (flagDisplayedFirstLineWarning) {
                    flagDisplayedFirstLineWarning = false;
                    LOG.warn("Here are your components that were not checked out in {}:", viewClass.getSimpleName());
                }
                LOG.warn("\t{} of {}", identifier, component.getClass());
            }
        }
    }

    private void checkNoResources(Property property) {
        boolean flagDisplayedFirstLineWarning = true;
        if (property != null) {
            while (!preStackNoResourcePropertyCounts.isEmpty()) {
                String propertyName = preStackNoResourcePropertyCounts.poll();
                if (flagDisplayedFirstLineWarning) {
                    flagDisplayedFirstLineWarning = false;
                    LOG.warn("Here are your resources that were not supplied in {}:", property.value());
                }
                LOG.warn("\t{}", propertyName);
            }
        }
    }

    private String stackTab() {
        if (stackCount <= 0) {
            return "";
        } else {
            String result = "";
            for (int i = 0; i < stackCount; i++) {
                result += ".";
            }
            return result;
        }
    }

    private void scanFieldWithView(final View view, Field field) throws SecurityException {
        if (view != null) {
            field.setAccessible(true);
            Class subViewClass = field.getType();
            String panelId = view.value();
            MVP mvp;
            try {
                stackCount++;
                mvp = prepare(subViewClass);
            } catch (ViewClassNotBindedException | WrongComponentException ex) {
                mvp = new MVPObject(new JPanel());
            }
            components.put(panelId, mvp.getView());
            preStackUncheckedOutComponentCounts.add(panelId);
        }
    }
    
    private void scanFieldWithRenderAs(final RenderAs renderAs, final JComponent component) {
        if (renderAs != null) {
            Class<? extends XRenderer> renderer = renderAs.value();
            try {
                XRenderer xrenderer = renderer.newInstance();
                Class clazz = xrenderer.type();
                if (clazz == TreeCellRenderer.class && component instanceof JTree) {
                    TreeCellRenderer render = (TreeCellRenderer) clazz.newInstance();
                    JTree tree = (JTree) component;
                    tree.setCellRenderer(render);
                }
            } catch (InstantiationException ex) {
                LOG.warn("Check your renderer's implementation: {}", renderer.getName());
                return;
            } catch (IllegalAccessException ex) {
                LOG.error("Framework bug! Report for scanning renderas annotation.");
                return;
            }
        }
    }

    private void scanFieldWithData(final Data data, final Field field, final Model model) throws WrongComponentException {
        if (data != null) {
            Class clazz = field.getType();
            final String modelProperty;
            if (JComponent.class.isAssignableFrom(clazz)) {
                try {
                    Field listField = model.getClass().getDeclaredField(data.value());
                    Path path = listField.getAnnotation(Path.class);
                    if (path != null) {
                        String url = path.value();
                        if (url.startsWith("/")) {
                            url = path.value().substring(1);
                        } else {
                            url = path.value();
                        }
                        ServerRequest request = new ServerRequest("GET", hostname + url, Media.JSON, null, new ComponentModel(), null);
                        ServerResponse response = cacheResource.get(request);
                        if (response != null) {
                            ComponentModel componentModel = (ComponentModel) response.getMessage();
                            listField.setAccessible(true);
                            listField.set(model, componentModel.getComponent());
                            listField.setAccessible(false);
                        }
                    }
                    modelProperty = LegalIdentifierChecker.check(data.value());
                    JComponent component;
                    if (data.data().isEmpty()) {
                        component = bindComponent(clazz, model, modelProperty);
                    } else {
                        String selectedItemProperty = LegalIdentifierChecker.check(data.data());
                        component = bindComponent(clazz, model, modelProperty, selectedItemProperty);
                    }
                    RenderAs renderAs = field.getAnnotation(RenderAs.class);
                    scanFieldWithRenderAs(renderAs, component);
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
                } catch (NoSuchFieldException ex) {
                    LOG.warn("Is your field {} exists in your model {}?", field.getName(), model.getClass());
                    return;
                } catch (SecurityException | ExecutionException ex) {
                    LOG.error("Framework bug! Check combo box binding implementation", ex);
                    return;
                }
            } else {
                throw new WrongComponentException("Component " + field.getType() + " cannot be used for binding the model");
            }
            Rule rule = field.getAnnotation(Rule.class);
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
                    textField.getDocument()
                            .addDocumentListener(new MediatorFieldListener(field, modelProperty, validator, label, model));
                }
                mediators.put(modelProperty, label);
            }
        }
    }

    private void scanFieldWithText(final Text[] texts, Field field, final Object viewPanel, Resource resource) {
        String propertyText = "";
        if (texts.length > 0) {
            try {
                Object componentInstance = field.get(viewPanel);
                Class component = field.getType();
                if (component == JTable.class || JTable.class.isAssignableFrom(component)) {
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
                } else if (component == JPanel.class || JPanel.class.isAssignableFrom(component)) {
                    Method setBorderTextMethod = component.getMethod("setBorder", Border.class);
                    propertyText = texts[0].value();
                    Class resourceClass = resource.getClass();
                    Field resourceField = resourceClass.getDeclaredField(propertyText);
                    resourceField.setAccessible(true);
                    String textValue = resourceField.get(resource).toString();
                    setBorderTextMethod.invoke(componentInstance, BorderFactory.createTitledBorder(textValue));
                } else {
                    Method setTextMethod = component.getMethod("setText", String.class);
                    propertyText = texts[0].value();
                    Class resourceClass = resource.getClass();
                    Field resourceField = resourceClass.getDeclaredField(propertyText);
                    resourceField.setAccessible(true);
                    String textValue = resourceField.get(resource).toString();
                    setTextMethod.invoke(componentInstance, textValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                java.util.logging.Logger.getLogger(WirexCore.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchFieldException ex) {
                preStackNoResourcePropertyCounts.offer(propertyText);
            }
        }
    }

    private void scanFieldWithDraw(final Draw draw, Field field, final Object viewPanel) {
        new Thread(() -> {
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
        }, "draw").start();
    }

    private <T> void scanFieldWithEvent(final Event[] events, Field field, final Object viewPanel, final Class presenterClass, final Object presenter, final EventContainer[] eventContainers) throws IllegalArgumentException, SecurityException {
        String presenterMethodName = "";
        Map<String, Method> presenterMethods = getMethods(presenterClass.getMethods());
        try {
            if (events.length > 0) {
                Class component = field.getType();
                Object componentObject = field.get(viewPanel);
                Class<?> listenerType = ActionListener.class;
                Map<String, Method> listeners = new HashMap<>(0);
                for (Event event : events) {
                    presenterMethodName = LegalIdentifierChecker.check(event.value());
                    String listenerMethod = event.at().getMethod();
                    listenerType = event.at().getListener();
                    if (!presenterMethods.containsKey(presenterMethodName)) {
                        LOG.warn("Is the method {} existed in {}?", presenterMethodName, presenterClass);
                        continue;
                    }
                    listeners.put(listenerMethod, presenterMethods.get(presenterMethodName));
                }
                Method removeListenerFromComponentMethod = component.getMethod("remove" + listenerType.getSimpleName(), listenerType);
                Method getAllListeners = component.getMethod("get" + listenerType.getSimpleName() + "s");
                removeAllListeners((Class<T>) listenerType, componentObject, removeListenerFromComponentMethod, (T[]) getAllListeners.invoke(componentObject));

                Method addListenerToComponentMethod = component.getMethod("add" + listenerType.getSimpleName(), listenerType);
                Method injectListenerMethod = ListenerFactory.class.getMethod(listenerType.getSimpleName(), Object.class, Map.class);
                addListenerToComponentMethod.invoke(componentObject, injectListenerMethod.invoke(null, presenter, listeners));
            }

            if (eventContainers.length > 0) {
                Class component = field.getType();
                Object componentObject = field.get(viewPanel);
                for (EventContainer eventContainer : eventContainers) {
                    Class listenerType = eventContainer.listens();
                    Map<String, Method> listeners = new HashMap<>(0);
                    String listenerTypeName = listenerType.getSimpleName();
                    if (listenerTypeName.contains("Adapter")) {
                        listenerType = Class.forName("java.awt.event." + listenerTypeName.replace("Adapter", "Listener"));
                    }
                    String listenerDerivativeName = listenerTypeName.replace("Adapter", "Listener");
                    Method addListenerToComponentMethod = component.getMethod("add" + listenerDerivativeName, listenerType);
                    Method injectListenerMethod;
                    if (listenerTypeName.contains("istener")) {
                        injectListenerMethod = ListenerFactory.class.getMethod(listenerTypeName, Object.class, Map.class);
                    } else {
                        injectListenerMethod = AdapterFactory.class.getMethod(listenerTypeName, Object.class, Map.class);
                    }
                    for (Event event : eventContainer.events()) {
                        EventMethod method = event.at();
                        String listenerMethod = method.getMethod();
                        presenterMethodName = LegalIdentifierChecker.check(event.value());
                        if (!presenterMethods.containsKey(presenterMethodName)) {
                            LOG.warn("Is the method {} existed in {}?", presenterMethodName, presenterClass);
                            continue;
                        }
                        Method presenterMethod = presenterMethods.get(presenterMethodName);
                        listeners.put(listenerMethod, presenterMethod);
                    }
                    addListenerToComponentMethod.invoke(componentObject, injectListenerMethod.invoke(null, presenter, listeners));
                }
            }

        } catch (NoSuchMethodException ex) {
            LOG.warn("You're event {} may not work on your type {}.", presenterMethodName, field.getType().getSimpleName());
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
            LOG.warn("Framework bug! Check event annotation scanning subroutine.");
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
                        Model newModel = accessModelClass.newInstance();
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

    private <T> void removeAllListeners(Class<T> listenerType, Object component, Method invoke, T[] listeners) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (T listener : listeners) {
            invoke.invoke(component, listener);
        }
    }

    @Override
    public Map form(Presenter presenter) {
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
                if (listClass != XList.class) {
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .registerTypeAdapter(Date.class, new DateJsonDeserializer())
                .create();
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
            mvp.setIconImage(appIcon);
            mvp.display(fire.type(), true);
            mvp.setResizable(fire.dynamic());
        }
        if (dispose != null) {
            dispose((Presenter) presenter);
        }
    }

    private JComponent bindComponent(Class component, Object bean, String property) throws InstantiationException, IllegalAccessException, PropertyNotFoundException {
        return bindComponent(component, bean, property, null);
    }

    private JComponent bindComponent(Class component, Object bean, String property, String property2) throws InstantiationException, IllegalAccessException, PropertyNotFoundException {
        PresentationModel adapter = new PresentationModel(bean);
        ValueModel componentModel = adapter.getModel(property);
        JComponent newComponent = (JComponent) component.newInstance();
        if (JTextField.class == component || JTextField.class.isAssignableFrom(component)) {
            Bindings.bind((JTextField) newComponent, componentModel);
        } else if (JFormattedTextField.class == component || JFormattedTextField.class.isAssignableFrom(component)) {
            Bindings.bind((JFormattedTextField) newComponent, componentModel);
        } else if (JLabel.class == component || JLabel.class.isAssignableFrom(component)) {
            Bindings.bind((JLabel) newComponent, componentModel);
        } else if (JCheckBox.class == component || JCheckBox.class.isAssignableFrom(component)) {
            Bindings.bind((AbstractButton) newComponent, componentModel);
        } else if (JComboBox.class == component || JComboBox.class.isAssignableFrom(component)) {
            Object propertyObject = componentModel.getValue();
            if (!(propertyObject instanceof XList)) {
                LOG.warn("Property {} failed to bind. Your component may not work. "
                        + "Make sure your combo box binding should have exactly"
                        + "2 arguments for selected value and the list of values.");
            } else {
                XList list = (XList) propertyObject;
                if (list != null && property2 != null) {
                    DefaultEventComboBoxModel model = GlazedListsSwing.eventComboBoxModelWithThreadProxyList(list);
                    newComponent = new JComboBox(model);

                    final JComboBox comboboxComponent = (JComboBox) newComponent;
                    comboboxComponent.addItemListener((ItemEvent event) -> {
                        if (event.getStateChange() == ItemEvent.SELECTED) {
                            Object item = event.getItem();
                            adapter.getModel(property2).setValue(item);
                        }
                    });
                    adapter.getModel(property2).addPropertyChangeListener((PropertyChangeEvent evt) -> {
                        Object value = evt.getNewValue();
                        if (list.contains(value)) {
                            comboboxComponent.setSelectedItem(value);
                        } else {
                            comboboxComponent.setSelectedItem(null);
                        }
                    });
                }
            }
        } else if (JRadioButton.class == component || JRadioButton.class.isAssignableFrom(component)) {
            Bindings.bind((AbstractButton) newComponent, componentModel);
        } else if (JProgressBar.class == component || JProgressBar.class.isAssignableFrom(component)) {
            BeanAdapter beanAdapter = new BeanAdapter(bean, true);
            Bindings.bind(newComponent, "value", beanAdapter.getValueModel(property));
            Bindings.bind(newComponent, "indeterminate", beanAdapter.getValueModel(property + 2));
        } else if (JTextArea.class == component || JTextArea.class.isAssignableFrom(component)) {
            Bindings.bind((JTextArea) newComponent, componentModel);
        } else if (JXHyperlink.class == component || JXHyperlink.class.isAssignableFrom(component)) {
            BeanAdapter beanAdapter = new BeanAdapter(bean, true);
            Bindings.bind(newComponent, "text", beanAdapter.getValueModel(property));
        } else if (JXDatePicker.class == component || JXDatePicker.class.isAssignableFrom(component)) {
            Date date = (Date) componentModel.getValue();
            final JXDatePicker datePickerComponent = (JXDatePicker) newComponent;
            datePickerComponent.setDate(date);

            datePickerComponent.addActionListener((ActionEvent e) -> {
                Object item = datePickerComponent.getDate();
                adapter.getModel(property).setValue(item);
            });

            adapter.getModel(property).addPropertyChangeListener((PropertyChangeEvent evt) -> {
                Date value = (Date) evt.getNewValue();
                datePickerComponent.setDate(value);
            });
        } else if (JTree.class == component || JTree.class.isAssignableFrom(component)) {
            try {
                Field listField = bean.getClass().getDeclaredField(property);
                Class modelClass = listField.getType();
                Object model = componentModel.getValue();
                EventList eventList = (EventList) model;
                SortedList sortedList = new SortedList(eventList, null);
                TreeList list = new TreeList(sortedList, new XTreeFormat(model), TreeList.NODES_START_EXPANDED);
//                JTree tree = new JTree(new EventTreeModel(list));
                JTree tree = new JTree(new EventTreeModel(list));
                if (JTree.class == component) {
                    newComponent = tree;
                } else {
                    newComponent = (JComponent) component.getConstructor(TreeModel.class).newInstance(new EventTreeModel(list));
                }
//                tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
//                    System.out.println(tree.getLastSelectedPathComponent());
//                });
//                tree.setCellRenderer(new TreeCellRenderer() {
//                    @Override
//                    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                        JLabel label = new JLabel();
//                        try {
//                            TreeList.Node list = (TreeList.Node) value;
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
                Field[] columns = listTypeClass.getDeclaredFields();
                String[] propertyNames;
                String[] propertyTexts;
                boolean[] editable;
                Map<String, ComponentModel> comboBoxList = new HashMap<>();
                int numOfTransient = 0;
                for (int i = 0; i < fields.length; i++) {
                    int modifiers = fields[i].getModifiers();
                    if (Modifier.isTransient(modifiers)) {
                        numOfTransient++;
                    } else {
                        columns[i - numOfTransient] = fields[i];
                    }
                }

                if (Model.class.isAssignableFrom(listTypeClass)) {
                    propertyNames = new String[fields.length - numOfTransient];
                    propertyTexts = new String[fields.length - numOfTransient];
                    editable = new boolean[fields.length - numOfTransient];
                    for (int i = 0; i < propertyNames.length; i++) {
                        Column column = columns[i].getAnnotation(Column.class);
                        Path path = columns[i].getAnnotation(Path.class);
                        Editable edit = columns[i].getAnnotation(Editable.class);
                        editable[i] = edit != null; // TODO: Table cell editable
                        if (path != null) {
                            String url = path.value();
                            if (url.startsWith("/")) {
                                url = path.value().substring(1);
                            } else {
                                url = path.value();
                            }
                            ServerRequest request = new ServerRequest("GET", hostname + url, Media.JSON, null, new ComponentModel(), null);
                            ServerResponse response = cacheResource.get(request);
                            if (response != null) {
                                ComponentModel comboBoxComponentModel = (ComponentModel) response.getMessage();
                                comboBoxList.put(column.value(), comboBoxComponentModel);
                                editable[i] = true;
                            }
                        }
                        String columnName;
                        if (column != null) {
                            columnName = column.value();
                        } else {
                            columnName = columns[i].getName();
                        }
                        propertyNames[i] = columns[i].getName();
                        propertyTexts[i] = columnName;
                    }
                } else {
                    EventList list = (EventList) componentModel.getValue();
                    propertyNames = new String[]{"value"};
                    propertyTexts = new String[]{"Value"};
                    editable = new boolean[]{false};
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, new XObject(list.get(i)));
                    }
                    adapter.setValue(property, list);
                    listTypeClass = XObject.class;
//                    System.out.println(listTypeClass);
                }

                EventList rows = (EventList) adapter.getModel(property).getValue();
//                TableFormat tf = GlazedLists.tableFormat(listTypeClass, propertyNames, propertyTexts);

                if (JTable.class == component) {
                    JTable table = new JTable(new EventTableModel(rows, propertyNames, propertyTexts, editable));
                    newComponent = table;
                } else {
                    JXTable table = new JXTable(new EventTableModel(rows, propertyNames, propertyTexts, editable));
                    newComponent = table;
                }

                for (Map.Entry<String, ComponentModel> entry : comboBoxList.entrySet()) {
                    XList list = entry.getValue().getComponent();
                    DefaultEventComboBoxModel model = GlazedListsSwing.eventComboBoxModelWithThreadProxyList(list);
                    ((JTable) newComponent).getColumn(entry.getKey()).setCellEditor(new DefaultCellEditor(new JComboBox(model)));
                }

                if (property2 != null) {
                    final JTable tableComponent = (JTable) newComponent;
                    tableComponent.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
                        XList list = (XList) adapter.getModel(property).getValue();
                        Object selectedRow = list.get(tableComponent.getSelectedRow());
                        adapter.getModel(property2).setValue(selectedRow);
                    });
                }

            } catch (NoSuchFieldException | SecurityException | ExecutionException ex) {
                LOG.error("Unable to bind component " + component + " with " + property, ex);
                newComponent = new JTable();
            }

        } else if (JPasswordField.class == component || JPasswordField.class.isAssignableFrom(component)) {
            newComponent = BasicComponentFactory.createPasswordField(componentModel);
        } else if (XComponent.class.isAssignableFrom(component)) {
            XComponent xcomponent = (XComponent) newComponent;
            adapter.getModel(property).addPropertyChangeListener((PropertyChangeEvent evt) -> {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        Object value = evt.getNewValue();
                        xcomponent.setValue(value);
                    }
                });
            });
            xcomponent.addValueListener((Object value) -> {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Field field = bean.getClass().getDeclaredField(property);
                            field.setAccessible(true);
                            field.set(bean, value);
                            field.setAccessible(false);
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                            LOG.error("Framework bug when accessing your Custom Component binded property {} in {}", property, bean.getClass());
                        }
                    }
                });
            });
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
        preStackUncheckedOutComponentCounts.add(property);
        return newComponent;
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
        presenters.remove(presenter.getClass());
        Class clazz = panel.getClass();
        Bind bind = (Bind) clazz.getAnnotation(Bind.class);
        models.remove(bind.model());
        totalPreparedViews--;
    }

    private void scanFieldWithBalloon(final Balloon balloon, final Field field, final Object viewPanel) throws ViewClassNotBindedException, WrongComponentException {
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
                        JComponent finalBalloonPanel;
                        if (bind != null) {
                            MVP mvp = prepare(viewClass);
                            finalBalloonPanel = mvp.getView();
                        } else {
                            finalBalloonPanel = (JComponent) viewClass.newInstance();
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

    private void scanFieldWithPermit(final Permit permit, final Field field, final Object viewPanel) {
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

    @Override
    public void setAppIcon(Icon appIcon) {
        this.appIcon = ((ImageIcon) appIcon).getImage();
    }

    private BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        return bi;
    }

    @Override
    public XLive releaseXLive(String name) {
        return liveContainer.get(name);
    }

    @Override
    public void toggleEncryption(boolean toggle) {
        this.encrypt = toggle;
    }

    private class MyActionListener implements ActionListener {

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

    public class ComponentModel extends Model {

        private XList component;

        public XList getComponent() {
            return component;
        }

        public void setComponent(XList component) {
            this.component = component;
        }

        @Override
        public String toString() {
            return "ComponentModel{" + "component=" + component + '}';
        }

    }

    private class MediatorFieldListener implements DocumentListener {

        private final Field field;
        private final ValueModel valueModel;
        private final ConstraintValidator validator;
        private final JLabel label;
        private final Model model;
        private final String modelProperty;

        MediatorFieldListener(Field field, String modelProperty, ConstraintValidator validator, JLabel label, Model model) {
            this.field = field;
            this.validator = validator;
            this.label = label;
            this.model = model;
            this.modelProperty = modelProperty;
            PresentationModel adapter = new PresentationModel(model);
            this.valueModel = adapter.getModel(modelProperty);
        }

        public void validate() {
            Optional optional = field.getAnnotation(Optional.class);
            Object value = valueModel.getValue();
            String inputText = value != null ? value.toString() : "";

            if (optional != null) {
                return;
            }
            if (validator.isValid(inputText, null)) {
                label.setForeground(Color.BLUE);
                Object state = model.getUndoObject().get(modelProperty);
                state = state != null ? state : "";
                if (state.equals(inputText)) {
                    label.setForeground(Color.BLACK);
                }
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

    }

    private class MVPObject implements MVP {

        private final JPanel viewPanel;
        private final Window parent;

        private String title = "Untitled";

        MVPObject(Object viewPanel) {
            this(viewPanel, null);
        }

        MVPObject(Object viewPanel, Window parent) {
            if (viewPanel instanceof JPanel) {
                this.viewPanel = (JPanel) viewPanel;
            } else {
                JPanel panel = new JPanel();
                panel.removeAll();
                panel.add((Component) viewPanel);
                this.viewPanel = panel;
            }
            this.parent = parent;
        }

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
                    ((Frame) window).setTitle(title);
                } else {
                    ((Dialog) window).setTitle(title);
                }
            }
        }

        @Override
        public void display(final Class<? extends Window> window, final Boolean isVisible) {
            EventQueue.invokeLater(() -> {
                Window dialog;
                if (window == JFrame.class) {
                    dialog = new JFrame();
                    ((Frame) dialog).setTitle(title);
                    ((JFrame) dialog).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    ((RootPaneContainer) dialog).setContentPane(viewPanel);
                    if (appIcon != null) {
                        dialog.setIconImage(appIcon);
                    }
                } else {
                    dialog = new JDialog(parent);
                    ((Dialog) dialog).setTitle(title);
                    ((JDialog) dialog).setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    ((RootPaneContainer) dialog).setContentPane(viewPanel);
                    if (appIcon != null) {
                        dialog.setIconImage(appIcon);
                    }
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

        @Override
        public void setIconImage(Image icon) {
            Window window = SwingUtilities.getWindowAncestor(viewPanel);
            if (window != null && icon != null) {
                if (window.getClass() == JFrame.class) {
                    ((JFrame) window).setIconImage(icon);
                } else {
                    ((JDialog) window).setIconImage(icon);
                }
            }
        }

    }
}
