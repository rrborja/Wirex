package net.wirex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

/**
 *
 * @author Ritchie Borja
 */
public class ServerRequestCallback implements RequestCallback {

    protected final Log logger = LogFactory.getLog(getClass());
    private final HttpEntity requestEntity;

    public ServerRequestCallback(HttpEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    @Override
    public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
        HttpHeaders httpHeaders = httpRequest.getHeaders();
        HttpHeaders requestHeaders = requestEntity.getHeaders();
        if (!requestHeaders.isEmpty()) {
            httpHeaders.putAll(requestHeaders);
        }
        InputStream inputStream = new ByteArrayInputStream(requestEntity.getBody().toString().getBytes());
        OutputStream requestOutputStream = httpRequest.getBody();
        try {
            int copiedBytes = IOUtils.copy(inputStream, requestOutputStream);
        } finally {
            IOUtils.closeQuietly(requestOutputStream);
        }
    }
}
