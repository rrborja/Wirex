/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import com.sun.jersey.api.client.ClientResponse;

/**
 *
 * @author RBORJA
 */
public class WebResponse {

    private Integer status;
    private String message;

    public WebResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
