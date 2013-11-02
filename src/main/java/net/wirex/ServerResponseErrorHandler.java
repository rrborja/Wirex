/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author RBORJA
 */
public class ServerResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger LOG = Logger.getLogger(ServerResponseErrorHandler.class.getName());
    
    private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        InputStream in = response.getBody();
        String body = IOUtils.toString(in, "UTF-8");
        LOG.log(Level.WARNING, body);
    }
    

}
