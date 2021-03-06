package net.wirex;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.structures.XLive;

/**
 *
 * @author Ritchie Borja
 */
public final class AppEngine {

    private static final Wirex instance = new WirexCore();

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
        return instance.checkout(name);
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
            return instance.checkout(component, name);
        }

    public static  void setPermissionModel(Class<? extends Model> modelClass) {
        instance.setPermissionModel(modelClass);
    }
    
    public static JLabel mediator(String name) {
        return instance.mediator(name);
    }
    
    protected static void setError(Presenter presenter, Exception ex) {
        instance.showError(presenter, ex);
    }
    
    public static String getErrorMessage() {
        return instance.getError();
    }
    
    public static BufferedImage getScreenshot() {
        return instance.getScreenshot();
    }
    
    public static Map form(Presenter presenter) {
        return instance.form(presenter);
    }
    
    public static Icon icon(String filename) {
        return instance.icon(filename);
    }

    public static <T> T access(Class<T> presenter) {
        return instance.access(presenter);
    }

    protected static Model checkoutModel(Class<? extends Model> modelClass) {
        return instance.checkoutModel(modelClass);
    }
    
    public static void setTrayIcon(String iconName) {
        instance.setTrayIcon(iconName);
    }
    
    public static <T> T settle(Class<T> menuBar) {
        return instance.settle(menuBar);
    }

    public static <T> T checkout(Class<T> component, String name, String url) {
        return instance.checkout(component, name, url);
    }

    public static  void toggleEncryption(boolean toggle) {
        instance.toggleEncryption(toggle);
    }
    
    static XLive releaseXLive(String name) {
        return instance.releaseXLive(name);
    }
    
    /**
     * Connects a Java EE web server that supports REST transactions
     *
     * @param url The url to connect to a server that processes REST
     * transactions
     */
    public static void connect(String url) {
        instance.connect(url);
    }

    /**
     * Locates a suite of icons from a web server for the ResourceBundle feature
     *
     * @param url The URL to retrieve the icons
     */
    public static void locateResource(String url) {
        instance.locateResource(url);
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
        return instance.push(request);
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
        return instance.prepare(viewClass);
    }

    /**
     * Deserialization tool to transfer a data from a model to the model origin.
     *
     * This method must not be called unless you know what you're doing.
     *
     * @param model The origin model
     * @param fromJson The model to be transfered
     */
    public static synchronized void deserialize(Model model, Model fromJson) {
        instance.deserialize(model, fromJson);
    }

    protected static String encodeToUrl(Model body) {
        return instance.encodeToUrl(body);
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
        return instance.snip(model);
    }

    /**
     * Injects the Wirex REST specification listener to the event components.
     *
     * @param presenter The presenter object to inject the server connectors to
     * its annotated methods
     * @param method The method to inject with
     */
    protected static synchronized void injectRestSpec(Object presenter, Method method) {
        instance.injectRestSpec(presenter, method);
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
    protected static synchronized void proceed(Object presenter, Method method) throws ViewClassNotBindedException, WrongComponentException {
        instance.proceed(presenter, method);
    }

    /**
     * Disposes a presenter's binded View
     *
     * @param presenter The presenter where its binded View to be disposed
     */
    public static void dispose(Presenter presenter) {
        instance.dispose(presenter);
    }
    
    public static String hash(Model model) {
        return instance.hash(model);
    }
    
    public static void setAppIcon(String icon) {
        instance.setAppIcon(icon(icon));
    }
    
    protected static Wirex getInstance() {
        return instance;
    }
    
    /**
     * Make AppEngine a singleton with no constructor
     */
    private AppEngine() {
    }
}
