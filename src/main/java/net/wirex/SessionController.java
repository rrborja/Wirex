package net.wirex;

import java.util.HashMap;
import java.util.Map;
import net.wirex.annotations.Snip;
import net.wirex.enums.Media;
import net.wirex.interfaces.Model;

/**
 *
 * @author Ritchie Borja
 */
public class SessionController {

    protected static String getCookie() {
        return SessionHolder.sessionId;
    }
    
    public static void authenticate(String username, char[] password) {
        SessionHolder.userInfo = new UserInfo(username, password);
        ServerRequest request = new ServerRequest("POST", SessionHolder.location, Media.JSON, null, SessionHolder.userInfo, null);
        ServerResponse<String> response = AppEngine.push(request);
        String sessionId = response.getMessage();
        SessionHolder.sessionId = sessionId;
        SessionHolder.userInfo = null;
    }
    
    private static class SessionHolder {
        public static String location;
        public static UserInfo userInfo;
        public static String sessionId;
    }
    
    private static class UserInfo extends Model {

        private String username;
        private @Snip char[] password;

        UserInfo(String username, char[] password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public char[] getPassword() {
            return password.clone();
        }

        public void setPassword(char[] password) {
            this.password = password;
        }

    }

}
