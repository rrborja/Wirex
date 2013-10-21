package net.visp.mvpshortcut;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import net.visp.wirex.ApplicationControllerFactory;
import net.visp.wirex.MVP;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        ApplicationControllerFactory.connect("http://staging.userservices.net:8080/g8");
        MVP mvpController = ApplicationControllerFactory.prepare(MyView.class);
        mvpController.display(JFrame.class);
    }
}
