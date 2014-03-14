package net.wirex;

import java.util.HashMap;
import java.util.Map;
import net.wirex.annotations.Snip;
import net.wirex.enums.Media;
import net.wirex.exceptions.UnsuccessfulServerResponseException;
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

    public static void authenticate(Map<String, String> args) throws UnsuccessfulServerResponseException {
        authenticate(
                args.get("username"),
                args.get("password")
        );
    }

    public static Map<String, String> credentials() {
        Map<String, String> map = new HashMap<>();
        map.put("username", SessionHolder.userInfo.username);
        map.put("password", SessionHolder.userInfo.password);
        return map;
    }

    public static void authenticate(String username, String password) throws UnsuccessfulServerResponseException {
        SessionHolder.userInfo = new UserInfo(username, password);
        ServerRequest request = new ServerRequest("POST", SessionHolder.location, Media.JSON, null, SessionHolder.userInfo, null);
        ServerResponse<String> response = AppEngine.push(request);
        String sessionId = response.getMessage();
        SessionHolder.sessionId = sessionId;
    }

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SessionController.class.getName());

    private static class SessionHolder {

        public static String location;
        public static UserInfo userInfo = new UserInfo("username", "password");
        public static String sessionId;
    }

    private static class UserInfo extends Model {

        private String username;
        private @Snip
        String password;

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
