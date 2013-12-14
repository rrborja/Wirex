package net.wirex;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
class ConsoleEngine {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ConsoleEngine.class.getSimpleName());

    private static Model model;
    private static JPanel view;
    private static Presenter presenter;

    public static String mask(String consolePassword) {
        String result = "";
        for (int i = 0; i < consolePassword.length(); i++) {
            result += "*";
        }
        return result;
    }

    public static void execute(String command, String... params) {
        Class runtime = ConsoleEngine.class;
        Method methods[] = runtime.getDeclaredMethods();
        for (Method method : methods) {
            LOG.info("{} {} {} {}", method.getName(), method.getParameterCount(), params.length, (method.getName().equals(command) && method.getParameterCount() == params.length && params.length > 0));
            if (method.getName().equals(command) && method.getParameterCount() <= 0 && params.length <= 0) {
                try {
                    method.invoke(null);
                    return;
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.error("{}: command not found", command);
                    return;
                }
            } else if ((method.getName().equals(command) && method.getParameterCount() == params.length && params.length > 0)) {
                try {
                    method.invoke(null, params);
                    return;
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.error("{}: command not found", command);
                    ex.printStackTrace();
                    return;
                }
            } else if ((method.getName().equals(command) && method.getParameterCount() != params.length && method.getParameterCount() > 0)) {
                LOG.error("Refer documentation in using {}", command);
            }
        }
        LOG.error("{}: command not found", command);
    }

    public static void list(String type) {
        switch (type) {
            case "model":
                List<Class> modelList = AppEngine.getInstance().listModels();
                modelList.stream().forEach((model1) -> {
                    LOG.info("{}", model1.getName());
                });
                break;
            case "view":
                break;
            case "presenter":
                List<Class> presenterList = AppEngine.getInstance().listPresenters();
                presenterList.stream().forEach((presenter1) -> {
                    LOG.info("{}", presenter1.getName());
                });
                break;
            default:
                LOG.error("{} is unknown. Use either model, view or presenter", type);
        }
    }

    public static void access() {

    }
    
    public static void invoke(String method) {
        if (presenter == null) {
            LOG.info("No presenter staged");
            return;
        }
        Class presenterClass = presenter.getClass();
        try {
            Method invoker = presenterClass.getDeclaredMethod(method);
            invoker.invoke(presenter);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void modify(String property, String value) {
        if (model == null) {
            LOG.info("No model staged");
            return;
        }
        Class modelClass = model.getClass();
        try {
            Method setter = new PropertyDescriptor(property, modelClass).getWriteMethod();
            setter.invoke(model, value);
        } catch (IntrospectionException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void read(String property) {
        if (model == null) {
            LOG.info("No model staged");
            return;
        }
        Class modelClass = model.getClass();
        try {
            Method getter = new PropertyDescriptor(property, modelClass).getReadMethod();
            getter.invoke(model);
        } catch (IntrospectionException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ConsoleEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void check(String type) {
        switch (type) {
            case "model":
                if (model == null) {
                    LOG.info("No model staged");
                    break;
                }
                LOG.info("Model {} staged", model.getClass().getName());
                break;
            case "view":
                if (view == null) {
                    LOG.info("No view staged");
                    break;
                }
                LOG.info("View {} staged", view.getClass().getName());
                break;
            case "presenter":
                if (presenter == null) {
                    LOG.info("No presenter staged");
                    break;
                }
                LOG.info("Presenter {} staged", presenter.getClass().getName());
                break;
            default:
                LOG.error("{} is unknown. Use either model, view or presenter", type);
        }
    }

    public static void access(String type, String input) {
        switch (type) {
            case "model":
                try {
                    model = AppEngine.checkoutModel((Class<? extends Model>) Class.forName(input));
                } catch (ClassNotFoundException ex) {
                    LOG.error("{} not found", input);
                }
                break;
            case "view":
                break;
            case "presenter":
                try {
                    presenter = AppEngine.access((Class<? extends Presenter>) Class.forName(input));
                } catch (ClassNotFoundException ex) {
                    LOG.error("{} not found", input);
                }
                break;
            default:
                LOG.error("{} is unknown. Use either model, view or presenter", type);
        }
    }

    public static void quit() {
        System.exit(0);
    }

    public static void login() {
        Scanner username = new Scanner(System.in);
        ConsoleProcess.systemUser = "";
        System.out.print("Login as: ");
        String consoleUsername = username.nextLine();

        System.out.print(consoleUsername + "@wirex's password: ");

        String consolePassword;
        if (null != System.console()) {
            consolePassword = new String(System.console().readPassword());
        } else {
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Console password:");
            JPasswordField pass = new JPasswordField(10);
            panel.add(label);
            panel.add(pass);
            String[] options = new String[]{"Proceed", "Cancel"};
            int option = JOptionPane.showOptionDialog(null, panel, "Login",
                    JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[1]);
            if (option == 0) {
                consolePassword = new String(pass.getPassword());
                System.out.println(ConsoleEngine.mask(consolePassword));
            } else {
                consolePassword = "";
            }
        }
        if (ConsoleEngine.login(consoleUsername, consolePassword)) {
            System.out.println("Login successful!");
            ConsoleProcess.systemUser = consoleUsername + "@";
        } else {
            System.out.println("Access denied!");
        }
    }

    public static boolean login(String username, String password) {

        return username.equals("root") && password.equals("v1sprock5");

    }

}
