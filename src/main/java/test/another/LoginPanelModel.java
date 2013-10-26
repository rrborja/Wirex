/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.another;

import com.google.gson.annotations.Expose;
import net.wirex.interfaces.Model;

/**
 *
 * @author RBORJA
 */
public class LoginPanelModel extends Model {

    private @Expose String username;
    private @Expose String password;
    private Boolean remember;

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

    public Boolean getRemember() {
        return remember;
    }

    public void setRemember(Boolean remember) {
        this.remember = remember;
    }
    
}
