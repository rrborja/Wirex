package net.wirex;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import net.wirex.annotations.Snip;
import net.wirex.enums.Media;
import net.wirex.interfaces.Model;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public class SessionController {

    protected static String getCookie() {
        return SessionHolder.sessionId;
    }

    public static void at(String location) {
        SessionHolder.location = location;
    }
    
    public static void authenticate(Map<String, String> args) {
        authenticate(
                args.get("username"), 
                args.get("password")
            );
    }

    public static void authenticate(String username, String password) {
        try {
            final URI uriLocation = new URI(SessionHolder.location);
            SessionHolder.userInfo = new UserInfo(username, password);
            ServerRequest request = new ServerRequest("POST", SessionHolder.location, Media.JSON, null, SessionHolder.userInfo, null);
            ServerResponse<String> response = AppEngine.push(request);
            String sessionId = response.getMessage();
            SessionHolder.sessionId = sessionId;
            SessionHolder.userInfo = null;
            CookieManager cookieManager = new CookieManager();
            CookieStore cookieStore = cookieManager.getCookieStore();
            HttpCookie cookie = new HttpCookie("JSESSIONID", SessionHolder.sessionId);
            cookieStore.add(uriLocation, cookie);
            CookieHandler.setDefault(cookieManager);
            cookieManager.setCookiePolicy(new CookiePolicy() {
                @Override
                public boolean shouldAccept(URI uri, HttpCookie cookie) {
                    String location = uriLocation.getHost();
                    String accessLocation = uri.getHost();
                    return accessLocation.equalsIgnoreCase(location);
                }
            });
        } catch (URISyntaxException ex) {
            LOG.error("Authentication URL {} is not a valid URL.", SessionHolder.location);
            return;
        }
    }

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SessionController.class.getName());

    private static class SessionHolder {

        public static String location;
        public static UserInfo userInfo;
        public static String sessionId;
    }

    private static class UserInfo extends Model {

        private String username;
        private @Snip String password;

        UserInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

}
