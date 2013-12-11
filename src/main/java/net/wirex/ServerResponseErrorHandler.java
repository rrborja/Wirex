package net.wirex;

import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ritchie Borja
 */
public final class ServerResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ServerResponseErrorHandler.class.getName());
    
    private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        InputStream in = response.getBody();
        String body = IOUtils.toString(in, "UTF-8");
//        LOG.warn(body);
    }
    

}
